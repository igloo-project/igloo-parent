package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.util.convert.IConverter;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.more.util.convert.converters.LongIdGenericEntityConverter;

public abstract class AbstractGenericEntitySelect2AjaxResourceReference<E extends GenericEntity<Long, ?>> extends AjaxSearchResourceReference<E> {

	private static final long serialVersionUID = 1L;

	public AbstractGenericEntitySelect2AjaxResourceReference(Class<?> scope, String name, Class<E> targetType, IConverter<? super E> renderer) {
		super(scope, name, new LongIdGenericEntityConverter<E>(targetType), renderer);
	}

}
