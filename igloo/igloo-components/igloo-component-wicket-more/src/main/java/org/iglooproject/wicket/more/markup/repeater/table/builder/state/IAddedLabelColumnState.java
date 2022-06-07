package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.sort.ISortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.renderer.Renderer;

public interface IAddedLabelColumnState<T, S extends ISort<?>> extends IAddedCoreColumnState<T, S> {

	@Override
	IAddedLabelColumnState<T, S> when(Condition condition);
	
	@Override
	IAddedLabelColumnState<T, S> withClass(String cssClass);
	
	@Override
	IAddedLabelColumnState<T, S> withSort(S sort);

	@Override
	IAddedLabelColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle);

	@Override
	IAddedLabelColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle, CycleMode cycleMode);

	IAddedLabelColumnState<T, S> multiline();

	IAddedLabelColumnState<T, S> showPlaceholder();

	IAddedLabelColumnState<T, S> showPlaceholder(IModel<String> placeholderModel);

	IAddedLabelColumnState<T, S> withTooltip(Renderer<? super T> tooltipRenderer);

	<C> IAddedLabelColumnState<T, S> withTooltip(SerializableFunction2<? super T, C> function, Renderer<? super C> renderer);

	IAddedLabelColumnState<T, S> withLink(ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper);
	
	<C> IAddedLabelColumnState<T, S> withLink(SerializableFunction2<? super T, C> binding, ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper);

	<C> IAddedLabelColumnState<T, S> withLink(ICoreBinding<? super T, C> binding, ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper);

	IAddedLabelColumnState<T, S> withSideLink(ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper);
	
	<C> IAddedLabelColumnState<T, S> withSideLink(SerializableFunction2<? super T, C> binding, ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper);

	<C> IAddedLabelColumnState<T, S> withSideLink(ICoreBinding<? super T, C> binding, ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper);

	/**
	 * @deprecated This is the default behavior, calling this method is no longer needed.
	 */
	@Deprecated
	IAddedLabelColumnState<T, S> disableIfInvalid();

	IAddedLabelColumnState<T, S> hideIfInvalid();

	IAddedLabelColumnState<T, S> linkBehavior(Behavior linkBehavior);

	IAddedLabelColumnState<T, S> targetBlank();

}
