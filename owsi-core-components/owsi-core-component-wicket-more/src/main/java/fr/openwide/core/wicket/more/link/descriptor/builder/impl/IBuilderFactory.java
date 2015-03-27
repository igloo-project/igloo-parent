package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IBuilderFactory<Result> {

	Result create(LinkParametersMapping parametersMapping, ILinkParameterValidator validator);

}
