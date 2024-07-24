package igloo.bootstrap.modal;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.jquery.util.JQueryAbstractBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class ModalOpenOnClickBehavior extends JQueryAbstractBehavior {

  private static final long serialVersionUID = 8188257386595829052L;

  private final IModalPopupPanel modal;

  /**
   * @param modal - le composant qui contient la popup
   */
  public ModalOpenOnClickBehavior(IModalPopupPanel modal) {
    super();
    this.modal = modal;
  }

  /** Code appelé avant tout traitement de l'événement d'affichage. */
  public JsStatement onModalStart() {
    return null;
  }

  /** Code appelé au moment avant de demander l'affichage de la popup. */
  public JsStatement onModalComplete() {
    return null;
  }

  /** Code appelé au moment de l'affichage du popup. */
  public JsStatement onModalShow() {
    return null;
  }

  /** Code appelé quand le popup est caché. */
  public JsStatement onModalHide() {
    return null;
  }

  /** Rend le composant attaché invisible si la popup est invisible */
  @Override
  public void onConfigure(Component component) {
    super.onConfigure(component);
    modal.configure();
    component.setVisibilityAllowed(modal.determineVisibility());
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);
    BootstrapRequestCycle.getSettings()
        .openModalOnClickRenderHead(
            component,
            response,
            modal,
            onModalStart(),
            onModalComplete(),
            onModalShow(),
            onModalHide());
  }
}
