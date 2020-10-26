package org.iglooproject.basicapp.web.application.common.form;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.IReferenceDataSearchQuery;
import org.iglooproject.basicapp.web.application.common.renderer.ReferenceDataRenderer;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownMultipleChoice;
import org.wicketstuff.select2.Response;

public class ReferenceDataAjaxDropDownMultipleChoice<T extends ReferenceData<? super T>, C extends Collection<T>> extends GenericSelect2AjaxDropDownMultipleChoice<T> {

	private static final long serialVersionUID = 8228499285903000586L;

	public ReferenceDataAjaxDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		Class<T> clazz
	) {
		this(id, model, collectionSupplier, new ChoiceProvider<>(clazz));
	}

	public ReferenceDataAjaxDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		ChoiceProvider<T> choiceProvider
	) {
		super(id, model, collectionSupplier, choiceProvider);
	}

	private static class ChoiceProvider<T extends ReferenceData<? super T>> extends AbstractLongIdGenericEntityChoiceProvider<T> {
		
		private static final long serialVersionUID = 1L;
		
		private final Class<T> clazz;
		
		public ChoiceProvider(Class<T> clazz) {
			super(clazz, ReferenceDataRenderer.get());
			this.clazz = clazz;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void query(String term, int offset, int limit, Response<T> response) {
			response.addAll(
				getBean(IReferenceDataSearchQuery.class, clazz)
					.label(term)
					.list(offset, limit)
			);
		}
	}

}
