package org.iglooproject.wicket.more.markup.html.bootstrap.component;

import org.apache.wicket.Component;

@Deprecated
public interface IBootstrapLabel<T, C extends Component & IBootstrapLabel<T, C>> {

	C asComponent();

}
