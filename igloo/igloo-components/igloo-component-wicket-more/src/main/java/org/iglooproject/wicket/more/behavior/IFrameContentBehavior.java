package org.iglooproject.wicket.more.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.IRequestListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;

import igloo.wicket.model.Detachables;

public class IFrameContentBehavior extends Behavior implements IRequestListener {

	private static final long serialVersionUID = 6273000150398527619L;

	private final IModel<String> contentModel;

	public IFrameContentBehavior(IModel<String> contentModel) {
		this.contentModel = contentModel;
	}

	@Override
	public void onRequest() {
		RequestCycle.get().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("text/html", "UTF-8", contentModel.getObject()));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(contentModel);
	}

}
