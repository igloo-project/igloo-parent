package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper.CoreThreeParameterLinkDescriptorMapperImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFourMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IThreeMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IThreeMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IThreeMappableParameterThreeChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IThreeMappableParameterTwoChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;


final class ThreeMappableParameterMainStateImpl
		<
		TParam1, TParam2, TParam3,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends AbstractOneOrMoreMappableParameterMainStateImpl
				<
				IThreeMappableParameterMainState
						<
						TParam1, TParam2, TParam3,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						>,
				TLateTargetDefinitionPageLinkDescriptor,
				TLateTargetDefinitionResourceLinkDescriptor,
				TLateTargetDefinitionImageResourceLinkDescriptor
				>
		implements IThreeMappableParameterMainState
				<
				TParam1, TParam2, TParam3,
				TLateTargetDefinitionPageLinkDescriptor,
				TLateTargetDefinitionResourceLinkDescriptor,
				TLateTargetDefinitionImageResourceLinkDescriptor
				> {
	
	ThreeMappableParameterMainStateImpl(
			AbstractOneOrMoreMappableParameterMainStateImpl
					<
					?,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			LinkParameterTypeInformation<TParam3> addedParameterType) {
		super(previousState, addedParameterType, 3);
	}
	
	@Override
	public <TParam4> FourMappableParameterMainStateImpl<
			TParam1, TParam2, TParam3, TParam4,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<TParam4> clazz) {
		return new FourMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.valueOf(clazz)
		);
	}

	@Override
	public <TParam4 extends Collection<TElement>, TElement> IFourMappableParameterMainState<
			TParam1, TParam2, TParam3, TParam4,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam4> clazz, Class<TElement> elementType) {
		return new FourMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementType)
		);
	}
	
	@Override
	public <TParam4 extends Collection<?>> IFourMappableParameterMainState<
			TParam1, TParam2, TParam3, TParam4,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam4> clazz, TypeDescriptor elementTypeDescriptor) {
		return new FourMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor)
		);
	}
	
	@Override
	public <TParam4 extends Collection<?>> IFourMappableParameterMainState<
			TParam1, TParam2, TParam3, TParam4,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam4> clazz, TypeDescriptor elementTypeDescriptor,
						SerializableSupplier2<? extends TParam4> emptyCollectionSupplier) {
		return new FourMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor, emptyCollectionSupplier)
		);
	}
	
	private <TTarget, TLinkDescriptor> IThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3>
			createMapper(IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
					IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>> pageClassFactory,
					List<Integer> parameterIndices) {
		return new CoreThreeParameterLinkDescriptorMapperImpl<>(
				mapperLinkDescriptorFactory(
						linkDescriptorFactory,
						pageClassFactory, parameterIndices
				)
		);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private class ThreeParameterChosenParameterStateImpl
			extends AbstractInternalChosenParameterStateImpl<ThreeParameterChosenParameterStateImpl>
			implements IThreeMappableParameterOneChosenParameterState,
					IThreeMappableParameterTwoChosenParameterState,
					IThreeMappableParameterThreeChosenParameterState {

		@Override
		protected ThreeParameterChosenParameterStateImpl thisAsTSelf() {
			return this;
		}

		@Override
		public IThreeParameterLinkDescriptorMapper<
				TLateTargetDefinitionPageLinkDescriptor,
				TParam1, TParam2, TParam3
				> page(IDetachableFactory pageClassFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
					pageClassFactory, getParameterIndices()
			);
		}

		@Override
		public IThreeParameterLinkDescriptorMapper<
				TLateTargetDefinitionResourceLinkDescriptor,
				TParam1, TParam2, TParam3
				> resource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}

		@Override
		public IThreeParameterLinkDescriptorMapper<
				TLateTargetDefinitionImageResourceLinkDescriptor,
				TParam1, TParam2, TParam3
				> imageResource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}
	}
	
	private ThreeParameterChosenParameterStateImpl pickNone() {
		return new ThreeParameterChosenParameterStateImpl();
	}
	
	@Override
	public ThreeParameterChosenParameterStateImpl pickFirst() {
		return pickNone().andFirst();
	}

	@Override
	public ThreeParameterChosenParameterStateImpl pickSecond() {
		return pickNone().andSecond();
	}

	@Override
	public ThreeParameterChosenParameterStateImpl pickThird() {
		return pickNone().andThird();
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionPageLinkDescriptor,
			TParam1, TParam2, TParam3
			> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return pickNone().page(constantModelFactory(pageClassModel));
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionResourceLinkDescriptor,
			TParam1, TParam2, TParam3
			> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().resource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2, TParam3
			> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionPageLinkDescriptor,
			TParam1, TParam2, TParam3
			> page(Class<? extends Page> pageClass) {
		return page(Model.of(pageClass));
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionResourceLinkDescriptor,
			TParam1, TParam2, TParam3
			> resource(ResourceReference resourceReference) {
		return resource(Model.of(resourceReference));
	}

	@Override
	public IThreeParameterLinkDescriptorMapper<
			TLateTargetDefinitionImageResourceLinkDescriptor,
			TParam1, TParam2, TParam3
			> imageResource(ResourceReference resourceReference) {
		return imageResource(Model.of(resourceReference));
	}
}
