package org.iglooproject.bootstrap.api;

import org.wicketstuff.wiquery.core.javascript.ChainableStatement;

public interface IBootstrapModal extends ChainableStatement {

	IBootstrapModal setBackdrop(BootstrapModalBackdrop static1);

	IBootstrapModal setKeyboard(Boolean b);

}