package org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory;

import igloo.wicket.factory.IDetachableFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.javatuples.Tuple;

public interface ILinkParameterValidatorFactory<T extends Tuple>
    extends IDetachableFactory<T, ILinkParameterValidator> {}
