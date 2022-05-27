package org.iglooproject.wicket.modal;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.IResourceStream;
import org.iglooproject.bootstrap.api.BootstrapModalBackdrop;
import org.iglooproject.bootstrap.api.BootstrapRequestCycle;
import org.iglooproject.bootstrap.api.IBootstrapModal;
import org.iglooproject.bootstrap.api.IModalPopupPanel;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public abstract class AbstractModalPopupPanel<O> extends GenericPanel<O> implements IModalPopupPanel, IMarkupResourceStreamProvider {

	private static final long serialVersionUID = -6919950872346297617L;

	private static final String HEADER_WICKET_ID = "header";

	private static final String BODY_WICKET_ID = "body";

	private static final String FOOTER_WICKET_ID = "footer";

	private final WebMarkupContainer container;

	private final WebMarkupContainer dialog;

	private IBootstrapModal bootstrapModal;

	public AbstractModalPopupPanel(String id, IModel<? extends O> model) {
		super(id, model);
		bootstrapModal = BootstrapRequestCycle.getSettings().modal();
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
	public IResourceStream getMarkupResourceStream(final MarkupContainer container, Class<?> containerClass) {
		final IResourceStreamLocator locator = Application.get().getResourceSettings().getResourceStreamLocator();
		if (AbstractModalPopupPanel.class.equals(containerClass)) {
			// normal call
			// markup is provided by current bootstrap registered version
			Class<?> bootstrapOverrideContainerClass = BootstrapRequestCycle.getSettings().modalMarkupClass();
			String path = bootstrapOverrideContainerClass.getName().replace('.', '/');
			IResourceStream resourceStream = locator.locate(bootstrapOverrideContainerClass, path, null, null, null, "html", false);
			return new MarkupResourceStream(resourceStream, new ContainerInfo(bootstrapOverrideContainerClass, container), bootstrapOverrideContainerClass);
		} else {
			// call originating from DelegatedMarkupPanel
			String path = containerClass.getName().replace('.', '/');
			IResourceStream resourceStream = locator.locate(containerClass, path, null, null, null, "html", false);
			return new MarkupResourceStream(resourceStream, new ContainerInfo(containerClass, container), containerClass);
		}
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
		
		BootstrapRequestCycle.getSettings().modalRenderHead(this, response);
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
		return getBootstrapModal().hide(getContainer());
	}

	protected void addCancelBehavior(AbstractLink link) {
		getBootstrapModal().addCancelBehavior(link);
	}

	public void setBootstrapModal(IBootstrapModal bootstrapModal) {
		this.bootstrapModal = bootstrapModal;
	}

	@Override
	public IBootstrapModal getBootstrapModal() {
		return this.bootstrapModal;
	}

	public AbstractModalPopupPanel<O> setStatic() {
		bootstrapModal.setKeyboard(false);
		bootstrapModal.setBackdrop(BootstrapModalBackdrop.STATIC);
		return this;
	}

}
