package org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory;

import org.javatuples.Tuple;

public abstract class AbstractLinkParameterMappingEntryFactory<T extends Tuple>
    implements ILinkParameterMappingEntryFactory<T> {

  private static final long serialVersionUID = 684813426404005774L;

  @Override
  public void detach() {}
}
