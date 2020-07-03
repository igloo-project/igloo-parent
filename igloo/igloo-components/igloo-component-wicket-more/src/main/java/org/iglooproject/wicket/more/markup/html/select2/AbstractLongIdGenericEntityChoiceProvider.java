package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityConverterChoiceRenderer;
import org.iglooproject.wicket.more.util.convert.converters.LongIdGenericEntityConverter;

public abstract class AbstractLongIdGenericEntityChoiceProvider<T extends GenericEntity<Long, ?>> extends AbstractChoiceRendererChoiceProvider<T> {
	
	private static final long serialVersionUID = 7880845591046909801L;
	
	private final LongIdGenericEntityConverter<T> entityConverter;

	public AbstractLongIdGenericEntityChoiceProvider(Class<T> clazz, IConverter<? super T> converter) {
		this(clazz, new GenericEntityConverterChoiceRenderer<>(converter));
	}

	public AbstractLongIdGenericEntityChoiceProvider(Class<T> clazz, IChoiceRenderer<? super T> choiceRenderer) {
		super(choiceRenderer);
		this.entityConverter = new LongIdGenericEntityConverter<>(clazz);
	}

	@Override
	public Collection<T> toChoices(Collection<String> ids) {
		return ids.stream().map(id -> entityConverter.convertToObject(id, null /* unused */)).collect(Collectors.toList());
	}

}
