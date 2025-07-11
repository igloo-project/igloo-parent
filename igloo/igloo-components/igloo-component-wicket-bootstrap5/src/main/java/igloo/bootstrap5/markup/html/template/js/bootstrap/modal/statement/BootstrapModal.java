package igloo.bootstrap5.markup.html.template.js.bootstrap.modal.statement;

import igloo.bootstrap.modal.BootstrapModalBackdrop;
import igloo.bootstrap.modal.IBootstrapModal;
import java.io.Serializable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

public final class BootstrapModal implements ChainableStatement, Serializable, IBootstrapModal {

  private static final long serialVersionUID = 7943137804361253044L;

  public static final String BOOTSTRAP_MODAL_CHAIN_LABEL = "modal";

  private static final String OPTION_BACKDROP = "backdrop";
  private static final String OPTION_FOCUS = "focus";
  private static final String OPTION_KEYBOARD = "keyboard";

  /** Contrôle l'affichage du fond et la possibilité de fermer la modale par clic sur le fond */
  private BootstrapModalBackdrop backdrop;

  /** Si true, active la sortie de la popup par la touche escape (défaut) */
  private Boolean keyboard;

  /** Si false, désactive le focusTrap */
  private Boolean focus;

  public BootstrapModal() {
    super();
  }

  @Override
  public String chainLabel() {
    return BOOTSTRAP_MODAL_CHAIN_LABEL;
  }

  @Override
  public CharSequence[] statementArgs() {
    Options options = getOptions();

    CharSequence[] args;
    args = new CharSequence[1];
    args[0] = options.getJavaScriptOptions();
    return args;
  }

  private Options getOptions() {
    Options options = new Options();
    if (backdrop != null) {
      options.put(OPTION_BACKDROP, backdrop);
    }
    if (keyboard != null) {
      options.put(OPTION_KEYBOARD, keyboard);
    }
    if (focus != null) {
      options.put(OPTION_FOCUS, focus);
    }
    return options;
  }

  @Override
  public void addCancelBehavior(Component component) {
    component.add(new AttributeModifier("data-bs-dismiss", "modal"));
  }

  @Override
  public JsStatement show(Component modal) {
    return modalEvent(modal, "show");
  }

  @Override
  public JsStatement hide(Component modal) {
    return modalEvent(modal, "hide");
  }

  private JsStatement modalEvent(Component modal, String event) {
    return new JsStatement()
        .append(
            "bootstrap.Modal.getOrCreateInstance(document.getElementById("
                + JsUtils.quotes(modal.getMarkupId())
                + "))")
        .chain(event);
  }

  @Override
  public JsStatement modal(Component modal) {
    return new JsStatement()
        .append(
            "new bootstrap.Modal(document.getElementById("
                + JsUtils.quotes(modal.getMarkupId())
                + "), "
                + getOptions().getJavaScriptOptions()
                + ")");
  }

  public BootstrapModalBackdrop getBackdrop() {
    return backdrop;
  }

  @Override
  public BootstrapModal setBackdrop(BootstrapModalBackdrop backdrop) {
    this.backdrop = backdrop;
    return this;
  }

  public Boolean getFocus() {
    return focus;
  }

  @Override
  public BootstrapModal setFocus(Boolean focus) {
    this.focus = focus;
    return this;
  }

  public Boolean getKeyboard() {
    return keyboard;
  }

  @Override
  public BootstrapModal setKeyboard(Boolean keyboard) {
    this.keyboard = keyboard;
    return this;
  }
}
