package org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory;

import org.javatuples.Tuple;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface ILinkParameterValidatorFactory<T extends Tuple> extends IDetachableFactory<T, ILinkParameterValidator> {

}
