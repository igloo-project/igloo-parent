package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public interface IBootstrapConfirm {

	JsStatement confirm(Component modal);

}
