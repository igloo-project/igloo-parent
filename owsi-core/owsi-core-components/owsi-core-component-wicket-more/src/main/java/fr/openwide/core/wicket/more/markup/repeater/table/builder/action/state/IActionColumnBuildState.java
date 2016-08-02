package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAction;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

public interface IActionColumnBuildState<T, S extends ISort<?>> {

	IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper);

	<C> IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper);

	IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper);

	<C> IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper);

	IActionColumnAddedAjaxActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAjaxAction<? super IModel<T>> action);

	IActionColumnAddedAjaxActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAjaxAction<? super IModel<T>> action);

	IActionColumnAddedActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<? super IModel<T>> action);

	IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<? super IModel<T>> action);

	IActionColumnConfirmActionBuilderStepStart<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer);

	IActionColumnAddedActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory);

	IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory);

	IActionColumnBuildState<T, S> withClassOnElements(String cssClassOnElements);

	IAddedCoreColumnState<T, S> end();

}
