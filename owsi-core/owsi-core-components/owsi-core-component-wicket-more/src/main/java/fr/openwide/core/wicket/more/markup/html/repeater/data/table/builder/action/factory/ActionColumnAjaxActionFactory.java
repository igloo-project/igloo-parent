package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IAjaxOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class ActionColumnAjaxActionFactory<T> extends AbstractActionColumnElementFactory<T, ActionColumnAjaxActionFactory<T>> {

	private static final long serialVersionUID = 1L;
	
	private final IAjaxOneParameterAjaxAction<IModel<T>> action;
	
	public ActionColumnAjaxActionFactory(BootstrapLabelRenderer<? super T> renderer, IAjaxOneParameterAjaxAction<IModel<T>> action) {
		super(renderer);
		this.action = action;
	}
	
	@Override
	public AbstractLink create(String wicketId, final IModel<T> parameter) {
		return new AjaxLink<T>(wicketId, parameter) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				action.updateAjaxAttributes(attributes, parameter);
			}
			@Override
			public void onClick(AjaxRequestTarget target) {
				action.execute(target, parameter);
			}
		};
	}
	
	@Override
	public ActionColumnAjaxActionFactory<T> thisAsF() {
		return this;
	}
	
	@Override
	public void detach() {
		Detachables.detach(action);
	}

}
