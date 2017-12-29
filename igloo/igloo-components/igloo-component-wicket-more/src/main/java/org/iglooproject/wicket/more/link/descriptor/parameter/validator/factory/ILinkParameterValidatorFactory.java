package org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory;

import org.javatuples.Tuple;

import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;

public interface ILinkParameterValidatorFactory<T extends Tuple> extends IDetachableFactory<T, ILinkParameterValidator> {

}
