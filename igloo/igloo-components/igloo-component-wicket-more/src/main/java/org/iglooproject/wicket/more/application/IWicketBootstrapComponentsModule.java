package org.iglooproject.wicket.more.application;

import java.util.function.Supplier;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapBadge;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapLabel;

public interface IWicketBootstrapComponentsModule {

	<T> Supplier<? extends IBootstrapBadge<T, ? extends IBootstrapBadge<T, ?>>> badgeSupplier(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer);

	@Deprecated
	<T> Supplier<? extends IBootstrapLabel<T, ? extends IBootstrapLabel<T, ?>>> labelSupplier(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer);

}
