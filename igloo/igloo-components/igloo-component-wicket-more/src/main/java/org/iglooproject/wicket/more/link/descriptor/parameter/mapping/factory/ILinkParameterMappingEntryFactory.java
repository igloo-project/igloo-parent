package org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory;

import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.javatuples.Tuple;

public interface ILinkParameterMappingEntryFactory<T extends Tuple> extends IDetachable {

  ILinkParameterMappingEntry create(T parameters);
}
