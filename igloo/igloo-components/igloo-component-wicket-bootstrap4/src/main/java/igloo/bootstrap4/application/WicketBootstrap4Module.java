package igloo.bootstrap4.application;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.application.IWicketModule;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

import igloo.bootstrap.BootstrapVersion;
import igloo.bootstrap.IBootstrapProvider;
import igloo.bootstrap.badge.IBootstrapBadge;
import igloo.bootstrap.modal.BootstrapModalEvent;
import igloo.bootstrap.modal.IBootstrapModal;
import igloo.bootstrap.modal.IModalPopupPanel;
import igloo.bootstrap.popover.IBootstrapPopoverOptions;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.bootstrap.tooltip.BootstrapTooltipBehavior;
import igloo.bootstrap.tooltip.IBootstrapTooltipOptions;
import igloo.bootstrap4.markup.html.bootstrap.component.BootstrapBadge;
import igloo.bootstrap4.markup.html.template.css.bootstrap.CoreBootstrap4CssScope;
import igloo.bootstrap4.markup.html.template.js.bootstrap.confirm.BootstrapConfirmJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.modal.Bootstrap4ModalJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalMoreJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.modal.component.Bootstrap4ModalPanel;
import igloo.bootstrap4.markup.html.template.js.bootstrap.modal.statement.BootstrapModal;
import igloo.bootstrap4.markup.html.template.js.bootstrap.popover.BootstrapPopoverJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.tab.BootstrapTabJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.tab.BootstrapTabMoreJavaScriptResourceReference;
import igloo.bootstrap4.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipJavaScriptResourceReference;

public class WicketBootstrap4Module implements IWicketModule, IBootstrapProvider {

	@Override
	public void updateResourceBundles(ResourceBundles resourceBundles) {
		resourceBundles
			.addJavaScriptBundle(getClass(), "modal-bundle.js",
				Bootstrap4ModalJavaScriptResourceReference.get(),
				BootstrapModalMoreJavaScriptResourceReference.get()
			);
	}

	@Override
	public void updateResourceSettings(ResourceSettings resourceSettings) {
		// Nothing
	}

	@Override
	public void registerImportScopes(IScssService scssService) {
		scssService.registerImportScope("core-bs4", CoreBootstrap4CssScope.class);
	}

	@Override
	public BootstrapVersion getVersion() {
		return BootstrapVersion.BOOTSTRAP_4;
	}

	@Override
	public void openModalOnClickRenderHead(Component component, IHeaderResponse response,
			IModalPopupPanel modal, JsStatement onModalStart, JsStatement onModalComplete,
			JsStatement onModalShow, JsStatement onModalHide) {
		modalRenderHead(component, response);
		
		JsStatement bindClickStatement = getBindClickStatement(component, modal, onModalStart, onModalComplete);
		if (bindClickStatement != null) {
			response.render(OnDomReadyHeaderItem.forScript(bindClickStatement.render()));
			
			Event onShow = new Event(BootstrapModalEvent.SHOW) {
				private static final long serialVersionUID = -5947286377954553132L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope(onModalShow);
				}
			};
			Event onHide = new Event(BootstrapModalEvent.HIDE) {
				private static final long serialVersionUID = -5947286377954553132L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope(onModalHide);
				}
			};
			
			// enregistrement des événements onShow et onHide
			response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal.getContainer()).chain(onShow).render()));
			response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal.getContainer()).chain(onHide).render()));
		}
	}

	@Override
	public void modalRenderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(Bootstrap4ModalJavaScriptResourceReference.get()));
	}

	private JsStatement getBindClickStatement(Component component, IModalPopupPanel modal,
			JsStatement onModalStart, JsStatement onModalComplete) {
		if (!component.isEnabledInHierarchy()) {
			return null;
		}
		
		Event event = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1410592312776274815L;
			
			@Override
			public JsScope callback() {
				JsStatement jsStatement = new JsStatement();
				if (onModalStart != null) {
					jsStatement.append(onModalStart.render(true));
				}
				jsStatement.append(modal.getBootstrapModal().show(modal.getContainer()).render(true));
				if (onModalComplete != null) {
					jsStatement.append(onModalComplete.render(true));
				}
				return JsScope.quickScope(jsStatement);
			}
		};
		return new JsStatement().$(component).chain(event);
	}

	@Override
	public Class<?> modalMarkupClass() {
		return Bootstrap4ModalPanel.class;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		//nothing, js are loaded plugin by plugin
	}

	@Override
	public IBootstrapModal createModal() {
		return new BootstrapModal();
	}

	@Override
	public <T> SerializableSupplier2<IBootstrapBadge<T, BootstrapBadge<T>>> badgeSupplier(String id, IModel<T> model, final IBootstrapRenderer<? super T> renderer) {
		return () -> new BootstrapBadge<T>(id, model, renderer);
	}

	@Override
	public void confirmRenderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapConfirmJavaScriptResourceReference.get()));
	}

	@Override
	public JsStatement confirmStatement(Component component) {
		return new JsStatement().$(component).chain("confirm");
	}

	@Override
	public void tooltipRenderHead(Component component, IHeaderResponse response, IBootstrapTooltipOptions options) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTooltipJavaScriptResourceReference.get()));
		if (!StringUtils.hasText(options.getSelector())) {
			throw new IllegalStateException("Option 'selector' is mandatory for " + BootstrapTooltipBehavior.class.getName());
		}
		
		response.render(OnDomReadyHeaderItem.forScript("$(" + options.getSelector() + ").tooltip(" + options.getJavaScriptOptions() + ");"));
	}

	@Override
	public void tabRenderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabMoreJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain("tab").render(true)));
	}

	@Override
	public void popoverRenderHead(Component component, IHeaderResponse response, IBootstrapPopoverOptions options) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapPopoverJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain("popover", options.getJavaScriptOptions()).render()));
	}

}
