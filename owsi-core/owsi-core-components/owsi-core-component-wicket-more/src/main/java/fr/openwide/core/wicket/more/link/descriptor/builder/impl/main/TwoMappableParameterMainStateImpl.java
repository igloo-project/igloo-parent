package fr.openwide.core.wicket.more.link.descriptor.builder.impl.main;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.javatuples.Tuple;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.CoreTwoParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.ITwoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.ITwoMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.ITwoMappableParameterTwoChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

final class TwoMappableParameterMainStateImpl
		<
		TParam1, TParam2,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends AbstractOneOrMoreMappableParameterMainStateImpl
						<
						ITwoMappableParameterMainState
								<
								TParam1, TParam2,
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						>
		implements ITwoMappableParameterMainState
						<
						TParam1, TParam2,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						> {
	
	TwoMappableParameterMainStateImpl(
			AbstractOneOrMoreMappableParameterMainStateImpl
					<
					?,
					TEarlyTargetDefinitionLinkDescriptor,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			Class<?> addedParameterType) {
		super(previousState, addedParameterType, 2);
	}
	
	@Override
	public <TParam3> ThreeMappableParameterMainStateImpl<
			TParam1, TParam2, TParam3,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam3> clazz) {
		return new ThreeMappableParameterMainStateImpl<>(this, clazz);
	}

	private <TTarget, TLinkDescriptor> ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>
			createMapper(IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
					IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>> pageClassFactory,
					List<Integer> parameterIndices) {
		return new CoreTwoParameterLinkDescriptorMapperImpl<>(
				mapperLinkDescriptorFactory(
						linkDescriptorFactory,
						pageClassFactory, parameterIndices
				)
		);
	}
	
	private <TTarget, TLinkDescriptor> ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>
			createEarlyTargetDefinitionMapper(
					BuilderTargetFactories<TLinkDescriptor, TTarget, ?, ?, ?> targetFactories) {
		return createMapper(
				targetFactories.getEarlyTargetDefinitionLinkDescriptorFactory(),
				constantModelFactory(targetFactories.getEarlyTargetDefinitionTargetModel()),
				ImmutableList.<Integer>of()
		);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private class TwoParameterChosenParameterStateImpl
			extends AbstractInternalChosenParameterStateImpl<TwoParameterChosenParameterStateImpl>
			implements ITwoMappableParameterOneChosenParameterState, ITwoMappableParameterTwoChosenParameterState {

		@Override
		protected TwoParameterChosenParameterStateImpl thisAsTSelf() {
			return this;
		}

		@Override
		public ITwoParameterLinkDescriptorMapper<
				TLateTargetDefinitionPageLinkDescriptor,
				TParam1, TParam2
				> page(IDetachableFactory pageClassFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
					pageClassFactory, getParameterIndices()
			);
		}

		@Override
		public ITwoParameterLinkDescriptorMapper<
				TLateTargetDefinitionResourceLinkDescriptor,
				TParam1, TParam2
				> resource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}

		@Override
		public ITwoParameterLinkDescriptorMapper<
				TLateTargetDefinitionImageResourceLinkDescriptor,
				TParam1, TParam2
				> imageResource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}
	}
	
	private TwoParameterChosenParameterStateImpl pickNone() {
		return new TwoParameterChosenParameterStateImpl();
	}
	
	@Override
	public TwoParameterChosenParameterStateImpl pickFirst() {
		return pickNone().andFirst();
	}

	@Override
	public TwoParameterChosenParameterStateImpl pickSecond() {
		return pickNone().andSecond();
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<TEarlyTargetDefinitionLinkDescriptor, TParam1, TParam2> build() {
		return createEarlyTargetDefinitionMapper(getTargetFactories());
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2> page(
			IModel<? extends Class<? extends Page>> pageClassModel) {
		return pickNone().page(constantModelFactory(pageClassModel));
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2> resource(
			IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().resource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2
			> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2> page(
			Class<? extends Page> pageClass) {
		return page(Model.of(pageClass));
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2> resource(
			ResourceReference resourceReference) {
		return resource(Model.of(resourceReference));
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2
			> imageResource(ResourceReference resourceReference) {
		return imageResource(Model.of(resourceReference));
	}

}
