package org.iglooproject.wicket.more.link.descriptor.builder.state.pageinstance;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.IPageInstanceTargetTerminalState;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

public interface IPageInstanceState<TLinkDescriptor extends IPageLinkGenerator>
    extends IPageInstanceTargetTerminalState<TLinkDescriptor> {

  /**
   * Add a validator to the resulting link descriptor that will pass validation if the target page
   * is an instance of the given class (or any other class passed to this method), and raise an
   * error otherwise.
   *
   * <p>The validator will be executed each time a link will get {@link ILinkGenerator generated} or
   * link parameters will get {@link ILinkParametersExtractor extracted}.
   *
   * @param expectedPageClass The class that the target page should be an instance of.
   * @return The same builder for chaining calls.
   */
  <P extends Page> IPageInstanceState<TLinkDescriptor> validate(Class<P> expectedPageClass);

  /**
   * Similar to {@link #validate(Class)}, except the class that the target page should be an
   * instance of is contained in a model, which allows for dynamic constraint on the target page
   * class.
   *
   * @param expectedPageClassModel The model containing the class that the target page should be an
   *     instance of.
   * @return The same builder for chaining calls.
   */
  IPageInstanceState<TLinkDescriptor> validate(
      IModel<? extends Class<? extends Page>> expectedPageClassModel);
}
