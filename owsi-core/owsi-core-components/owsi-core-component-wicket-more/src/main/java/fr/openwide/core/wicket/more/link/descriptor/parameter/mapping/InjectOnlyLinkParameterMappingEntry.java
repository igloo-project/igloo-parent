package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.bindgen.binding.AbstractBinding;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.AbstractLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.SimpleMandatoryLinkParameterValidator;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;
import fr.openwide.core.wicket.more.model.BindingModel;

public class InjectOnlyLinkParameterMappingEntry<T> extends AbstractLinkParameterMappingEntry {

	private static final long serialVersionUID = -5638280709363241126L;

	protected final String parameterName;
	protected final IModel<? extends T> mappedModel;
	
	public static final <R, T> InjectOnlyLinkParameterMappingEntry<T> of(String parameterName, IModel<R> rootModel, AbstractBinding<R, T> binding) {
		return new InjectOnlyLinkParameterMappingEntry<T>(parameterName, BindingModel.of(rootModel, binding));
	}
	
	public static final <T> InjectOnlyLinkParameterMappingEntry<T> of(String parameterName, IModel<? extends T> mappedModel) {
		return new InjectOnlyLinkParameterMappingEntry<T>(parameterName, mappedModel);
	}
	
	protected InjectOnlyLinkParameterMappingEntry(String parameterName, IModel<? extends T> mappedModel) {
		this.parameterName = parameterName;
		this.mappedModel = mappedModel;
	}
	
	@Override
	public void inject(PageParameters targetParameters, ILinkParameterConversionService conversionService) throws LinkParameterInjectionException {
		inject(targetParameters, conversionService, parameterName, mappedModel.getObject());
	}
	
	@Override
	public void extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService) {
		// Nothing to do
	}
	
	@Override
	public ILinkParameterMappingEntry wrap(Component component) {
		IModel<? extends T> wrappedModel = wrap(mappedModel, component);
		return new InjectOnlyLinkParameterMappingEntry<T>(parameterName, wrappedModel);
	}
	
	@Override
	public ILinkParameterValidator mandatoryValidator() {
		return new SimpleMandatoryLinkParameterValidator(
				ImmutableList.of(parameterName),
				ImmutableList.<IModel<?>>of() // The model is not mandatory : we won't extract it anyway
		);
	}

	@Override
	public void detach() {
		mappedModel.detach();
	}

}
