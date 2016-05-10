package fr.openwide.core.wicket.more.ajax;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.AbstractListener;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.IVisitFilter;
import org.apache.wicket.util.visit.Visits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.IAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.repeater.IRefreshableOnDemandRepeater;
import fr.openwide.core.wicket.more.util.visit.VisitFilters;
import fr.openwide.core.wicket.more.util.visit.Visitors;

public final class AjaxListeners {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AjaxListeners.class);
	
	private AjaxListeners() {
	}
	
	public static void add(AjaxRequestTarget target, AjaxRequestTarget.AbstractListener ... listeners) {
		add(target, Arrays.asList(listeners));
	}
	
	public static void add(AjaxRequestTarget target, Iterable<? extends AjaxRequestTarget.AbstractListener> listeners) {
		for (AjaxRequestTarget.AbstractListener listener : listeners) {
			target.addListener(listener);
		}
	}
	
	public static void add(AjaxRequestTarget target, Map<Collection<AbstractListener>, Condition> listeners) {
		for (Entry<Collection<AbstractListener>, Condition> listener : listeners.entrySet()) {
			if (listener.getValue().applies()) {
				add(target, listener.getKey());
			}
		}
	}
	
	public static void addChildrenIfVisible(final AjaxRequestTarget target, MarkupContainer parent,
			Class<?> childCriteria) {
		Args.notNull(parent, "parent");
		Args.notNull(childCriteria, "childCriteria");
		
		Visits.visitChildren(
				parent, Visitors.addToTarget(target),
				VisitFilters.every(VisitFilters.including(childCriteria), VisitFilters.renderedComponents())
		);
	}
	
	public static AjaxRequestTarget.AbstractListener refreshPage() {
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				//target.add(target.getPage()); // Won't work, since the handler already decided started to respond without redirecting when this method is called
				throw new RestartResponseException(target.getPage());
			}
		};
	}
	
	/**
	 * Adds or remove items of the given refreshing view using javascript, without refreshing any of the existing items
	 * that were not removed nor added during this request.
	 * 
	 * <p><strong>WARNING:</strong> removing markup of removed items will only work for ListViews and RefreshingViews.
	 * Instances of RepeatingView (not a subclass) do not allow detection of the removed items.
	 * 
	 * <p><strong>WARNING:</strong> added items will be added as last child of the repeater's parent. If it's
	 * not what you want, you may simply wrap the repeater in a {@link WebMarkupContainer} whose single child is
	 * the repeater.
	 */
	public static AjaxRequestTarget.AbstractListener refreshNewAndRemovedItems(final IRefreshableOnDemandRepeater repeater) {
		if (!repeater.getParent().getOutputMarkupId()) {
			LOGGER.warn("Trying to update new and removed items on a repeater whose parent does not"
					+ " output its markup id. This is likely to fail on the client side. Repeater: {}", repeater);
		}
		return new RefreshNewAndRemovedItemsListener(repeater);
	}
	
	private static class RefreshNewAndRemovedItemsListener extends SerializableListener  {
		private static final long serialVersionUID = 1L;
		
		private IRefreshableOnDemandRepeater repeater;
		
		public RefreshNewAndRemovedItemsListener(IRefreshableOnDemandRepeater repeater) {
			this.repeater = repeater;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!getClass().equals(obj.getClass())) {
				return false;
			}
			RefreshNewAndRemovedItemsListener other = (RefreshNewAndRemovedItemsListener) obj;
			return new EqualsBuilder().append(repeater, other.repeater).isEquals();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(repeater).toHashCode();
		}
		
		@Override
		public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
			if (willUpdateRepeater(map)) {
				// Skip refresh, since the whole repeater will be rendered anyway
				return;
			}
			Set<Component> itemsBefore = Sets.newLinkedHashSet();
			Set<Component> itemsAfter = Sets.newLinkedHashSet();
			
			Iterators.addAll(itemsBefore, repeater.iterator());
			repeater.refreshItems();
			Iterators.addAll(itemsAfter, repeater.iterator());
			
			/*
			 * Remove markup of components that were removed from the repeater when calling beforeRender().
			 * 
			 * This will only work for self-populating components, such as ListViews or RefreshingViews.
			 * Others, such as an instance of RepeatingView (not a subclass), will have had their children
			 * removed before the call to refreshItems, so we can't possibly know these children
			 * existed in the first place.
			 */
			for (Component removedItem : Sets.difference(itemsBefore, itemsAfter)) {
				target.prependJavaScript(
						String.format(
								"Wicket.$('%s').remove();",
								removedItem.getMarkupId()
						)
				);
			}
			
			for (Component itemAfter : repeater) {
				if (!itemAfter.hasBeenRendered() && itemAfter.determineVisibility()) {
					// First-time rendering for this item: we should add it to the markup.
					target.prependJavaScript(
							String.format(
									"var item=document.createElement('%s');item.id='%s';" +
									"Wicket.$('%s').appendChild(item);",
									"tr", itemAfter.getMarkupId(), repeater.getParent().getMarkupId()
							)
					);
					target.add(itemAfter);
				}
			}
		}

		private boolean willUpdateRepeater(Map<String, Component> map) {
			Component cursor = repeater.getParent();
			while (cursor != null) {
				if (map.containsValue(cursor)) {
					return true;
				}
				cursor = cursor.getParent();
			}
			return false;
		}
	};

	/**
	 * @return A listener that will trigger the refresh of every given component.
	 */
	public static AjaxRequestTarget.AbstractListener refresh(Component first, Component ... rest) {
		final List<Component> list = Lists.asList(first, rest);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				for (Component component : list) {
					target.add(component);
				}
			}
		};
	}

	/**
	 * @return A listener that will trigger the refresh of every component in the page of the given class(es).
	 */
	@SafeVarargs
	public static AjaxRequestTarget.AbstractListener refresh(Class<? extends Component> first,
			Class<? extends Component> ... rest) {
		return refresh(VisitFilters.including(first, rest));
	}
	
	public static AjaxRequestTarget.AbstractListener refresh(final IVisitFilter ... additiveFilters) {
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				Visits.visitChildren(
						target.getPage(),
						Visitors.addToTarget(target),
						VisitFilters.every(additiveFilters)
				);
			}
		};
	}
	
	/**
	 * @deprecated Use {@link #refreshChildren(MarkupContainer, Class, Class...)} instead.
	 */
	@SafeVarargs
	public static AjaxRequestTarget.AbstractListener refresh(final MarkupContainer parent,
			Class<? extends Component> first, Class<? extends Component> ... rest) {
		return refreshChildren(parent, first, rest);
	}

	@SafeVarargs
	public static AjaxRequestTarget.AbstractListener refreshChildren(final MarkupContainer parent,
			Class<? extends Component> first, Class<? extends Component> ... rest) {
		final IVisitFilter filter = VisitFilters.including(first, rest);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				refreshChildren(parent, filter);
			}
		};
	}
	
	public static AjaxRequestTarget.AbstractListener refreshChildren(final MarkupContainer parent,
			IVisitFilter ... additiveFilters) {
		final IVisitFilter filter = VisitFilters.every(additiveFilters);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				Visits.visitChildren(
						parent,
						Visitors.addToTarget(target),
						filter
				);
			}
		};
	}
	
	/**
	 * @return A listener that will trigger the refresh of every component in the page
	 * of the given class(es), <strong>except <code>self</code> and components in {@link IAjaxModalPopupPanel modals}</strong>.
	 */
	@SafeVarargs
	public static <T extends Component> AjaxRequestTarget.AbstractListener refreshOthersNotInAjaxModals(final T self,
			Class<? extends T> first, Class<? extends T> ... rest) {
		Args.notNull(self, "self");
		return refresh(
				VisitFilters.every(
						VisitFilters.renderedComponents(),
						VisitFilters.including(first, rest),
						VisitFilters.downToIncluding(IAjaxModalPopupPanel.class),
						VisitFilters.excluding(self)
				)
		);
	}
	
	public static ImmutableSet.Builder<AjaxRequestTarget.AbstractListener> chain() {
		return ImmutableSet.builder();
	}

	public static ImmutableMap.Builder<Collection<AjaxRequestTarget.AbstractListener>, Condition> chainCondition() {
		return ImmutableMap.builder();
	}
	
}
