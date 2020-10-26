package org.iglooproject.basicapp.web.application.common.form;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.IReferenceDataSearchQuery;
import org.iglooproject.basicapp.web.application.common.renderer.ReferenceDataRenderer;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class ReferenceDataAjaxDropDownSingleChoice<T extends ReferenceData<? super T>> extends GenericSelect2AjaxDropDownSingleChoice<T> {

	private static final long serialVersionUID = 7076114890845943476L;

	public ReferenceDataAjaxDropDownSingleChoice(
		String id,
		IModel<T> model,
		Class<T> clazz
	) {
		this(id, model, new ChoiceProvider<>(clazz));
	}

	public ReferenceDataAjaxDropDownSingleChoice(
		String id,
		IModel<T> model,
		ChoiceProvider<T> choiceProvider
	) {
		super(id, model, choiceProvider);
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
