package igloo.bootstrap.modal;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public interface IBootstrapModal extends ChainableStatement {

	IBootstrapModal setBackdrop(BootstrapModalBackdrop static1);

	IBootstrapModal setKeyboard(Boolean b);

	void addCancelBehavior(Component component);

	JsStatement show(Component modal);

	JsStatement hide(Component modal);

	JsStatement modal(Component modal);

}