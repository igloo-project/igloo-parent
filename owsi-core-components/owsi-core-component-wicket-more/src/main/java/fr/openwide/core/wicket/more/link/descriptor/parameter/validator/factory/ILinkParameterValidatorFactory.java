package fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory;

import org.apache.wicket.model.IDetachable;
import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface ILinkParameterValidatorFactory<T extends Tuple> extends IDetachable {
	
	ILinkParameterValidator create(T parameters);

}
