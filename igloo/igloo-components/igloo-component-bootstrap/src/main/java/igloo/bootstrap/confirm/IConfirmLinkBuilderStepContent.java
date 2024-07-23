package igloo.bootstrap.confirm;

import igloo.wicket.factory.IDetachableFactory;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

public interface IConfirmLinkBuilderStepContent<L extends AbstractLink, O> {

  IConfirmLinkBuilderStepEndContent<L, O> content(IModel<String> contentModel);

  IConfirmLinkBuilderStepEndContent<L, O> content(
      IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory);
}
