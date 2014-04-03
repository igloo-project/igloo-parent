package fr.openwide.core.wicket.more.markup.html.image;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;

/**
 * Container utilisant les glyphicons de Bootstrap et affichant
 * une icône selon que le model soit true ou false.
 * Par défaut un tick ou une croix avec possibilité de personnaliser.
 */
public class BooleanGlyphicon extends WebMarkupContainer {

	private static final long serialVersionUID = -7046943814231028574L;

	private static final String CLASS_ATTRIBUTE = "class";
	private static final String CLASS_SEPARATOR = " ";

	private static final String BOOTSTRAP_TRUE_ICON_CLASS = "icon-ok";
	private static final String BOOTSTRAP_FALSE_ICON_CLASS = "icon-remove";
	
	public static final IModel<String> BOOTSTRAP_TRUE_ICON_CLASS_MODEL = new Model<String>(BOOTSTRAP_TRUE_ICON_CLASS);
	public static final IModel<String> BOOTSTRAP_FALSE_ICON_CLASS_MODEL = new Model<String>(BOOTSTRAP_FALSE_ICON_CLASS);
	
	private static final String BOOLEAN_GLYPHICON_CLASS = "boolean-glyphicon";

	private boolean showFalseIcon;
	
	private IModel<String> trueIconClassModel;
	
	private IModel<String> falseIconClassModel;
	
	public BooleanGlyphicon(String id, IModel<Boolean> booleanModel) {
		this(id, booleanModel, true);
	}
	
	public BooleanGlyphicon(String id, IModel<Boolean> booleanModel, boolean showFalseIcon) {
		this(id, booleanModel, showFalseIcon, BOOTSTRAP_TRUE_ICON_CLASS_MODEL, BOOTSTRAP_FALSE_ICON_CLASS_MODEL);
	}
	
	public BooleanGlyphicon(String id, IModel<Boolean> booleanModel, boolean showFalseIcon,
			IModel<String> trueIconClassModel, IModel<String> falseIconClassModel) {
		super(id, booleanModel);
		this.showFalseIcon = showFalseIcon;
		this.trueIconClassModel = trueIconClassModel;
		this.falseIconClassModel = falseIconClassModel;
		
		add(new ClassAttributeAppender(BOOLEAN_GLYPHICON_CLASS));
	}

	@Override
	public void onComponentTag(final ComponentTag tag) {
		Boolean value = getValue();
		
		if (value != null && value) {
			tag.append(CLASS_ATTRIBUTE, trueIconClassModel != null ? trueIconClassModel.getObject() : null, CLASS_SEPARATOR);
		} else if (showFalseIcon) {
			tag.append(CLASS_ATTRIBUTE, falseIconClassModel != null ? falseIconClassModel.getObject() : null, CLASS_SEPARATOR);
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
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if (trueIconClassModel != null) {
			trueIconClassModel.detach();
		}
		if (falseIconClassModel != null) {
			falseIconClassModel.detach();
		}
	}
}
