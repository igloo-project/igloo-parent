package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class LinkBreadCrumbElementPanel extends GenericPanel<String> {

	private static final long serialVersionUID = 5385792712763242343L;

	public LinkBreadCrumbElementPanel(String id, BreadCrumbElement breadCrumbElement) {
		super(id, breadCrumbElement.getLabelModel());
		
		Link<Void> link = new BookmarkablePageLink<Void>("breadCrumbElementLink", breadCrumbElement.getPageClass(),
				breadCrumbElement.getPageParameters());
		link.add(new Label("breadCrumbElementLabel", getModel()));
		
		add(link);
	}

}
