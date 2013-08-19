package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import java.io.Serializable;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;

public interface IParametersState<T extends ILinkDescriptor> extends IBuildState<T> {

	IParametersState<T> parameter(String name, IModel<?> valueModel);

	IParametersState<T> parameter(String name, Serializable value);

}
