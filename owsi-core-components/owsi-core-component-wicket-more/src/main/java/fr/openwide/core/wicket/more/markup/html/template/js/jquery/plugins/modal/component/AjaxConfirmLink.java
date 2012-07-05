package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.CancelEventIfNoAjaxDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc.ConfirmAjaxCallDecorator;

public abstract class AjaxConfirmLink<O> extends AjaxLink<O> {

	private static final long serialVersionUID = -645345859108195615L;

	private static final IAjaxCallDecorator CONFIRM_DECORATOR = new ConfirmAjaxCallDecorator();

	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel));
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return CONFIRM_DECORATOR;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderJavaScriptReference(ModalJavaScriptResourceReference.get());
	}

	/**
	 * Surchargé pour supprimer le {@link CancelEventIfNoAjaxDecorator} (car dans notre cas, wcall n'est pas défini).
	 * (comme de toute façon, l'action ajax n'est pas déclenchée au moment du clic, il est inutile de conditionner
	 * l'annulation de l'événément originel).
	 */
	@Override
	protected AjaxEventBehavior newAjaxEventBehavior(String event) {
		return new AjaxEventBehavior(event) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onClick(target);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return AjaxConfirmLink.this.getAjaxCallDecorator();
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				// add the onclick handler only if link is enabled
				if (isLinkEnabled())
				{
					super.onComponentTag(tag);
				}
			}

			@Override
			protected AjaxChannel getChannel() {
				return AjaxConfirmLink.this.getChannel();
			}
		};
	}

}
