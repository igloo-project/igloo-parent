package fr.openwide.core.wicket.more.markup.html.template.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class LinkBreadCrumbElementPanel extends GenericPanel<BreadCrumbElement> {

	private static final long serialVersionUID = 5385792712763242343L;

	public LinkBreadCrumbElementPanel(String id, IModel<BreadCrumbElement> model, boolean isSeparatorVisible) {
		super(id, model);
		
		WebMarkupContainer separator = new WebMarkupContainer("separator");
		separator.setVisible(isSeparatorVisible);
		add(separator);
		
		Link<Void> link = new BookmarkablePageLink<Void>("breadCrumbElementLink", getModelObject().getPageClass(), getModelObject().getPageParameters());
		link.add(new Label("breadCrumbElementLabel", new ResourceModel(getModelObject().getLabelKey(), getModelObject().getLabelKey())));
		
		add(link);
	}

}
