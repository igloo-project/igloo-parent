package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.factory.LinkGeneratorFactory;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.sort.ISortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import fr.openwide.core.wicket.more.rendering.Renderer;

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

	<C> IAddedLabelColumnState<T, S> withTooltip(Function<? super T, C> function, Renderer<? super C> renderer);

	/**
	 * @deprecated Use {@link #withLink(IOneParameterLinkDescriptorMapper)} instead.
	 */
	@Deprecated
	IAddedLabelColumnState<T, S> withLink(LinkGeneratorFactory<T> linkGeneratorFactory);

	/**
	 * @deprecated Use {@link #withLink(AbstractCoreBinding, IOneParameterLinkDescriptorMapper)} instead.
	 */
	@Deprecated
	<C> IAddedLabelColumnState<T, S> withLink(AbstractCoreBinding<? super T, C> binding, LinkGeneratorFactory<C> linkGeneratorFactory);

	/**
	 * @deprecated Use {@link #withSideLink(IOneParameterLinkDescriptorMapper)} instead.
	 */
	@Deprecated
	IAddedLabelColumnState<T, S> withSideLink(LinkGeneratorFactory<T> linkGeneratorFactory);

	/**
	 * @deprecated Use {@link #withSideLink(AbstractCoreBinding, IOneParameterLinkDescriptorMapper)} instead.
	 */
	@Deprecated
	<C> IAddedLabelColumnState<T, S> withSideLink(AbstractCoreBinding<? super T, C> binding, LinkGeneratorFactory<C> linkGeneratorFactory);

	IAddedLabelColumnState<T, S> withLink(IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> linkGeneratorMapper);

	<C> IAddedLabelColumnState<T, S> withLink(AbstractCoreBinding<? super T, C> binding, IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> linkGeneratorMapper);

	IAddedLabelColumnState<T, S> withSideLink(IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> linkGeneratorMapper);

	<C> IAddedLabelColumnState<T, S> withSideLink(AbstractCoreBinding<? super T, C> binding, IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> linkGeneratorMapper);

	IAddedLabelColumnState<T, S> disableIfInvalid();

	IAddedLabelColumnState<T, S> hideIfInvalid();

}
