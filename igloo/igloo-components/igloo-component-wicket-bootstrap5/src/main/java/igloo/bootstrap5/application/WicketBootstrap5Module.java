package igloo.bootstrap5.application;

import igloo.bootstrap.BootstrapVersion;
import igloo.bootstrap.IBootstrapProvider;
import igloo.bootstrap.badge.IBootstrapBadge;
import igloo.bootstrap.modal.BootstrapModalEvent;
import igloo.bootstrap.modal.IBootstrapModal;
import igloo.bootstrap.modal.IModalPopupPanel;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.bootstrap.tooltip.BootstrapTooltipBehavior;
import igloo.bootstrap.tooltip.IBootstrapTooltipOptions;
import igloo.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import igloo.bootstrap5.markup.html.template.css.bootstrap.CoreBootstrap5CssScope;
import igloo.bootstrap5.markup.html.template.js.bootstrap.Bootstrap5OverrideJavaScriptResourceReference;
import igloo.bootstrap5.markup.html.template.js.bootstrap.modal.component.Bootstrap5ModalPanel;
import igloo.bootstrap5.markup.html.template.js.bootstrap.modal.statement.BootstrapModal;
import java.util.function.Supplier;
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
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public class WicketBootstrap5Module implements IWicketModule, IBootstrapProvider {

  @Override
  public void updateResourceBundles(ResourceBundles resourceBundles) {
    // nothing to do
  }

  @Override
  public void registerImportScopes(IScssService scssService) {
    scssService.registerImportScope("core-bs5", CoreBootstrap5CssScope.class);
  }

  @Override
  public void updateResourceSettings(ResourceSettings resourceSettings) {
    // nothing to do
  }

  @Override
  public BootstrapVersion getVersion() {
    return BootstrapVersion.BOOTSTRAP_5;
  }

  @Override
  public void openModalOnClickRenderHead(
      Component component,
      IHeaderResponse response,
      IModalPopupPanel modal,
      JsStatement onModalStart,
      JsStatement onModalComplete,
      JsStatement onModalShow,
      JsStatement onModalHide) {
    modalRenderHead(component, response);

    JsStatement bindClickStatement =
        getBindClickStatement(component, modal, onModalStart, onModalComplete);
    if (bindClickStatement != null) {
      response.render(OnDomReadyHeaderItem.forScript(bindClickStatement.render()));

      Event onShow =
          new Event(BootstrapModalEvent.SHOW) {
            private static final long serialVersionUID = -5947286377954553132L;

            @Override
            public JsScope callback() {
              return JsScopeEvent.quickScope(onModalShow);
            }
          };
      Event onHide =
          new Event(BootstrapModalEvent.HIDE) {
            private static final long serialVersionUID = -5947286377954553132L;

            @Override
            public JsScope callback() {
              return JsScopeEvent.quickScope(onModalHide);
            }
          };

      // enregistrement des événements onShow et onHide
      // TODO: are these events still bindable ?
      response.render(
          OnDomReadyHeaderItem.forScript(
              new JsStatement().$(modal.getContainer()).chain(onShow).render()));
      response.render(
          OnDomReadyHeaderItem.forScript(
              new JsStatement().$(modal.getContainer()).chain(onHide).render()));
    }
  }

  @Override
  public void modalRenderHead(Component component, IHeaderResponse response) {
    renderHead(component, response);
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
  }

  @Override
  public void confirmRenderHead(Component component, IHeaderResponse response) {
    renderHead(component, response);
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
    response.render(OnDomReadyHeaderItem.forScript(confirmStatement(component).render(true)));
  }

  @Override
  public JsStatement confirmStatement(Component component) {
    return new JsStatement()
        .append(
            "new Confirm(document.getElementById(" + JsUtils.quotes(component.getMarkupId()) + "))")
        .append(";");
  }

  private JsStatement getBindClickStatement(
      Component component,
      IModalPopupPanel modal,
      JsStatement onModalStart,
      JsStatement onModalComplete) {
    if (!component.isEnabledInHierarchy()) {
      return null;
    }

    Event event =
        new Event(MouseEvent.CLICK) {
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
    return Bootstrap5ModalPanel.class;
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
  }

  @Override
  public IBootstrapModal createModal() {
    return new BootstrapModal();
  }

  @Override
  public <T> SerializableSupplier2<IBootstrapBadge<T, BootstrapBadge<T>>> badgeSupplier(
      String id, IModel<T> model, final IBootstrapRenderer<? super T> renderer) {
    return () -> new BootstrapBadge<T>(id, model, renderer);
  }

  @Override
  public void tooltipRenderHead(
      Component component, IHeaderResponse response, IBootstrapTooltipOptions options) {
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
    if (!StringUtils.hasText(options.getSelector())) {
      throw new IllegalStateException(
          "Option 'selector' is mandatory for " + BootstrapTooltipBehavior.class.getName());
    }

    response.render(
        OnDomReadyHeaderItem.forScript(
            "new bootstrap.Tooltip(document.body, " + options.getJavaScriptOptions() + ");"));
  }

  @Override
  public void tabRenderHead(Component component, IHeaderResponse response) {
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
    response.render(
        OnDomReadyHeaderItem.forScript("new bootstrap.Tab('#" + component.getMarkupId() + "');"));
  }

  @Override
  public void popoverRenderHead(
      Component component, IHeaderResponse response, Supplier<CharSequence> options) {
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
    response.render(
        OnDomReadyHeaderItem.forScript(
            "new bootstrap.Popover(document.getElementById('"
                + component.getMarkupId()
                + "'), "
                + options.get()
                + ");"));
  }
}
