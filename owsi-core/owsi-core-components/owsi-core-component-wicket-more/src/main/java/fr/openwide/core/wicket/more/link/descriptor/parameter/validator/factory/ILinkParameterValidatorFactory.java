package fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface ILinkParameterValidatorFactory<T extends Tuple> extends IDetachableFactory<T, ILinkParameterValidator> {

}
