package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class ActionColumnConfirmActionFactory<T> extends AbstractActionColumnElementFactory<T, ActionColumnConfirmActionFactory<T>> {

	private static final long serialVersionUID = 1L;
	
	private final IOneParameterComponentFactory<AjaxConfirmLink<T>, IModel<T>> ajaxConfirmLinkFactory;
	
	public ActionColumnConfirmActionFactory(BootstrapRenderer<? super T> renderer, IOneParameterComponentFactory<AjaxConfirmLink<T>, IModel<T>> ajaxConfirmLinkFactory) {
		super(renderer);
		this.ajaxConfirmLinkFactory = ajaxConfirmLinkFactory;
	}
	
	@Override
	public AbstractLink create(String wicketId, final IModel<T> parameter) {
		return ajaxConfirmLinkFactory.create(wicketId, parameter);
	}
	
	@Override
	public ActionColumnConfirmActionFactory<T> thisAsF() {
		return this;
	}
	
	@Override
	public void detach() {
		Detachables.detach(ajaxConfirmLinkFactory);
	}

}
