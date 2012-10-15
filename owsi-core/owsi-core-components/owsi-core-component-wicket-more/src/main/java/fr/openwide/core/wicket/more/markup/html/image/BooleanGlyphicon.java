package fr.openwide.core.wicket.more.markup.html.image;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Container utilisant les glyphicons de Bootstrap et affichant
 * un tick ou une croix selon que le model soit true ou false.
 */
public class BooleanGlyphicon extends WebMarkupContainer {

	private static final long serialVersionUID = -7046943814231028574L;

	private static final String CLASS_ATTRIBUTE = "class";
	private static final String CLASS_SEPARATOR = " ";

	private static final String BOOTSTRAP_TRUE_ICON_CLASS = "icon-ok";
	private static final String BOOTSTRAP_FALSE_ICON_CLASS = "icon-remove";

	private boolean showFalseIcon;
	
	public BooleanGlyphicon(String id, IModel<Boolean> booleanModel) {
		this(id, booleanModel, true);
	}
	
	public BooleanGlyphicon(String id, IModel<Boolean> booleanModel, boolean showFalseIcon) {
		super(id, booleanModel);
		this.showFalseIcon = showFalseIcon;
	}

	@Override
	public void onComponentTag(final ComponentTag tag) {
		Boolean value = getValue();
		
		if (value != null && value) {
			tag.append(CLASS_ATTRIBUTE, BOOTSTRAP_TRUE_ICON_CLASS, CLASS_SEPARATOR);
		} else if (showFalseIcon) {
			tag.append(CLASS_ATTRIBUTE, BOOTSTRAP_FALSE_ICON_CLASS, CLASS_SEPARATOR);
		}
		super.onComponentTag(tag);
	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && (getValue() != null);
	}

	private Boolean getValue() {
		return (Boolean) getDefaultModelObject();
	}
}
