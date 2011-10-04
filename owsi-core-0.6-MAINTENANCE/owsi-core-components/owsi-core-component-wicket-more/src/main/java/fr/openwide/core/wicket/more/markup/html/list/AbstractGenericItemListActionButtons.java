package fr.openwide.core.wicket.more.markup.html.list;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox.FancyboxHtmlPanelBehavior;


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
		ContextImage actionIcon = new ContextImage("actionImage", getActionIconPath());
		actionIcon.add(new AttributeModifier("alt", true, getActionText()));
		actionIcon.add(new AttributeModifier("data-tooltip", true, getActionText()));
		actionLink.add(actionIcon);
		add(actionLink);
		
		add(getEditLink("editLink", itemModel));
		
		Component deleteLink = getDeleteLink("deleteLink", itemModel);
		WebMarkupContainer deleteLinkConfirmation = new WebMarkupContainer("deleteLinkConfirmation");
		
		Component confirmationDeleteLink = getConfirmationDeleteLink("confirmationDeleteLink", itemModel);
		Label deleteLinkConfirmationMessage = new Label("deleteLinkConfirmationMessage", new ResourceModel("common.deleteConfirmation"));
		Component cancelDeleteLink = getCancelDeleteLink("cancelDeleteLink", itemModel);
		
		deleteLinkConfirmation.add(confirmationDeleteLink, cancelDeleteLink, deleteLinkConfirmationMessage);
		
		deleteLink.add(new FancyboxHtmlPanelBehavior(deleteLinkConfirmation));
		
		add(deleteLink);
		add(deleteLinkConfirmation);
	}
	
	protected abstract IModel<String> getActionIconPath();
	
	protected abstract IModel<String> getActionText();
	
	protected abstract MarkupContainer getActionLink(final String id, final IModel<? extends T> itemModel);
	
	protected abstract Component getEditLink(String id, IModel<? extends T> itemModel);
	
	protected abstract Component getDeleteLink(String id, IModel<? extends T> itemModel);
	
	protected abstract Component getConfirmationDeleteLink(String id, IModel<? extends T> itemModel);
	
	protected abstract Component getCancelDeleteLink(String id, IModel<? extends T> itemModel);

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
