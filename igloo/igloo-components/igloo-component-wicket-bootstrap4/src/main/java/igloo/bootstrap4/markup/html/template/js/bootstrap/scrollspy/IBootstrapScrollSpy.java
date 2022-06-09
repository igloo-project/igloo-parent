package igloo.bootstrap4.markup.html.template.js.bootstrap.scrollspy;

import igloo.bootstrap4.markup.html.template.js.IDetachableChainableStatement;

/**
 * @deprecated old code not reviewed for a long time. If needed, ask for re-inclusion.
 */
@Deprecated(since = "4.3.0")
public interface IBootstrapScrollSpy extends IDetachableChainableStatement {

	static final String CHAIN_LABEL = "scrollspy";

	@Override
	default String chainLabel() {
		return CHAIN_LABEL;
	}

	@Override
	default void detach() {
		// nothing to do
	}

}
