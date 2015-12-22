package fr.openwide.core.wicket.more.ajax;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.AbstractListener;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.ClassVisitFilter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.Visits;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.IAjaxModalPopupPanel;

public final class AjaxListeners {
	
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
	
	public static void addChildrenIfVisible(final AjaxRequestTarget target, MarkupContainer parent, Class<?> childCriteria) {
		Args.notNull(parent, "parent");
		Args.notNull(childCriteria, "childCriteria");
		
		parent.visitChildren(childCriteria, new IVisitor<Component, Void>() {
			@Override
			public void component(final Component component, final IVisit<Void> visit) {
				Component parent = component.getParent();
				if (component.isVisibleInHierarchy() || (parent == null || parent.isVisibleInHierarchy()) && component.getOutputMarkupPlaceholderTag()) {
					target.add(component);
				}
				visit.dontGoDeeper();
			}
		});
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
	public static AjaxRequestTarget.AbstractListener refresh(Class<? extends Component> first, Class<? extends Component> ... rest) {
		final List<Class<? extends Component>> list = Lists.asList(first, rest);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				for (Class<? extends Component> clazz : list) {
					target.addChildren(target.getPage(), clazz);
				}
			}
		};
	}
	
	@SafeVarargs
	public static AjaxRequestTarget.AbstractListener refresh(final MarkupContainer parent, Class<? extends Component> first, Class<? extends Component> ... rest) {
		final List<Class<? extends Component>> list = Lists.asList(first, rest);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				for (Class<? extends Component> clazz : list) {
					target.addChildren(parent, clazz);
				}
			}
		};
	}
	
	/**
	 * @return A listener that will trigger the refresh of every component in the page
	 * of the given class(es), <strong>except <code>self</code> and components in {@link IAjaxModalPopupPanel modals}</strong>.
	 */
	@SafeVarargs
	public static <T extends Component> AjaxRequestTarget.AbstractListener refreshOthersNotInAjaxModals(final T self, Class<? extends T> first, Class<? extends T> ... rest) {
		final List<Class<? extends T>> list = Lists.asList(first, rest);
		return new SerializableListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
				for (Class<? extends Component> clazz : list) {
					addChildrenNotInAjaxModalsExcept(target, target.getPage(), clazz, self);
				}
			}
		};
	}

	private static void addChildrenNotInAjaxModalsExcept(final AjaxRequestTarget target, MarkupContainer parent,
			Class<?> childCriteria, final Component excepted) {
		Args.notNull(parent, "parent");
		Args.notNull(childCriteria, "childCriteria");
		Visits.visitChildren(
				parent,
				new IVisitor<Component, Void>() {
					@Override
					public void component(final Component component, final IVisit<Void> visit) {
						target.add(component);
						visit.dontGoDeeper();
					}
				},
				new ClassVisitFilter(childCriteria) {
					@Override
					public boolean visitChildren(Object object) {
						return !IAjaxModalPopupPanel.class.isInstance(object);
					}
					@Override
					public boolean visitObject(Object object) {
						return super.visitObject(object) && !object.equals(excepted);
					}
				}
		);
	}
	
	public static ImmutableSet.Builder<AjaxRequestTarget.AbstractListener> chain() {
		return ImmutableSet.builder();
	}

	public static ImmutableMap.Builder<Collection<AjaxRequestTarget.AbstractListener>, Condition> chainCondition() {
		return ImmutableMap.builder();
	}

	

}
