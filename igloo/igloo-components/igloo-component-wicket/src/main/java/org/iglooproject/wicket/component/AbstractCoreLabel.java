package org.iglooproject.wicket.component;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;

public abstract class AbstractCoreLabel<T extends AbstractCoreLabel<T>> extends Label {
	
	private static final long serialVersionUID = 1697388050602143288L;
	
	protected boolean showPlaceholder = false;
	
	protected IModel<String> placeholderModel;
	
	protected boolean hideIfEmpty = false;
	
	protected boolean multiline = false;
	
	protected transient IModel<?> currentModel;
	
	public AbstractCoreLabel(String id, IModel<?> model) {
		super(id, model);
	}
	
	public AbstractCoreLabel(String id, Serializable label) {
		super(id, label);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		boolean defaultModelIsEmpty = Strings.isEmpty(getDefaultModelObjectAsString());
		
		if (!defaultModelIsEmpty) {
			currentModel = getDefaultModel();
		} else if (showPlaceholder) {
			if (placeholderModel == null) {
				placeholderModel = new ResourceModel("common.field.empty");
			}
			currentModel = placeholderModel;
		} else {
			currentModel = null;
		}
		
		setVisible(!(hideIfEmpty && defaultModelIsEmpty));
	}
	
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		CharSequence body = getDefaultModelObjectAsString(getCurrentModelObject());
		if (multiline) {
			body = Strings.toMultilineMarkup(body);
		}
		replaceComponentTagBody(markupStream, openTag, body);
	}
	
	protected Object getCurrentModelObject() {
		if (currentModel == null) {
			return null;
		}
		return currentModel.getObject();
	}
	
	/**
	 * @return this as an object of type T
	 */
	protected abstract T thisAsT();
	
	public boolean isShowPlaceholder() {
		return showPlaceholder;
	}
	
	public T showPlaceholder() {
		this.showPlaceholder = true;
		return thisAsT();
	}
	
	public T showPlaceholder(IModel<String> placeholderModel) {
		return showPlaceholder().placeholderModel(placeholderModel);
	}
	
	public IModel<String> getPlaceholderModel() {
		return placeholderModel;
	}
	
	public T placeholderModel(IModel<String> placeholderModel) {
		this.placeholderModel = placeholderModel;
		return thisAsT();
	}
	
	public boolean isHideIfEmpty() {
		return hideIfEmpty;
	}
	
	public T hideIfEmpty() {
		this.hideIfEmpty = true;
		return thisAsT();
	}
	
	public boolean isMultiline() {
		return multiline;
	}
	
	public T multiline() {
		this.multiline = true;
		return thisAsT();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		
		currentModel = null;
		
		if (placeholderModel != null) {
			placeholderModel.detach();
		}
	}
}
