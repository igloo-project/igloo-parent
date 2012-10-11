package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.value.IValueMap;

public class ConfirmContentBehavior extends Behavior {

	private static final long serialVersionUID = -5357415707217228299L;

	private static final String ATTRIBUTE_TITLE = "data-modal-confirm-title";
	private static final String ATTRIBUTE_TEXT = "data-modal-confirm-text";
	private static final String ATTRIBUTE_YES_LABEL = "data-modal-confirm-yes-label";
	private static final String ATTRIBUTE_NO_LABEL = "data-modal-confirm-no-label";
	private static final String ATTRIBUTE_TEXT_NO_ESCAPE = "data-modal-confirm-text-noescape";

	private final IModel<String> titleModel;

	private final IModel<String> textModel;

	private final IModel<String> yesLabelModel;

	private final IModel<String> noLabelModel;

	private boolean textNoEscape;

	public ConfirmContentBehavior(IModel<String> titleModel, IModel<String> textModel, IModel<String> yesLabelModel,
			IModel<String> noLabelModel) {
		this(titleModel, textModel, yesLabelModel, noLabelModel, false);
	}

	public ConfirmContentBehavior(IModel<String> titleModel, IModel<String> textModel, IModel<String> yesLabelModel,
			IModel<String> noLabelModel, boolean textNoEscape) {
		super();
		this.titleModel = titleModel;
		this.textModel = textModel;
		this.yesLabelModel = yesLabelModel;
		this.noLabelModel = noLabelModel;
		this.textNoEscape = textNoEscape;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		IValueMap attributes = tag.getAttributes();
		addAttribute(component, attributes, ATTRIBUTE_TITLE, titleModel);
		addAttribute(component, attributes, ATTRIBUTE_TEXT, textModel);
		addAttribute(component, attributes, ATTRIBUTE_YES_LABEL, yesLabelModel);
		addAttribute(component, attributes, ATTRIBUTE_NO_LABEL, noLabelModel);
		
		if (textNoEscape) {
			attributes.put(ATTRIBUTE_TEXT_NO_ESCAPE, textNoEscape);
		}
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		
		if (titleModel != null) {
			titleModel.detach();
		}
		if (textModel != null) {
			textModel.detach();
		}
		if (yesLabelModel != null) {
			yesLabelModel.detach();
		}
		if (noLabelModel != null) {
			noLabelModel.detach();
		}
	}

	private void addAttribute(Component component, IValueMap attributes, String attributeName, IModel<String> model) {
		String label = getLabel(component, model);
		if (label != null) {
			attributes.put(attributeName, label);
		}
	}

	private String getLabel(Component component, IModel<String> labelModel) {
		if (labelModel != null) {
			IModel<String> model = labelModel;
			if (model instanceof IComponentAssignedModel) {
				model = ((IComponentAssignedModel<String>)model).wrapOnAssignment(component);
			}
			return model.getObject();
		} else {
			return null;
		}
	}

}
