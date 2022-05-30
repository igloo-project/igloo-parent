package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import static org.iglooproject.wicket.api.condition.Condition.anyPermission;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.api.bindgen.BindingModel;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.iglooproject.wicket.api.model.ModelFactories;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.AbstractCoreAddedParameterMapperStateImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterMappingEntryBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.mapping.CollectionLinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.mapping.SimpleLinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.InjectOnlyLinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ConditionLinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

abstract class AbstractMainStateImpl
		<
		TSelf extends IMainState<TSelf>,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		implements IMainState<TSelf> {

	protected static <T> IDetachableFactory<Tuple, IModel<T>> constantModelFactory(IModel<T> earlyTargetDefinitionTargetModel) {
		return ModelFactories.constant(earlyTargetDefinitionTargetModel);
	}
	
	private final BuilderTargetFactories<
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> targetFactories;
	
	private final Map<LinkParameterMappingEntryBuilder<?>, List<Integer>> mappingEntryBuilders;
	private final Map<ILinkParameterValidatorFactory<?>, List<Integer>> validatorFactories;
	
	public AbstractMainStateImpl(
			BuilderTargetFactories<
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> targetFactories) {
		this.targetFactories = targetFactories;
		this.mappingEntryBuilders = Maps.newLinkedHashMap();
		this.validatorFactories = Maps.newLinkedHashMap();
	}
	
	public AbstractMainStateImpl(AbstractMainStateImpl<
			?,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> previousMainState) {
		this.targetFactories = previousMainState.targetFactories;
		this.mappingEntryBuilders = Maps.newLinkedHashMap(previousMainState.mappingEntryBuilders);
		this.validatorFactories = Maps.newLinkedHashMap(previousMainState.validatorFactories);
	}
	
	@SuppressWarnings("unchecked")
	protected TSelf thisAsTSelf() {
		return (TSelf) this;
	}

	@Override
	public <T> IAddedParameterMappingState<TSelf> map(String name, IModel<T> valueModel, Class<T> valueType) {
		Args.notNull(name, "name");
		Args.notNull(valueModel, "valueModel");
		Args.notNull(valueType, "valueType");
	
		return map(new SimpleLinkParameterMappingEntry<T>(
				name, valueModel, LinkParameterTypeInformation.valueOf(valueType).getTypeDescriptorSupplier()
		));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					Class<T> elementType) {
		return mapCollection(parameterName, valueModel, rawCollectionType, TypeDescriptor.valueOf(elementType));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					TypeDescriptor elementTypeDescriptor) {
		return map(new CollectionLinkParameterMappingEntry<>(
				parameterName, valueModel,
				LinkParameterTypeInformation.collection(rawCollectionType, elementTypeDescriptor)
						.getTypeDescriptorSupplier()
		));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					TypeDescriptor elementTypeDescriptor, SerializableSupplier2<C> emptyCollectionSupplier) {
		return map(new CollectionLinkParameterMappingEntry<>(
				parameterName, valueModel,
				LinkParameterTypeInformation.collection(rawCollectionType, elementTypeDescriptor)
						.getTypeDescriptorSupplier(),
				emptyCollectionSupplier
		));
	}
	
	protected final <TTuple extends Tuple> IAddedParameterMappingState<TSelf>
			doMap(final ILinkParameterMappingEntryFactory<TTuple> factory, List<Integer> parameterIndices) {
		LinkParameterMappingEntryBuilder<TTuple> builder = new LinkParameterMappingEntryBuilder<>(factory);
		mappingEntryBuilders.put(builder, parameterIndices);
		return new AbstractCoreAddedParameterMapperStateImpl<TSelf, TTuple>(builder) {
			@Override
			protected TSelf toNextState(LinkParameterMappingEntryBuilder<TTuple> builder) {
				return AbstractMainStateImpl.this.thisAsTSelf();
			}
		};
	}
	
	@Override
	public IAddedParameterMappingState<TSelf> map(ILinkParameterMappingEntry parameterMappingEntry) {
		Args.notNull(parameterMappingEntry, "parameterMappingEntry");
		return doMap(new ConstantLinkParameterMappingEntryFactory(parameterMappingEntry), ImmutableList.<Integer>of());
	}
	
	private static class ConstantLinkParameterMappingEntryFactory implements ILinkParameterMappingEntryFactory<Tuple> {
		private static final long serialVersionUID = 1L;
		
		private final ILinkParameterMappingEntry parameterMappingEntry;
		
		protected ConstantLinkParameterMappingEntryFactory(ILinkParameterMappingEntry parameterMappingEntry) {
			super();
			this.parameterMappingEntry = parameterMappingEntry;
		}

		@Override
		public void detach() {
			parameterMappingEntry.detach();
		}

		@Override
		public ILinkParameterMappingEntry create(Tuple parameters) {
			return parameterMappingEntry;
		}
	}
	
	@Override
	public <T> IAddedParameterMappingState<TSelf> renderInUrl(String parameterName,
			IModel<T> valueModel) {
		return map(new InjectOnlyLinkParameterMappingEntry<>(parameterName, valueModel));
	}
	
	@Override
	public <R, T> IAddedParameterMappingState<TSelf> renderInUrl(String parameterName,
			IModel<R> rootModel, BindingRoot<R, T> binding) {
		return map(new InjectOnlyLinkParameterMappingEntry<>(parameterName, BindingModel.of(rootModel, binding)));
	}
	
	protected final <TTuple extends Tuple> TSelf
			doValidator(final ILinkParameterValidatorFactory<TTuple> factory, List<Integer> parameterIndices) {
		validatorFactories.put(factory, parameterIndices);
		return thisAsTSelf();
	}
	
	@Override
	public TSelf validator(ILinkParameterValidator validator) {
		Args.notNull(validator, "validator");
		return doValidator(new ConstantLinkParameterValidatorFactory(validator), ImmutableList.<Integer>of());
	}
	
	private static class ConstantLinkParameterValidatorFactory implements ILinkParameterValidatorFactory<Tuple> {
		private static final long serialVersionUID = 1L;
		
		private final ILinkParameterValidator validator;
		
		protected ConstantLinkParameterValidatorFactory(ILinkParameterValidator validator) {
			super();
			this.validator = validator;
		}

		@Override
		public void detach() {
			validator.detach();
		}

		@Override
		public ILinkParameterValidator create(Tuple parameters) {
			return validator;
		}
	}
	
	@Override
	public TSelf validator(Condition condition) {
		Args.notNull(condition, "condition");
		return validator(new ConditionLinkParameterValidator(condition));
	}
	
	@Override
	public TSelf permission(IModel<?> model, String permissionName) {
		return validator(new ConditionLinkParameterValidator(Condition.permission(model, permissionName)));
	}
	
	@Override
	public TSelf permission(IModel<?> model,
			String firstPermissionName, String... otherPermissionNames) {
		return validator(new ConditionLinkParameterValidator(anyPermission(model, firstPermissionName, otherPermissionNames)));
	}
	
	@Override
	public <R, T> TSelf permission(
			IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName, String... otherPermissionNames) {
		return permission(BindingModel.of(model, binding), firstPermissionName, otherPermissionNames);
	}
	
	public BuilderTargetFactories<
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> getTargetFactories() {
		return targetFactories;
	}
	
	protected final <TTarget, TLinkDescriptor> IBuilderMapperLinkDescriptorFactory<TLinkDescriptor>
			mapperLinkDescriptorFactory(
					IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
					IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>> targetFactory,
					List<Integer> targetFactoryParameterIndices
			) {
		return linkDescriptorFactory.forMapper(
				Pair.with(targetFactory, targetFactoryParameterIndices),
				mappingEntryBuilders, validatorFactories
		);
	}

}
