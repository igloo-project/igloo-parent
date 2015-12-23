package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.action.IAjaxOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.builder.ActionColumnConfirmActionBuilder;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state.IAddedCoreColumnState;

public interface IActionColumnBuildState<T, S extends ISort<?>> {

	IActionColumnAddedLinkState<T, S> addLink(BootstrapLabelRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper);

	<C> IActionColumnAddedLinkState<T, S> addLink(BootstrapLabelRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper);

	IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapLabelRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper);

	<C> IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapLabelRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper);

	IActionColumnAddedActionState<T, S> addAction(BootstrapLabelRenderer<? super T> renderer,
			IAjaxOneParameterAjaxAction<IModel<T>> action);

	IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapLabelRenderer<? super T> renderer,
			IAjaxOneParameterAjaxAction<IModel<T>> action);

	ActionColumnConfirmActionBuilder<T, S> addConfirmAction(BootstrapLabelRenderer<? super T> renderer);

	IActionColumnBuildState<T, S> withClassOnElements(String cssClassOnElements);

	IAddedCoreColumnState<T, S> end();

}
