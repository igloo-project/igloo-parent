package fr.openwide.core.wicket.more.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Preconditions;

import fr.openwide.core.jpa.more.business.property.model.CompositeProperty;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;
import fr.openwide.core.jpa.more.business.property.service.IPropertyService;

public class ApplicationPropertyModel<T> extends LoadableDetachableModel<T> {

	private static final long serialVersionUID = 7221634823252925011L;

	@SpringBean
	private IPropertyService propertyService;

	private final PropertyId<T> propertyId;

	private final CompositeProperty<T, ?, ?> compositeProperty;

	public static <T> IModel<T> of(PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		return new ApplicationPropertyModel<T>(propertyId);
	}

	public static <T> IModel<T> of(CompositeProperty<T, ?, ?> compositeProperty) {
		Preconditions.checkNotNull(compositeProperty);
		return new ApplicationPropertyModel<T>(compositeProperty);
	}

	protected ApplicationPropertyModel(PropertyId<T> propertyId) {
		this(propertyId, null);
	}
	
	protected ApplicationPropertyModel(CompositeProperty<T, ?, ?> compositeProperty) {
		this(null, compositeProperty);
	}

	private ApplicationPropertyModel(PropertyId<T> propertyId, CompositeProperty<T, ?, ?> compositeProperty) {
		super();
		Injector.get().inject(this);
		this.propertyId = propertyId;
		this.compositeProperty = compositeProperty;
	}

	@Override
	protected T load() {
		if (propertyId != null) {
			return propertyService.get(propertyId);
		} else if (compositeProperty != null) {
			return propertyService.get(compositeProperty);
		} else {
			throw new IllegalStateException("A property is required.");
		}
	}

}
