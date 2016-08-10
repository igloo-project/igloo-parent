package fr.openwide.core.wicket.more.link.descriptor.builder.impl.main;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.CoreOneParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.ITwoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IOneMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

final class OneMappableParameterMainStateImpl
		<
		TParam1,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends AbstractGenericOneMappableParameterMainStateImpl
						<
						IOneMappableParameterMainState
								<
								TParam1,
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>,
						TParam1,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor,
						IOneParameterLinkDescriptorMapper
								<
								TEarlyTargetDefinitionLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionPageLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionResourceLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionImageResourceLinkDescriptor,
								TParam1
								>
						>
		implements IOneMappableParameterMainState
						<
						TParam1,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						> {

	OneMappableParameterMainStateImpl(
			NoMappableParameterMainStateImpl<
					TEarlyTargetDefinitionLinkDescriptor,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			LinkParameterTypeInformation<TParam1> addedParameterType) {
		super(previousState, addedParameterType);
	}

	@Override
	public <TParam2> TwoMappableParameterMainStateImpl<
			TParam1, TParam2,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<TParam2> clazz) {
		return new TwoMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.valueOf(clazz)
		);
	}

	@Override
	public <TParam2 extends Collection<TElement>, TElement> ITwoMappableParameterMainState<
			TParam1, TParam2,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam2> clazz, Class<TElement> elementType) {
		return new TwoMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementType)
		);
	}
	
	@Override
	public <TParam2 extends Collection<?>> ITwoMappableParameterMainState<
			TParam1, TParam2,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam2> clazz, TypeDescriptor elementTypeDescriptor) {
		return new TwoMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor)
		);
	}
	
	@Override
	public <TParam2 extends Collection<?>> ITwoMappableParameterMainState<
			TParam1, TParam2,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam2> clazz, TypeDescriptor elementTypeDescriptor,
						Supplier<? extends TParam2> emptyCollectionSupplier) {
		return new TwoMappableParameterMainStateImpl<>(
				this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor, emptyCollectionSupplier)
		);
	}
	
	private <TTarget, TLinkDescriptor> IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>
			createMapper(IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
					IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>> pageClassFactory,
					List<Integer> parameterIndices) {
		return new CoreOneParameterLinkDescriptorMapperImpl<>(
				mapperLinkDescriptorFactory(
						linkDescriptorFactory, 
						pageClassFactory, parameterIndices 
				)
		);
	}
	
	private <TTarget, TLinkDescriptor> IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>
			createEarlyTargetDefinitionMapper(
					BuilderTargetFactories<TLinkDescriptor, TTarget, ?, ?, ?> targetFactories) {
		return createMapper(
				targetFactories.getEarlyTargetDefinitionLinkDescriptorFactory(),
				constantModelFactory(targetFactories.getEarlyTargetDefinitionTargetModel()),
				ImmutableList.<Integer>of()
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class OneParameterChosenParameterStateImpl
			extends AbstractInternalChosenParameterStateImpl<OneParameterChosenParameterStateImpl>
			implements IOneMappableParameterOneChosenParameterState {

		@Override
		protected OneParameterChosenParameterStateImpl thisAsTSelf() {
			return this;
		}

		@Override
		public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1>
				page(IDetachableFactory pageClassFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
					pageClassFactory, getParameterIndices()
			);
		}

		@Override
		public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1>
				resource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}

		@Override
		public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionImageResourceLinkDescriptor, TParam1>
				imageResource(IDetachableFactory resourceReferenceFactory) {
			return createMapper(
					getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
					resourceReferenceFactory, getParameterIndices()
			);
		}
	}
	
	private OneParameterChosenParameterStateImpl pickNone() {
		return new OneParameterChosenParameterStateImpl();
	}
	
	@Override
	protected OneParameterChosenParameterStateImpl pickLast() {
		return pickNone().andFirst();
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TEarlyTargetDefinitionLinkDescriptor, TParam1> build() {
		return createEarlyTargetDefinitionMapper(getTargetFactories());
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1> page(
			IModel<? extends Class<? extends Page>> pageClassModel) {
		return pickNone().page(constantModelFactory(pageClassModel));
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1> resource(
			IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().resource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionImageResourceLinkDescriptor, TParam1> imageResource(
			IModel<? extends ResourceReference> resourceReferenceModel) {
		return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionPageLinkDescriptor, TParam1> page(
			Class<? extends Page> pageClass) {
		return page(Model.of(pageClass));
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionResourceLinkDescriptor, TParam1> resource(
			ResourceReference resourceReference) {
		return resource(Model.of(resourceReference));
	}

	@Override
	public IOneParameterLinkDescriptorMapper<TLateTargetDefinitionImageResourceLinkDescriptor, TParam1> imageResource(
			ResourceReference resourceReference) {
		return imageResource(Model.of(resourceReference));
	}


}
