package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc.ConfirmAjaxCallListener;

public abstract class AjaxConfirmLink<O> extends AjaxLink<O> {

	private static final long serialVersionUID = -645345859108195615L;

	private static final IAjaxCallListener CONFIRM_LISTENER = new ConfirmAjaxCallListener();

	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		this(id, model, titleModel, textModel, yesLabelModel, noLabelModel, false);
	}

	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, boolean textNoEscape) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, textNoEscape));
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getAjaxCallListeners().add(CONFIRM_LISTENER);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
	}

	/**
	 * Surchargé pour supprimer le {@link CancelEventIfNoAjaxDecorator} (car dans notre cas, wcall n'est pas défini).
	 * (comme de toute façon, l'action ajax n'est pas déclenchée au moment du clic, il est inutile de conditionner
	 * l'annulation de l'événément originel).
	 */
	// TODO migration Wicket 6 : voir avec LAL ce qu'on fait de ça
//	@Override
//	protected AjaxEventBehavior newAjaxEventBehavior(String event) {
//		return new AjaxEventBehavior(event) {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onEvent(AjaxRequestTarget target) {
//				onClick(target);
//			}
//
//			@Override
//			protected IAjaxCallDecorator getAjaxCallDecorator() {
//				return AjaxConfirmLink.this.getAjaxCallDecorator();
//			}
//
//			@Override
//			protected void onComponentTag(ComponentTag tag) {
//				// add the onclick handler only if link is enabled
//				if (isLinkEnabled())
//				{
//					super.onComponentTag(tag);
//				}
//			}
//
//			@Override
//			protected AjaxChannel getChannel() {
//				return AjaxConfirmLink.this.getChannel();
//			}
//		};
//	}

}
