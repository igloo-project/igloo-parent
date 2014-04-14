package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.SimpleMandatoryCollectionLinkParameterValidator;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;

@SuppressWarnings("rawtypes")
public class CollectionLinkParameterMappingEntry<RawC extends Collection, C extends RawC>
		extends AbstractLinkParameterMappingEntry {

	private static final long serialVersionUID = 2126702467532153474L;
	
	protected final String parameterName;
	protected final IModel<C> mappedModel;
	protected final Class<RawC> rawCollectionType;
	protected final TypeDescriptor mappedTypeDescriptor;
	protected final Function<? super C, ? extends C> collectionCustomizerFunction;

	public CollectionLinkParameterMappingEntry(String parameterName, IModel<C> mappedModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor) {
		this(parameterName, mappedModel, rawCollectionType, elementTypeDescriptor, Functions.<C>identity());
	}
	
	/**
	 * @param emptyCollectionSupplier Allows to perform fine-tuned customization on the actual collection instance that cannot be performed by
	 *                                the conversionService itself (for example, instantiation of TreeSet with a specific comparator)
	 */
	public CollectionLinkParameterMappingEntry(String parameterName, IModel<C> mappedModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, final Supplier<? extends C> emptyCollectionSupplier) {
		this(parameterName, mappedModel, rawCollectionType, elementTypeDescriptor,
				new SerializableFunction<C, C>() {
					private static final long serialVersionUID = 1L;
					@Override
					@SuppressWarnings("unchecked")
					public C apply(C input) {
						C newCollection = emptyCollectionSupplier.get();
						newCollection.addAll(input);
						return newCollection;
					}
				});
	}
	
	private CollectionLinkParameterMappingEntry(String parameterName, IModel<C> mappedModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, Function<? super C, ? extends C> collectionCustomizerFunction) {
		checkNotNull(parameterName);
		checkNotNull(mappedModel);
		checkNotNull(rawCollectionType);
		checkNotNull(elementTypeDescriptor);
		checkNotNull(collectionCustomizerFunction);
		this.parameterName = parameterName;
		this.mappedModel = mappedModel;
		this.rawCollectionType = rawCollectionType;
		this.mappedTypeDescriptor = TypeDescriptor.collection(rawCollectionType, elementTypeDescriptor);
		this.collectionCustomizerFunction = collectionCustomizerFunction;
	}
	
	@Override
	public void inject(PageParameters targetParameters, ILinkParameterConversionService conversionService) throws LinkParameterInjectionException {
		C collection = mappedModel.getObject();
		if (collection != null && collection.isEmpty()) {
			collection = null; // Just make sure that the default spring converter for collections won't translate this to an empty string.
		}
		inject(targetParameters, conversionService, parameterName, collection);
	}
	
	@Override
	public void extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService) throws LinkParameterExtractionException {
		Object extractedCollection = extract(sourceParameters, conversionService, parameterName, mappedTypeDescriptor);
		@SuppressWarnings("unchecked")
		C castedCollection = (C)rawCollectionType.cast(extractedCollection);
		C finalCollection = collectionCustomizerFunction.apply(castedCollection);
		mappedModel.setObject(finalCollection);
	}
	
	@Override
	public ILinkParameterMappingEntry wrap(Component component) {
		IModel<C> wrappedModel = wrap(mappedModel, component);
		return new CollectionLinkParameterMappingEntry<RawC, C>(parameterName, wrappedModel, rawCollectionType,
				mappedTypeDescriptor.getElementTypeDescriptor(), collectionCustomizerFunction);
	}
	
	@Override
	public ILinkParameterValidator mandatoryValidator() {
		return new SimpleMandatoryCollectionLinkParameterValidator(parameterName, mappedModel);
	}

	@Override
	public void detach() {
		mappedModel.detach();
	}

}
