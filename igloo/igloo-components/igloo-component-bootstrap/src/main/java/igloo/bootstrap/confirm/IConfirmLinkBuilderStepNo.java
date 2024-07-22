package igloo.bootstrap.confirm;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

public interface IConfirmLinkBuilderStepNo<L extends AbstractLink, O> {

  IConfirmLinkBuilderStepOnclick<L, O> no(IModel<String> noLabelModel);

  IConfirmLinkBuilderStepOnclick<L, O> no(IModel<String> noLabelModel, IModel<String> noIconModel);

  IConfirmLinkBuilderStepOnclick<L, O> no(
      IModel<String> noLabelModel, IModel<String> noIconModel, IModel<String> noButtonModel);
}
