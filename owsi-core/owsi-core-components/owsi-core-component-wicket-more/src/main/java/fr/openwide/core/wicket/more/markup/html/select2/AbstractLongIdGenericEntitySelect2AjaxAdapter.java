package fr.openwide.core.wicket.more.markup.html.select2;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.util.convert.converters.LongIdGenericEntityConverter;

public abstract class AbstractLongIdGenericEntitySelect2AjaxAdapter<T extends GenericEntity<Long, ?>> extends AbstractChoiceRendererSelect2AjaxAdapter<T> {
	
	private static final long serialVersionUID = 7880845591046909801L;
	
	private final LongIdGenericEntityConverter<T> entityConverter;

	public AbstractLongIdGenericEntitySelect2AjaxAdapter(Class<T> clazz, IChoiceRenderer<? super T> choiceRenderer) {
		super(choiceRenderer);
		this.entityConverter = new LongIdGenericEntityConverter<T>(clazz);
	}

	@Override
	public final T getChoice(String id) {
		return entityConverter.convertToObject(id, null /* unused */);
	}

}
