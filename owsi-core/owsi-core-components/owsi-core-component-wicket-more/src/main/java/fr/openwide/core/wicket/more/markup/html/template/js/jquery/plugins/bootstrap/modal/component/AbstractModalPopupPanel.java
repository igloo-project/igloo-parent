package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

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
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.ModalOpenOnClickBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalManagerStatement;

public abstract class AbstractModalPopupPanel<O> extends GenericPanel<O> implements IModalPopupPanel {

	private static final long serialVersionUID = -6919950872346297617L;

	private static final String HEADER_WICKET_ID = "header";

	private static final String BODY_WICKET_ID = "body";

	private static final String FOOTER_WICKET_ID = "footer";

	private final WebMarkupContainer container;

	public AbstractModalPopupPanel(String id, IModel<? extends O> model) {
		super(id, model);
		setOutputMarkupId(true);
		
		// doit être présent dès le début pour le bon fonctionnement de prepareLink
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(new AttributeAppender("class", getCssClassNamesModel(), " "));
		add(container);
	}

	public void prepareLink(final Component link) {
		prepareLink(link, null);
	}

	/**
	 * @param link
	 * @param options - peut être null
	 */
	public void prepareLink(final Component link, BootstrapModal options) {
		link.add(new ModalOpenOnClickBehavior(getContainer(), options) {
			private static final long serialVersionUID = 7578810529771850911L;
			
			@Override
			public JsStatement onModalStart() {
				return AbstractModalPopupPanel.this.onModalStart();
			}
			
			@Override
			public JsStatement onModalComplete() {
				return AbstractModalPopupPanel.this.onModalComplete();
			}
		});
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		container.add(createHeader(HEADER_WICKET_ID));
		container.add(createBody(BODY_WICKET_ID));
		container.add(createFooter(FOOTER_WICKET_ID));
	}

	protected abstract Component createHeader(String wicketId);

	protected abstract Component createBody(String wicketId);

	protected abstract Component createFooter(String wicketId);

	protected final Component getHeader() {
		return container.get(HEADER_WICKET_ID);
	}

	protected final Component getBody() {
		return container.get(BODY_WICKET_ID);
	}

	protected final Component getFooter() {
		return container.get(FOOTER_WICKET_ID);
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

	/**
	 * A surcharger pour personnaliser la classe CSS de la popup.
	 */
	protected IModel<String> getCssClassNamesModel() {
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
	 * Permet de récupérer le code de fermeture de la popup.
	 */
	public JsStatement closeStatement() {
		return BootstrapModalManagerStatement.hide(getContainer());
	}

	protected void addCancelBehavior(AbstractLink link) {
		link.add(new AttributeModifier("data-dismiss", "modal"));
	}

}
