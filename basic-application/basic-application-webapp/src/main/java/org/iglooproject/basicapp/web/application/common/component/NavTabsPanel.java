package org.iglooproject.basicapp.web.application.common.component;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.web.application.common.util.BootstrapTabsUtils;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tab.BootstrapTabBehavior;
import org.iglooproject.wicket.more.util.model.Detachables;

public class NavTabsPanel extends Panel {
	
	private static final long serialVersionUID = -2317592648347012891L;
	
	private RepeatingView tabsRepeatingView;
	private RepeatingView tabsContentsRepeatingView;
	
	public NavTabsPanel(String id) {
		super(id);
		this.tabsRepeatingView = new RepeatingView("tabs");
		this.tabsContentsRepeatingView = new RepeatingView("tabsContents");
		
		add(tabsRepeatingView, tabsContentsRepeatingView);
	}
	
	public interface ITabFactory extends IDetachable {
		
		Component createLabel(String wicketId);
		Component createContent(String wicketId);
		
		@Override
		default void detach() {
			// nothing to do
		}
	}
	
	public abstract static class SimpleTabFactory implements ITabFactory {
		
		private static final long serialVersionUID = 1L;
		
		private final IModel<String> labelModel;
		
		public SimpleTabFactory(String labelKey) {
			this(new ResourceModel(labelKey));
		}
		
		public SimpleTabFactory(IModel<String> labelModel) {
			super();
			this.labelModel = labelModel;
		}
		
		@Override
		public Component createLabel(String wicketId) {
			return new CoreLabel(wicketId, labelModel);
		}
		
		@Override
		public void detach() {
			ITabFactory.super.detach();
			Detachables.detach(labelModel);
		}
	}
	
	public static class FeatureNotYetAvailableTabFactory extends SimpleTabFactory {
		
		private static final long serialVersionUID = 1L;
		
		public FeatureNotYetAvailableTabFactory(String labelKey) {
			super(labelKey);
		}
		
		public FeatureNotYetAvailableTabFactory(IModel<String> labelModel) {
			super(labelModel);
		}
		
		@Override
		public Component createLabel(String wicketId) {
			return super.createLabel(wicketId).setEscapeModelStrings(false);
		}
		
		@Override
		public Component createContent(String wicketId) {
			return new FeatureNotYetAvailablePanel(wicketId);
		}
	}
	
	public NavTabsPanel add(ITabFactory factory) {
		boolean first = tabsContentsRepeatingView.size() == 0;
		
		MarkupContainer tab = new WebMarkupContainer(tabsRepeatingView.newChildId());
		WebMarkupContainer link = new WebMarkupContainer("link");
		
		Component content = factory.createContent(tabsContentsRepeatingView.newChildId());
		
		BootstrapTabsUtils.decorateTabLink(link, content.getMarkupId());
		
		if (first) {
			Behavior activeBehavior = new ClassAttributeAppender("active show");
			link.add(activeBehavior);
			content.add(activeBehavior);
		}
		
		tabsRepeatingView.add(
			tab
				.add(
					link
						.add(factory.createLabel("label"))
				)
				.add(new BootstrapTabBehavior())
		);
		
		tabsContentsRepeatingView.add(
			content
				.setOutputMarkupId(true)
		);
		
		return this;
	}

}
