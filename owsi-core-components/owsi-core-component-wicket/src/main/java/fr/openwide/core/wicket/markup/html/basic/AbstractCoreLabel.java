package fr.openwide.core.wicket.markup.html.basic;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;
import org.springframework.util.StringUtils;

public abstract class AbstractCoreLabel<T extends AbstractCoreLabel<T>> extends Label {
	
	private static final long serialVersionUID = 1697388050602143288L;
	
	private IModel<?> mainModel;
	
	private boolean showPlaceholder = false;
	
	private IModel<String> placeholderModel;
	
	private boolean hideIfEmpty = false;
	
	private boolean multiline = false;
	
	public AbstractCoreLabel(String id, IModel<?> model) {
		super(id, model);
		this.mainModel = wrap(model);
	}
	
	public AbstractCoreLabel(String id, Serializable label) {
		super(id, label);
		this.mainModel = Model.of(label);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		boolean mainModelIsEmpty = !StringUtils.hasText(getDefaultModelObjectAsString(getMainModelObject()));
		
		if (!mainModelIsEmpty) {
			setDefaultModel(mainModel);
		} else if (showPlaceholder) {
			if (placeholderModel == null) {
				placeholderModel = new ResourceModel("common.emptyField");
			}
			setDefaultModel(placeholderModel);
		} else {
			setDefaultModel(null);
		}
		
		setVisible(!(hideIfEmpty && mainModelIsEmpty));
	}
	
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		CharSequence body = getDefaultModelObjectAsString();
		if (multiline) {
			body = Strings.toMultilineMarkup(body);
		}
		replaceComponentTagBody(markupStream, openTag, body);
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
	
	protected Object getMainModelObject() {
		if (mainModel != null) {
			return mainModel.getObject();
		}
		return null;
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if (mainModel != null) {
			mainModel.detach();
		}
		if (placeholderModel != null) {
			placeholderModel.detach();
		}
	}
}
