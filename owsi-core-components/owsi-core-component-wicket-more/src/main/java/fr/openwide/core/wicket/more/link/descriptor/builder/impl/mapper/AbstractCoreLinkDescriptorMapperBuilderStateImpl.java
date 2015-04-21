package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderParametersStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public abstract class AbstractCoreLinkDescriptorMapperBuilderStateImpl<Result, L extends ILinkDescriptor>
		implements IParameterMappingState<Result> {
	
	protected final CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory;
	
	public AbstractCoreLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory) {
		super();
		this.linkDescriptorFactory = linkDescriptorFactory;
	}
	
	protected abstract IBuilderFactory<Result> getFactory();
	
	private CoreLinkDescriptorBuilderParametersStateImpl<Result> toParametersState() {
		return new CoreLinkDescriptorBuilderParametersStateImpl<Result>(getFactory());
	}
	
	@Override
	public final Result build() {
		return toParametersState().build();
	}

	@Override
	public <T> IAddedParameterMappingState<IParameterMappingState<Result>> map(String parameterName, IModel<T> valueModel, Class<T> valueType) {
		return toParametersState().map(parameterName, valueModel, valueType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType, Class<T> elementType) {
		return toParametersState().mapCollection(parameterName, valueModel, rawCollectionType, elementType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
			TypeDescriptor elementTypeDescriptor) {
		return toParametersState().mapCollection(parameterName, valueModel, rawCollectionType, elementTypeDescriptor);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel, Class<RawC> rawCollectionType,
			TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier) {
		return toParametersState().mapCollection(parameterName, valueModel, rawCollectionType, elementTypeDescriptor, emptyCollectionSupplier);
	}

	@Override
	public IAddedParameterMappingState<IParameterMappingState<Result>> map(ILinkParameterMappingEntry parameterMappingEntry) {
		return toParametersState().map(parameterMappingEntry);
	}

	@Override
	public <T> IAddedParameterMappingState<IParameterMappingState<Result>> renderInUrl(String parameterName, IModel<T> valueModel) {
		return toParametersState().renderInUrl(parameterName, valueModel);
	}

	@Override
	public <R, T> IAddedParameterMappingState<IParameterMappingState<Result>> renderInUrl(String parameterName, IModel<R> rootModel,
			AbstractBinding<R, T> binding) {
		return toParametersState().renderInUrl(parameterName, rootModel, binding);
	}

	@Override
	public IParameterMappingState<Result> validator(ILinkParameterValidator validator) {
		return toParametersState().validator(validator);
	}

	@Override
	public IParameterMappingState<Result> validator(Condition condition) {
		return toParametersState().validator(condition);
	}

	@Override
	public IParameterMappingState<Result> permission(IModel<?> model, String permissionName) {
		return toParametersState().permission(model, permissionName);
	}

	@Override
	public IParameterMappingState<Result> permission(IModel<?> model, String firstPermissionName,
			String... otherPermissionNames) {
		return toParametersState().permission(model, firstPermissionName, otherPermissionNames);
	}

	@Override
	public <R, T> IParameterMappingState<Result> permission(IModel<R> model, BindingRoot<R, T> binding,
			String firstPermissionName, String... otherPermissionNames) {
		return toParametersState().permission(model, binding, firstPermissionName, otherPermissionNames);
	}

}
