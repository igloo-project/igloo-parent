package fr.openwide.core.wicket.more.markup.html.list;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;


public abstract class AbstractGenericItemListActionButtons<T extends Serializable> extends GenericPanel<T> {

	private static final long serialVersionUID = 7007771250037945470L;

	private WebMarkupContainer editLinkHidden;

	private WebMarkupContainer deleteLinkHidden;

	private WebMarkupContainer actionLinkHidden;

	public AbstractGenericItemListActionButtons(final String id, final IModel<? extends T> itemModel) {
		super(id, itemModel);
		
		actionLinkHidden = new WebMarkupContainer("actionLinkHidden");
		editLinkHidden = new WebMarkupContainer("editLinkHidden");
		deleteLinkHidden = new WebMarkupContainer("deleteLinkHidden");
		add(actionLinkHidden, editLinkHidden, deleteLinkHidden);
		
		MarkupContainer actionLink = getActionLink("actionLink", itemModel);
		actionLink.add(new AttributeModifier("alt", getActionText(itemModel)));
		actionLink.add(new AttributeModifier("title", getActionText(itemModel)));
		actionLink.add(new AttributeAppender("class", getActionBootstrapColorClass(itemModel), " "));
		
		WebMarkupContainer actionIcon = new WebMarkupContainer("actionIcon");
		actionIcon.add(new AttributeAppender("class", getActionBootstrapIconColorClass(itemModel), " "));
		actionIcon.add(new AttributeAppender("class", getActionBootstrapIconClass(itemModel), " "));
		actionLink.add(actionIcon);
		add(actionLink);
		
		MarkupContainer editLink = getEditLink("editLink", itemModel);
		editLink.add(new AttributeModifier("alt", getEditText(itemModel)));
		editLink.add(new AttributeModifier("title", getEditText(itemModel)));
		editLink.add(new AttributeAppender("class", getEditBootstrapColorClass(itemModel), " "));
		
		WebMarkupContainer editIcon = new WebMarkupContainer("editIcon");
		editIcon.add(new AttributeAppender("class", getEditBootstrapIconColorClass(itemModel), " "));
		editIcon.add(new AttributeAppender("class", getEditBootstrapIconClass(itemModel), " "));
		editLink.add(editIcon);
		add(editLink);
		
		MarkupContainer deleteLink = getDeleteLink("deleteLink", itemModel);
		deleteLink.add(new AttributeModifier("alt", getDeleteText(itemModel)));
		deleteLink.add(new AttributeModifier("title", getDeleteText(itemModel)));
		deleteLink.add(new AttributeAppender("class", getDeleteBootstrapColorClass(itemModel), " "));
		
		WebMarkupContainer deleteIcon = new WebMarkupContainer("deleteIcon");
		deleteIcon.add(new AttributeAppender("class", getDeleteBootstrapIconColorClass(itemModel), " "));
		deleteIcon.add(new AttributeAppender("class", getDeleteBootstrapIconClass(itemModel), " "));
		deleteLink.add(deleteIcon);
		add(deleteLink);
	}
	
	// Action link methods

	protected abstract IModel<String> getActionBootstrapIconClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getActionBootstrapIconColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getActionBootstrapColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getActionText(final IModel<? extends T> itemModel);

	protected abstract MarkupContainer getActionLink(final String id, final IModel<? extends T> itemModel);

	// Edit link methods
	
	protected abstract IModel<String> getEditBootstrapIconClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getEditBootstrapIconColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getEditBootstrapColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getEditText(final IModel<? extends T> itemModel);
	
	protected abstract MarkupContainer getEditLink(String id, final IModel<? extends T> itemModel);

	// Delete link methods
	
	protected abstract IModel<String> getDeleteBootstrapIconClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getDeleteBootstrapIconColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getDeleteBootstrapColorClass(final IModel<? extends T> itemModel);

	protected abstract IModel<String> getDeleteText(final IModel<? extends T> itemModel);
	
	protected abstract MarkupContainer getDeleteLink(String id, final IModel<? extends T> itemModel);

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
