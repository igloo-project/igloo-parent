package igloo.bootstrap.confirm;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AjaxConfirmLink<O> extends AjaxLink<O> {

  private static final long serialVersionUID = -645345859108195615L;

  public static <O> IConfirmLinkBuilderStepStart<AjaxConfirmLink<O>, O> build() {
    return new AjaxConfirmLinkBuilder<>();
  }

  private final Form<?> form;

  protected AjaxConfirmLink(
      String id,
      IModel<O> model,
      IModel<String> titleModel,
      IModel<String> textModel,
      IModel<String> yesLabelModel,
      IModel<String> noLabelModel,
      IModel<String> cssClassNamesModel,
      boolean textNoEscape) {
    this(
        id,
        model,
        null,
        titleModel,
        textModel,
        yesLabelModel,
        noLabelModel,
        new Model<String>("fa fa-fw fa-check"),
        new Model<String>(),
        new Model<String>("btn btn-success"),
        new Model<String>("btn-outline-secondary"),
        cssClassNamesModel,
        textNoEscape);
  }

  protected AjaxConfirmLink(
      String id,
      IModel<O> model,
      Form<?> form,
      IModel<String> titleModel,
      IModel<String> textModel,
      IModel<String> yesLabelModel,
      IModel<String> noLabelModel,
      IModel<String> yesIconModel,
      IModel<String> noIconModel,
      IModel<String> yesButtonModel,
      IModel<String> noButtonModel,
      IModel<String> cssClassNamesModel,
      boolean textNoEscape) {
    super(id, model);
    setOutputMarkupId(true);
    this.form = form;
    add(
        new ConfirmContentBehavior(
            titleModel,
            textModel,
            yesLabelModel,
            noLabelModel,
            yesIconModel,
            noIconModel,
            yesButtonModel,
            noButtonModel,
            cssClassNamesModel,
            textNoEscape));
  }

  /**
   * Cette méthode fournit normalement le handler pour l'événement onclick. On le remplace par
   * l'événement de confirmation (le onclick est géré sans ajax au-dessus).
   */
  @Override
  protected AjaxEventBehavior newAjaxEventBehavior(String event) {
    if (form != null) {
      return new AjaxFormSubmitBehavior(form, BootstrapConfirmEvent.CONFIRM.getEventLabel()) {
        private static final long serialVersionUID = 4405251450215656630L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
          AjaxConfirmLink.this.onClick(target);
        }

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
          super.updateAjaxAttributes(attributes);
          AjaxConfirmLink.this.updateAjaxAttributes(attributes);
        }
      };
    } else {
      // Lorsque l'évènement 'confirm' est détecté, on déclenche l'action à proprement parler.
      return new AjaxEventBehavior(BootstrapConfirmEvent.CONFIRM.getEventLabel()) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isEnabled(Component component) {
          // On ajoute le handler seulement si le lien est activé
          return AjaxConfirmLink.this.isEnabledInHierarchy();
        }

        @Override
        protected void onEvent(AjaxRequestTarget target) {
          AjaxConfirmLink.this.onClick(target);
        }

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
          super.updateAjaxAttributes(attributes);
          AjaxConfirmLink.this.updateAjaxAttributes(attributes);
        }
      };
    }
  }
}
