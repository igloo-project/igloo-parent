package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

public interface ITerminalState<L extends ILinkParametersExtractor> {

	L build();

}
