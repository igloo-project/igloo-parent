package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import static fr.openwide.core.wicket.more.condition.Condition.anyPermission;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.CollectionLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.InjectOnlyLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.SimpleLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ConditionLinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.model.BindingModel;

public class CoreLinkDescriptorBuilderParametersStateImpl<TResult>
		implements IParameterMappingState<TResult>, IAddedParameterMappingState<IParameterMappingState<TResult>> {
	
	private final IBuilderFactory<TResult> factory;
	private final Collection<ILinkParameterMappingEntry> parameterMappingEntries;
	private final Collection<ILinkParameterValidator> parameterValidators;
	
	private ILinkParameterMappingEntry lastAddedParameterMappingEntry;
	
	public CoreLinkDescriptorBuilderParametersStateImpl(IBuilderFactory<TResult> factory) {
		this.factory = factory;
		this.parameterMappingEntries = Lists.newArrayList();
		this.parameterValidators = Lists.newArrayList();
	}

	@Override
	public <T> IAddedParameterMappingState<IParameterMappingState<TResult>> map(String name, IModel<T> valueModel,
			Class<T> valueType) {
		Args.notNull(name, "name");
		Args.notNull(valueModel, "valueModel");
		Args.notNull(valueType, "valueType");

		return map(new SimpleLinkParameterMappingEntry<T>(name, valueModel, valueType));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					Class<T> elementType) {
		return mapCollection(parameterName, valueModel, rawCollectionType, TypeDescriptor.valueOf(elementType));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					TypeDescriptor elementTypeDescriptor) {
		return map(new CollectionLinkParameterMappingEntry<RawC, C>(
				parameterName, valueModel, rawCollectionType, elementTypeDescriptor
				));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>>
			mapCollection(String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
					TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier) {
		return map(new CollectionLinkParameterMappingEntry<RawC, C>(
				parameterName, valueModel, rawCollectionType, elementTypeDescriptor, emptyCollectionSupplier
				));
	}
	
	@Override
	public IAddedParameterMappingState<IParameterMappingState<TResult>> map(ILinkParameterMappingEntry parameterMappingEntry) {
		Args.notNull(parameterMappingEntry, "parameterMappingEntry");
		
		parameterMappingEntries.add(parameterMappingEntry);
		lastAddedParameterMappingEntry = parameterMappingEntry;
		
		return this;
	}
	
	@Override
	public <T> IAddedParameterMappingState<IParameterMappingState<TResult>> renderInUrl(String parameterName,
			IModel<T> valueModel) {
		return map(new InjectOnlyLinkParameterMappingEntry<>(parameterName, valueModel));
	}
	
	@Override
	public <R, T> IAddedParameterMappingState<IParameterMappingState<TResult>> renderInUrl(String parameterName,
			IModel<R> rootModel, AbstractBinding<R, T> binding) {
		return map(new InjectOnlyLinkParameterMappingEntry<>(parameterName, BindingModel.of(rootModel, binding)));
	}
	
	@Override
	public IParameterMappingState<TResult> optional() {
		return this;
	}
	
	@Override
	public IParameterMappingState<TResult> mandatory() {
		parameterValidators.add(lastAddedParameterMappingEntry.mandatoryValidator());
		return this;
	}

	@Override
	public IParameterMappingState<TResult> validator(ILinkParameterValidator validator) {
		Args.notNull(validator, "validator");
		parameterValidators.add(validator);
		return this;
	}

	@Override
	public IParameterMappingState<TResult> validator(Condition condition) {
		Args.notNull(condition, "condition");
		parameterValidators.add(new ConditionLinkParameterValidator(condition));
		return this;
	}
	
	@Override
	public IParameterMappingState<TResult> permission(IModel<?> model, String permissionName) {
		return validator(new ConditionLinkParameterValidator(Condition.permission(model, permissionName)));
	}
	
	@Override
	public IParameterMappingState<TResult> permission(IModel<?> model,
			String firstPermissionName, String... otherPermissionNames) {
		return validator(new ConditionLinkParameterValidator(anyPermission(model, firstPermissionName, otherPermissionNames)));
	}
	
	@Override
	public <R, T> IParameterMappingState<TResult> permission(
			IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName, String... otherPermissionNames) {
		return permission(BindingModel.of(model, binding), firstPermissionName, otherPermissionNames);
	}
	
	@Override
	public final TResult build() {
		return factory.create(parameterMappingEntries, parameterValidators);
	}

}
