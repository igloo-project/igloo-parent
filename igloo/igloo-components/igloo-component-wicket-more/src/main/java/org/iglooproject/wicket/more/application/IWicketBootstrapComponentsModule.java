package org.iglooproject.wicket.more.application;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapBadge;

public interface IWicketBootstrapComponentsModule {

	<T> SerializableSupplier2<? extends IBootstrapBadge<T, ? extends IBootstrapBadge<T, ?>>> badgeSupplier(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer);

}
