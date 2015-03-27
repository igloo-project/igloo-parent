package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory;

import org.apache.wicket.model.IDetachable;
import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;

public interface ILinkParameterMappingEntryFactory<T extends Tuple> extends IDetachable {
	
	ILinkParameterMappingEntry create(T parameters);

}
