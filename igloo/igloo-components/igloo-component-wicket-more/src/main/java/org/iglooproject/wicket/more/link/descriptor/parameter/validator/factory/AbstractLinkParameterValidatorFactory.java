package org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory;

import org.javatuples.Tuple;

public abstract class AbstractLinkParameterValidatorFactory<T extends Tuple>
    implements ILinkParameterValidatorFactory<T> {

  private static final long serialVersionUID = 684813426404005774L;

  @Override
  public void detach() {}
}
