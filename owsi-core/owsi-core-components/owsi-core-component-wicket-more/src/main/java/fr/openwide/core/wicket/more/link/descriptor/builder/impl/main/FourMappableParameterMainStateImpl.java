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
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.CoreFourParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IFourMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterFourChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterThreeChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterTwoChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

final class FourMappableParameterMainStateImpl
		<
		TParam1, TParam2, TParam3, TParam4,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends AbstractOneOrMoreMappableParameterMainStateImpl
				<
				IFourMappableParameterMainState
						<
						TParam1, TParam2, TParam3, TParam4,
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
		implements IFourMappableParameterMainState
				<
				TParam1, TParam2, TParam3, TParam4,
				TEarlyTargetDefinitionLinkDescriptor,
				TLateTargetDefinitionPageLinkDescriptor,
				TLateTargetDefinitionResourceLinkDescriptor,
				TLateTargetDefinitionImageResourceLinkDescriptor
				> {
	
	FourMappableParameterMainStateImpl(
			AbstractOneOrMoreMappableParameterMainStateImpl
					<
					?,
					TEarlyTargetDefinitionLinkDescriptor,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			Class<?> addedParameterType) {
		super(previousState, addedParameterType, 4);
	}

	private <TTarget, TLinkDescriptor> IFourParameterLinkDescriptorMapper<
			TLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> createMapper(IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
					IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>> pageClassFactory,
					List<Integer> parameterIndices) {
		return new CoreFourParameterLinkDescriptorMapperImpl<>(
				mapperLinkDescriptorFactory(
						linkDescriptorFactory,
						pageClassFactory, parameterIndices
				)
		);
	}
	
	private <TTarget, TLinkDescriptor> IFourParameterLinkDescriptorMapper<
			TLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> createEarlyTargetDefinitionMapper(
					BuilderTargetFactories<TLinkDescriptor, TTarget, ?, ?, ?> targetFactories) {
		return createMapper(
				targetFactories.getEarlyTargetDefinitionLinkDescriptorFactory(),
				constantModelFactory(targetFactories.getEarlyTargetDefinitionTargetModel()),
				ImmutableList.<Integer>of()
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class FourParameterChosenParameterStateImpl
			extends AbstractInternalChosenParameterStateImpl<FourParameterChosenParameterStateImpl>
			implements IFourMappableParameterOneChosenParameterState,
					IFourMappableParameterTwoChosenParameterState,
					IFourMappableParameterThreeChosenParameterState,
					IFourMappableParameterFourChosenParameterState {

		@Override
		protected FourParameterChosenParameterStateImpl thisAsTSelf() {
			return this;
		}

		@Override
		public IFourParameterLinkDescriptorMapper<
				TLateTargetDefinitionPageLinkDescriptor,
				TParam1, TParam2, TParam3, TParam4
				> page(IDetachableFactory pageClassFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
					pageClassFactory, getParameterIndices()
			);
		}

		@Override
		public IFourParameterLinkDescriptorMapper<
				TLateTargetDefinitionResourceLinkDescriptor,
				TParam1, TParam2, TParam3, TParam4
				> resource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}

		@Override
		public IFourParameterLinkDescriptorMapper<
				TLateTargetDefinitionImageResourceLinkDescriptor,
				TParam1, TParam2, TParam3, TParam4
				> imageResource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}
	}
	
	private FourParameterChosenParameterStateImpl pickNone() {
		return new FourParameterChosenParameterStateImpl();
	}
	
	@Override
	public FourParameterChosenParameterStateImpl pickFirst() {
		return pickNone().andFirst();
	}

	@Override
	public FourParameterChosenParameterStateImpl pickSecond() {
		return pickNone().andSecond();
	}

	@Override
	public FourParameterChosenParameterStateImpl pickThird() {
		return pickNone().andThird();
	}

	@Override
	public FourParameterChosenParameterStateImpl pickFourth() {
		return pickNone().andFourth();
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TEarlyTargetDefinitionLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> build() {
		return createEarlyTargetDefinitionMapper(getTargetFactories());
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionPageLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return pickNone().page(constantModelFactory(pageClassModel));
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionResourceLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().resource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionPageLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> page(Class<? extends Page> pageClass) {
		return page(Model.of(pageClass));
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionResourceLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> resource(ResourceReference resourceReference) {
		return resource(Model.of(resourceReference));
	}

	@Override
	public IFourParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2, TParam3, TParam4
			> imageResource(ResourceReference resourceReference) {
		return imageResource(Model.of(resourceReference));
	}

}
