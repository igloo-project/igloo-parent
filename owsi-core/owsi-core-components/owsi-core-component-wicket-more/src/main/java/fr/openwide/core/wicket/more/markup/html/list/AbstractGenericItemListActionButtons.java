package fr.openwide.core.wicket.more.markup.html.list;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;


public abstract class AbstractGenericItemListActionButtons<T extends Serializable> extends GenericPanel<T> {

	private static final long serialVersionUID = 7007771250037945470L;

	private WebMarkupContainer editLinkHidden;

	private WebMarkupContainer deleteLinkHidden;

	private WebMarkupContainer actionLinkHidden;

	public AbstractGenericItemListActionButtons(final String id, final IModel<? extends T> itemModel) {
		super(id, itemModel);
		
		editLinkHidden = new WebMarkupContainer("editLinkHidden");
		deleteLinkHidden = new WebMarkupContainer("deleteLinkHidden");
		actionLinkHidden = new WebMarkupContainer("actionLinkHidden");
		add(editLinkHidden, deleteLinkHidden, actionLinkHidden);
		
		MarkupContainer actionLink = getActionLink("actionLink", itemModel);
		actionLink.add(new AttributeModifier("alt", getActionText(itemModel)));
		actionLink.add(new AttributeModifier("title", getActionText(itemModel)));
		WebMarkupContainer actionIcon = new WebMarkupContainer("actionIcon");
		actionIcon.add(new AttributeAppender("class", Model.of("icon"), " "));
		actionIcon.add(new AttributeAppender("class", getActionBootstrapIconClass(itemModel), " "));
		actionLink.add(actionIcon);
		add(actionLink);
		
		add(getEditLink("editLink", itemModel));
		
		add(getDeleteLink("deleteLink", itemModel));
	}

	protected abstract IModel<String> getActionBootstrapIconClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getActionText(final IModel<? extends T> itemModel);

	protected abstract MarkupContainer getActionLink(final String id, final IModel<? extends T> itemModel);

	protected abstract Component getEditLink(String id, final IModel<? extends T> itemModel);

	protected abstract Component getDeleteLink(String id, final IModel<? extends T> itemModel);

	public WebMarkupContainer getEditLinkHidden() {
		return editLinkHidden;
	}

	public WebMarkupContainer getDeleteLinkHidden() {
		return deleteLinkHidden;
	}

	public WebMarkupContainer getActionLinkHidden() {
		return actionLinkHidden;
	}
}
