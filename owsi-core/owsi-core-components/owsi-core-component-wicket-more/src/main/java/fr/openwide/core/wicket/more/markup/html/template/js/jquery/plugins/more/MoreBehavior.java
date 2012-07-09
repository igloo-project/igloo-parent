package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.more;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class MoreBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = 6124848862459414294L;

	private IMore more;

	private IModel<String> moreLabelModel;

	public MoreBehavior(IMore more, IModel<String> moreLabelModel) {
		super();
		
		this.more = more;
		this.moreLabelModel = moreLabelModel;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		
		IModel<String> model = moreLabelModel;
		if (model instanceof IComponentAssignedModel) {
			model = ((IComponentAssignedModel<String>)model).wrapOnAssignment(component);
		}
		
		String moreLabel = model.getObject();
		if (moreLabel != null) {
			tag.put(More.ATTRIBUTE_LABEL, moreLabel);
		}
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(more);
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		
		if (moreLabelModel != null) {
			moreLabelModel.detach();
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.renderJavaScriptReference(MoreResourceReference.get());
	}

}
