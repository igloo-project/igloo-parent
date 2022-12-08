package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.more;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

import igloo.wicket.model.Models;

public class MoreBehavior extends Behavior {

	private static final long serialVersionUID = 6124848862459414294L;

	private static final String DEFAULT_LABEL_MESSAGE_KEY = "common.seeMore";

	private More more;

	private IModel<String> moreLabelModel;

	public MoreBehavior() {
		this(new More(), new ResourceModel(DEFAULT_LABEL_MESSAGE_KEY));
	}

	public MoreBehavior(More more, IModel<String> moreLabelModel) {
		super();
		
		this.more = more;
		this.moreLabelModel = moreLabelModel;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		
		IModel<String> model = Models.wrap(moreLabelModel, component);
		
		String moreLabel = model.getObject();
		if (moreLabel != null) {
			tag.put(More.ATTRIBUTE_LABEL, moreLabel);
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.render(JavaScriptHeaderItem.forReference(MoreResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(more).render()));
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		
		if (moreLabelModel != null) {
			moreLabelModel.detach();
		}
	}

}
