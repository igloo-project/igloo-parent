package org.iglooproject.wicket.bootstrap3.markup.html.bootstrap;

import java.util.function.Supplier;

import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.functional.SerializableSupplier;
import org.iglooproject.wicket.bootstrap3.markup.html.bootstrap.component.BootstrapBadge;
import org.iglooproject.wicket.bootstrap3.markup.html.bootstrap.component.BootstrapLabel;
import org.iglooproject.wicket.more.application.IWicketBootstrapComponentsModule;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;

@org.springframework.stereotype.Component
public class WicketBootstrapComponentsModule implements IWicketBootstrapComponentsModule {

	@Override
	public <T> Supplier<BootstrapBadge<T>> badgeSupplier(String id, IModel<T> model, BootstrapRenderer<? super T> renderer) {
		return new Supplier<BootstrapBadge<T>>() {
			@Override
			public BootstrapBadge<T> get() {
				return new BootstrapBadge<>(id, model, renderer);
			}
		};
	}

	@Override
	public <T> Supplier<BootstrapLabel<T>> labelSupplier(String id, IModel<T> model, BootstrapRenderer<? super T> renderer) {
		return new SerializableSupplier<BootstrapLabel<T>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public BootstrapLabel<T> get() {
				return new BootstrapLabel<>(id, model, renderer);
			}
		};
	}

}
