package igloo.bootstrap.confirm;

import igloo.wicket.action.Actions;
import igloo.wicket.action.AjaxActions;
import igloo.wicket.action.IAction;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.ModelFactories;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

public abstract class AbstractConfirmLinkBuilder<L extends AbstractLink, O>
    implements IConfirmLinkBuilderStepStart<L, O>,
        IConfirmLinkBuilderStepContent<L, O>,
        IConfirmLinkBuilderStepEndContent<L, O>,
        IConfirmLinkBuilderStepNo<L, O>,
        IConfirmLinkBuilderStepOnclick<L, O>,
        IConfirmLinkBuilderStepOneParameterTerminal<L, O>,
        IConfirmLinkBuilderStepTerminal<L, O> {

  private static final long serialVersionUID = 365949870142796149L;

  protected IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory;

  protected IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory;

  protected IModel<String> yesLabelModel;

  protected IModel<String> noLabelModel;

  protected IModel<String> yesIconModel;

  protected IModel<String> noIconModel;

  protected IModel<String> yesButtonModel;

  protected IModel<String> noButtonModel;

  protected IModel<String> cssClassNamesModel;

  protected Form<?> form;

  protected boolean keepMarkup = false;

  protected IOneParameterAjaxAction<? super IModel<O>> onAjaxClick;

  protected IOneParameterAction<? super IModel<O>> onClick;

  protected AbstractConfirmLinkBuilder() {}

  @Override
  public IConfirmLinkBuilderStepContent<L, O> title(IModel<String> titleModel) {
    return title(ModelFactories.constant(titleModel));
  }

  @Override
  public IConfirmLinkBuilderStepContent<L, O> title(
      IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory) {
    this.titleModelFactory = titleModelFactory;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepEndContent<L, O> content(IModel<String> contentModel) {
    return content(ModelFactories.constant(contentModel));
  }

  @Override
  public IConfirmLinkBuilderStepEndContent<L, O> content(
      IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory) {
    this.contentModelFactory = contentModelFactory;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepEndContent<L, O> cssClassNamesModel(
      IModel<String> cssClassNamesModel) {
    this.cssClassNamesModel = cssClassNamesModel;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> deleteConfirmation() {
    confirm();
    title(new ResourceModel("common.action.confirm.title"));
    content(
        parameter -> {
          if (parameter != null) {
            return new StringResourceModel(
                "common.action.delete.confirm.content.object", parameter);
          } else {
            return new ResourceModel("common.action.delete.confirm.content");
          }
        });
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepEndContent<L, O> keepMarkup() {
    this.keepMarkup = true;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepNo<L, O> yes(IModel<String> yesLabelModel) {
    this.yesLabelModel = yesLabelModel;
    this.yesIconModel = new Model<>("fa fa-fw fa-check");
    this.yesButtonModel = new Model<>("btn btn-success");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepNo<L, O> yes(
      IModel<String> yesLabelModel, IModel<String> yesIconModel) {
    this.yesLabelModel = yesLabelModel;
    this.yesIconModel = yesIconModel;
    this.yesButtonModel = new Model<>("btn btn-success");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepNo<L, O> yes(
      IModel<String> yesLabelModel, IModel<String> yesIconModel, IModel<String> yesButtonModel) {
    this.yesLabelModel = yesLabelModel;
    this.yesIconModel = yesIconModel;
    this.yesButtonModel = yesButtonModel;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> no(IModel<String> noLabelModel) {
    this.noLabelModel = noLabelModel;
    this.noIconModel = new Model<>();
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> no(
      IModel<String> noLabelModel, IModel<String> noIconModel) {
    this.noLabelModel = noLabelModel;
    this.noIconModel = noIconModel;
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> no(
      IModel<String> noLabelModel, IModel<String> noIconModel, IModel<String> noButtonModel) {
    this.noLabelModel = noLabelModel;
    this.noIconModel = noIconModel;
    this.noButtonModel = noButtonModel;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> yesNo() {
    this.yesLabelModel = new ResourceModel("common.yes");
    this.noLabelModel = new ResourceModel("common.no");
    this.yesIconModel = new Model<>("fa fa-fw fa-check");
    this.noIconModel = new Model<>();
    this.yesButtonModel = new Model<>("btn btn-success");
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> confirm() {
    this.yesLabelModel = new ResourceModel("common.action.confirm");
    this.noLabelModel = new ResourceModel("common.action.cancel");
    this.yesIconModel = new Model<>("fa fa-fw fa-check");
    this.noIconModel = new Model<>();
    this.yesButtonModel = new Model<>("btn btn-success");
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> validate() {
    this.yesLabelModel = new ResourceModel("common.action.validate");
    this.noLabelModel = new ResourceModel("common.action.cancel");
    this.yesIconModel = new Model<>("fa fa-fw fa-check");
    this.noIconModel = new Model<>();
    this.yesButtonModel = new Model<>("btn btn-success");
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOnclick<L, O> save() {
    this.yesLabelModel = new ResourceModel("common.action.save");
    this.noLabelModel = new ResourceModel("common.action.cancel");
    this.yesIconModel = new Model<>("fa fa-fw fa-check");
    this.noIconModel = new Model<>();
    this.yesButtonModel = new Model<>("btn btn-success");
    this.noButtonModel = new Model<>("btn btn-outline-secondary");
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(
      IOneParameterAjaxAction<? super IModel<O>> onAjaxClick) {
    this.onAjaxClick = onAjaxClick;
    this.onClick = null;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepOneParameterTerminal<L, O> onClick(
      IOneParameterAction<? super IModel<O>> onClick) {
    this.onClick = onClick;
    this.onAjaxClick = null;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepTerminal<L, O> onClick(IAjaxAction onClick) {
    this.onAjaxClick = AjaxActions.ignoreParameter(onClick);
    this.onClick = null;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepTerminal<L, O> onClick(IAction onClick) {
    this.onClick = Actions.ignoreParameter(onClick);
    this.onAjaxClick = null;
    return this;
  }

  @Override
  public IConfirmLinkBuilderStepEndContent<L, O> submit(Form<?> form) {
    this.form = form;
    return this;
  }

  @Override
  public L create(String wicketId) {
    return create(wicketId, null);
  }

  protected static class FunctionalAjaxConfirmLink<O> extends AjaxConfirmLink<O> {
    private static final long serialVersionUID = -2098954474307467112L;

    private final IOneParameterAjaxAction<? super IModel<O>> onClick;

    public FunctionalAjaxConfirmLink(
        String id,
        IModel<O> model,
        Form<?> form,
        IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory,
        IDetachableFactory<? super IModel<O>, ? extends IModel<String>> textModelFactory,
        IModel<String> yesLabelModel,
        IModel<String> noLabelModel,
        IModel<String> yesIconModel,
        IModel<String> noIconModel,
        IModel<String> yesButtonModel,
        IModel<String> noButtonModel,
        IModel<String> cssClassNamesModel,
        boolean textNoEscape,
        IOneParameterAjaxAction<? super IModel<O>> onClick) {
      super(
          id,
          model,
          form,
          titleModelFactory.create(model),
          textModelFactory.create(model),
          yesLabelModel,
          noLabelModel,
          yesIconModel,
          noIconModel,
          yesButtonModel,
          noButtonModel,
          cssClassNamesModel,
          textNoEscape);
      this.onClick = onClick;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      this.onClick.execute(target, getModel());
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
      super.updateAjaxAttributes(attributes);
      this.onClick.updateAjaxAttributes(attributes, getModel());
    }

    @Override
    protected void onDetach() {
      super.onDetach();
      Detachables.detach(onClick);
    }
  }

  protected static class FunctionalConfirmLink<O> extends ConfirmLink<O> {
    private static final long serialVersionUID = -2098954474307467112L;

    private final IOneParameterAction<? super IModel<O>> onClick;

    public FunctionalConfirmLink(
        String id,
        IModel<O> model,
        IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory,
        IDetachableFactory<? super IModel<O>, ? extends IModel<String>> textModelFactory,
        IModel<String> yesLabelModel,
        IModel<String> noLabelModel,
        IModel<String> yesIconModel,
        IModel<String> noIconModel,
        IModel<String> yesButtonModel,
        IModel<String> noButtonModel,
        IModel<String> cssClassNamesModel,
        boolean textNoEscape,
        IOneParameterAction<? super IModel<O>> onClick) {
      super(
          id,
          model,
          titleModelFactory.create(model),
          textModelFactory.create(model),
          yesLabelModel,
          noLabelModel,
          yesIconModel,
          noIconModel,
          yesButtonModel,
          noButtonModel,
          cssClassNamesModel,
          textNoEscape);
      this.onClick = onClick;
    }

    @Override
    public void onClick() {
      this.onClick.execute(getModel());
    }

    @Override
    protected void onDetach() {
      super.onDetach();
      Detachables.detach(onClick);
    }
  }

  @Override
  public void detach() {
    Detachables.detach(
        titleModelFactory,
        contentModelFactory,
        yesLabelModel,
        noLabelModel,
        yesIconModel,
        noIconModel,
        yesButtonModel,
        noButtonModel,
        cssClassNamesModel,
        onClick);
  }
}
