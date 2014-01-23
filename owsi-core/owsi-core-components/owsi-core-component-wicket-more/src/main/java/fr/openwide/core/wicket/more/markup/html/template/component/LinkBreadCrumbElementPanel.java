package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;

import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

@Deprecated
public class LinkBreadCrumbElementPanel extends GenericPanel<String> {

	private static final long serialVersionUID = 5385792712763242343L;
	
	public LinkBreadCrumbElementPanel(String id, BreadCrumbElement breadCrumbElement, BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
		super(id, breadCrumbElement.getLabelModel());
		
		Link<Void> breadCrumbLink = new BookmarkablePageLink<Void>("breadCrumbElementLink", breadCrumbElement.getPageClass(),
				breadCrumbElement.getPageParameters()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				setVisible(Session.get().getAuthorizationStrategy().isInstantiationAuthorized(getPageClass()));
			}
		};
		breadCrumbLink.setBody(getModel());
		breadCrumbLink.add(renderingBehavior);
		add(breadCrumbLink);
		
		add(
				new EnclosureBehavior() {
					private static final long serialVersionUID = 1L;
					@Override
					protected void setVisibility(Component component, boolean visible) {
						component.setVisible(visible);
					}
				}
				.component(breadCrumbLink)
		);
	}

}
