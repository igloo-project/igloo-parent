package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.bootstrap.api.BootstrapModalBackdrop;
import org.iglooproject.bootstrap.api.IBootstrapModal;
import org.iglooproject.bootstrap.api.IModalPopupPanel;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.behavior.ModalOpenOnClickBehavior;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.statement.BootstrapModal;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.statement.BootstrapModalStatement;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public abstract class AbstractModalPopupPanel<O> extends GenericPanel<O> implements IModalPopupPanel {

	private static final long serialVersionUID = -6919950872346297617L;

	private static final String HEADER_WICKET_ID = "header";

	private static final String BODY_WICKET_ID = "body";

	private static final String FOOTER_WICKET_ID = "footer";

	private final WebMarkupContainer container;

	private final WebMarkupContainer dialog;

	private IBootstrapModal bootstrapModal;

	public AbstractModalPopupPanel(String id, IModel<? extends O> model) {
		super(id, model);
		setOutputMarkupId(true);
		
		// doit être présent dès le début pour le bon fonctionnement de prepareLink
		container = new WebMarkupContainer("container");
		dialog = new WebMarkupContainer("dialog");
		
		add(
				container
						.add(
								dialog
										.add(new ClassAttributeAppender(getModalDialogCssClassModel()))
						)
						.add(new ClassAttributeAppender(getModalCssClassModel()))
						.add(new AttributeAppender("tabindex", Model.of("-1")))
						.setOutputMarkupId(true)
		);
	}

	/**
	 * @param link
	 * @param options - peut être null
	 */
	public void prepareLink(final Component link) {
		link.add(new ModalOpenOnClickBehavior(this) {
			private static final long serialVersionUID = 7578810529771850911L;
			
			@Override
			public JsStatement onModalStart() {
				return AbstractModalPopupPanel.this.onModalStart();
			}
			
			@Override
			public JsStatement onModalComplete() {
				return AbstractModalPopupPanel.this.onModalComplete();
			}
			
			@Override
			public JsStatement onModalShow() {
				return AbstractModalPopupPanel.this.onModalShow();
			}
			
			@Override
			public JsStatement onModalHide() {
				return AbstractModalPopupPanel.this.onModalHide();
			}
		});
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		dialog.add(createHeader(HEADER_WICKET_ID));
		dialog.add(createBody(BODY_WICKET_ID));
		dialog.add(createFooter(FOOTER_WICKET_ID));
	}

	protected abstract Component createHeader(String wicketId);

	protected abstract Component createBody(String wicketId);

	protected abstract Component createFooter(String wicketId);

	protected final Component getHeader() {
		return dialog.get(HEADER_WICKET_ID);
	}

	protected final Component getBody() {
		return dialog.get(BODY_WICKET_ID);
	}

	protected final Component getFooter() {
		return dialog.get(FOOTER_WICKET_ID);
	}

	@Override
	public Markup getAssociatedMarkup() {
		return MarkupFactory.get().getMarkup(this, AbstractModalPopupPanel.class, false);
	}
	
	@Override
	public String getContainerMarkupId() {
		return container.getMarkupId();
	}

	@Override
	public WebMarkupContainer getContainer() {
		return container;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
	}

	public IModel<String> getModalCssClassModel() {
		return Model.of();
	}

	public IModel<String> getModalDialogCssClassModel() {
		return Model.of();
	}

	/**
	 * A surcharger au besoin ; code appelé avant tout traitement
	 */
	protected JsStatement onModalStart() {
		return null;
	}

	/**
	 * A surcharger au besoin ; code appelé à l'affichage de la popup
	 */
	protected JsStatement onModalComplete() {
		return null;
	}

	/**
	 * Code appelé au moment de l'affichage du popup.
	 */
	public JsStatement onModalShow() {
		return null;
	}

	/**
	 * Code appelé quand le popup est caché.
	 */
	public JsStatement onModalHide() {
		return null;
	}

	/**
	 * Permet de récupérer le code de fermeture de la popup.
	 */
	public JsStatement closeStatement() {
		return BootstrapModalStatement.hide(getContainer());
	}

	protected void addCancelBehavior(AbstractLink link) {
		link.add(new AttributeModifier("data-dismiss", "modal"));
	}

	public void setBootstrapModal(IBootstrapModal bootstrapModal) {
		this.bootstrapModal = bootstrapModal;
	}

	@Override
	public IBootstrapModal getBootstrapModal() {
		return this.bootstrapModal;
	}

	public AbstractModalPopupPanel<O> setStatic() {
		if (bootstrapModal == null) {
			bootstrapModal = BootstrapModal.modal();
		}
		bootstrapModal.setKeyboard(false);
		bootstrapModal.setBackdrop(BootstrapModalBackdrop.STATIC);
		return this;
	}

}
