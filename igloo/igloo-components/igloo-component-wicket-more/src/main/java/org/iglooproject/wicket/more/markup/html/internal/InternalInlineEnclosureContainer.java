package org.iglooproject.wicket.more.markup.html.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.filter.EnclosureHandler;
import org.apache.wicket.markup.resolver.ComponentResolvers;
import org.apache.wicket.markup.resolver.ComponentResolvers.ResolverFilter;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.util.string.Strings;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InternalInlineEnclosureContainer extends WebMarkupContainer implements IComponentResolver {

	private static final long serialVersionUID = 1L;

	private static final Splitter CHILDREN_ID_SPLIITER = Splitter.on(",").omitEmptyStrings().trimResults();

	private List<String> childrenId = Lists.newArrayList();

	private Map<String, Component> childrenComponent = Maps.newHashMap();

	public InternalInlineEnclosureContainer(final String id, final String childrenId) {
		super(id);
		
		setOutputMarkupId(true);
		
		if (!Strings.isEmpty(childrenId)) {
			this.childrenId.addAll(CHILDREN_ID_SPLIITER.splitToList(childrenId));
		}
	}

	protected final Map<String, Component> getChildren() {
		if (childrenComponent == null) {
			childrenComponent = Maps.newHashMap();
		}
		
		for (String childId : childrenId) {
			childrenComponent.put(childId, getChild(childId));
		}
		
		return childrenComponent;
	}

	protected final Component getChild(String childId) {
		Component childComponent = childrenComponent.get(childId);
		
		if (childComponent != null) {
			return childComponent;
		}
		
		// try to find child when queued
		childComponent = resolveChild(this, childId);
		
		if (childComponent == null) {
			// try to find child when resolved
			childComponent = getChildComponent(new MarkupStream(getMarkup()), getEnclosureParent(), childId);
		}
		
		return childComponent;
	}

	private Component resolveChild(MarkupContainer container, String childId) {
		Component childController = container.get(childId);
		
		Iterator<Component> children = container.iterator();
		
		while (children.hasNext() && childController == null) {
			Component transparentChild = children.next();
			
			if (transparentChild instanceof TransparentWebMarkupContainer) {
				childController = resolveChild((MarkupContainer) transparentChild, childId);
			}
		}
		
		return childController;
	}

	@Override
	public boolean isVisible() {
		for (Component child : getChildren().values()) {
			if (child.determineVisibility()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		for (Component child : getChildren().values()) {
			child.configure();
			if (child.determineVisibility()) {
				setVisible(true);
				return;
			}
		}
		
		setVisible(false);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		
		// necessary when queued and lives with the page instead of just during render
		childrenComponent = null;
	}

	protected MarkupContainer getEnclosureParent() {
		MarkupContainer parent = getParent();
		
		if (parent == null) {
			throw new WicketRuntimeException("Unable to find parent component which is not a transparent resolver");
		}
		return parent;
	}

	private Component getChildComponent(final MarkupStream markupStream, MarkupContainer enclosureParent, String childId) {
		String fullChildId = childId.toString();
		
		Component controller = enclosureParent.get(fullChildId);
		if (controller == null) {
			int orgIndex = markupStream.getCurrentIndex();
			try {
				while (markupStream.isCurrentIndexInsideTheStream()) {
					markupStream.next();
					if (markupStream.skipUntil(ComponentTag.class)) {
						ComponentTag tag = markupStream.getTag();
						if ((tag != null) && (tag.isOpen() || tag.isOpenClose())) {
							String tagId = tag.getId();
							
							if (fullChildId.equals(tagId)) {
								ComponentTag fullComponentTag = new ComponentTag(tag);
								fullComponentTag.setId(childId.toString());
								
								controller = ComponentResolvers.resolve(enclosureParent, markupStream, fullComponentTag,
										new ResolverFilter() {
											@Override
											public boolean ignoreResolver(final IComponentResolver resolver) {
												return resolver instanceof EnclosureHandler;
											}
										});
								break;
							} else if (fullChildId.startsWith(tagId + PATH_SEPARATOR)) {
								fullChildId = Strings.afterFirst(fullChildId, PATH_SEPARATOR);
							}
						}
					}
				}
			} finally {
				markupStream.setCurrentIndex(orgIndex);
			}
		}
		
		checkChildComponent(controller, childId);
		return controller;
	}

	@Override
	public Component resolve(MarkupContainer container, MarkupStream markupStream, ComponentTag tag) {
		if (childrenComponent.containsKey(tag.getId())) {
			return childrenComponent.get(tag.getId());
		}
		return getEnclosureParent().get(tag.getId());
	}

	private void checkChildComponent(final Component controller, final String childId) {
		if (controller == null) {
			throw new WicketRuntimeException("Could not find child with id: " + childId + " in the wicket:enclosure-container");
		} else if (controller == this) {
			throw new WicketRuntimeException("Programming error: childComponent == enclose component; endless loop");
		}
	}

}
