package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.ModalOpenOnClickBehavior;

public abstract class AbstractModalPopupPanel<O> extends GenericPanel<O> implements IModalPopupPanel {

	private static final long serialVersionUID = -6919950872346297617L;

	private static final String HEADER_WICKET_ID = "header";

	private static final String BODY_WICKET_ID = "body";

	private static final String FOOTER_WICKET_ID = "footer";

	private WebMarkupContainer container;

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
		link.add(new ModalOpenOnClickBehavior(getContainer()));
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

	public abstract IModel<String> getCssClassNamesModel();

}
