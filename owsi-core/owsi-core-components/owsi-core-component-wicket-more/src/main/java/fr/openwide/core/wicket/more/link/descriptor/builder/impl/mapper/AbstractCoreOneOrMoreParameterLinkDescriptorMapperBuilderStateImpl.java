package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public abstract class AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl<Result, L extends ILinkDescriptor, SelfState, TLast>
		extends AbstractCoreLinkDescriptorMapperBuilderStateImpl<Result, L>
		implements IParameterMapperOneChosenParameterMappingState<SelfState, TLast> {
	
	protected final List<Class<?>> dynamicParameterTypes;
	
	protected final ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders;
	
	protected final ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories;
	
	public AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl(
			CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory, Class<?> addedParameterType) {
		super(linkDescriptorFactory);
		this.entryBuilders = LinkedListMultimap.create();
		this.validatorFactories = LinkedListMultimap.create();
		this.dynamicParameterTypes = ImmutableList.<Class<?>>of(addedParameterType);
	}
	
	public AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType, int expectedNumberOfParameters) {
		super(linkDescriptorFactory);
		this.entryBuilders = LinkedListMultimap.create(entryBuilders);
		this.validatorFactories = LinkedListMultimap.create(validatorFactories);
		this.dynamicParameterTypes = ImmutableList.<Class<?>>builder().addAll(dynamicParameterTypes).add(addedParameterType).build();
		Args.withinRange(expectedNumberOfParameters-1, expectedNumberOfParameters-1, dynamicParameterTypes.size(), "dynamicParameterTypes.size()");
	}

	protected abstract IParameterMapperOneChosenParameterMappingState<SelfState, TLast> pickLast();

	@Override
	public <T> IAddedParameterMappingState<SelfState> map(String parameterName) {
		return pickLast().map(parameterName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<SelfState> mapCollection(
			String parameterName, Class<T> elementType) {
		return pickLast().mapCollection(parameterName, elementType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<SelfState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor) {
		return pickLast().mapCollection(parameterName, elementTypeDescriptor);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<SelfState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier) {
		return pickLast().mapCollection(parameterName, elementTypeDescriptor, emptyCollectionSupplier);
	}

	@Override
	public IAddedParameterMappingState<SelfState> map(
			ILinkParameterMappingEntryFactory<? super Unit<IModel<TLast>>> parameterMappingEntryFactory) {
		return pickLast().map(parameterMappingEntryFactory);
	}

	@Override
	public <T> IAddedParameterMappingState<SelfState> renderInUrl(String parameterName) {
		return pickLast().renderInUrl(parameterName);
	}

	@Override
	public IAddedParameterMappingState<SelfState> renderInUrl(String parameterName,
			AbstractBinding<? super TLast, ?> binding) {
		return pickLast().renderInUrl(parameterName, binding);
	}

	@Override
	public SelfState validator(
			ILinkParameterValidatorFactory<? super Unit<IModel<TLast>>> parameterValidatorFactory) {
		return pickLast().validator(parameterValidatorFactory);
	}
	
	@Override
	public SelfState validator(IDetachableFactory<? super Unit<IModel<TLast>>, ? extends Condition> conditionFactory) {
		return pickLast().validator(conditionFactory);
	}

	@Override
	public SelfState validator(Predicate<? super TLast> predicate) {
		return pickLast().validator(predicate);
	}

	@Override
	public SelfState permission(String permissionName) {
		return pickLast().permission(permissionName);
	}

	@Override
	public SelfState permission(String firstPermissionName, String... otherPermissionNames) {
		return pickLast().permission(firstPermissionName, otherPermissionNames);
	}

	@Override
	public SelfState permission(BindingRoot<? super TLast, ?> binding,
			String firstPermissionName, String... otherPermissionNames) {
		return pickLast().permission(binding, firstPermissionName, otherPermissionNames);
	}

}
