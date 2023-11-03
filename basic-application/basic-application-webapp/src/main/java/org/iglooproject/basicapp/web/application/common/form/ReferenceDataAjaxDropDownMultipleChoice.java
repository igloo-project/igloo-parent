package org.iglooproject.basicapp.web.application.common.form;

import java.util.Collection;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSort;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.basicapp.web.application.common.renderer.ReferenceDataRenderer;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownMultipleChoice;
import org.wicketstuff.select2.Response;

import com.google.common.collect.ImmutableMap;

import igloo.wicket.model.Detachables;
import igloo.wicket.spring.SpringBeanLookupCache;

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
		
		private final SpringBeanLookupCache<IBasicReferenceDataSearchQuery<T>> referenceDataSearchQueryLookupCache;
		
		public ChoiceProvider(Class<T> clazz) {
			super(clazz, ReferenceDataRenderer.get());
			this.referenceDataSearchQueryLookupCache = SpringBeanLookupCache.<IBasicReferenceDataSearchQuery<T>>of(
				() -> CoreWicketApplication.get().getApplicationContext(),
				IBasicReferenceDataSearchQuery.class,
				clazz
			);
		}
		
		@Override
		protected void query(String term, int offset, int limit, Response<T> response) {
			BasicReferenceDataSearchQueryData<T> data = new BasicReferenceDataSearchQueryData<>();
			data.setLabel(term);
			data.setEnabledFilter(EnabledFilter.ENABLED_ONLY);
			Map<BasicReferenceDataSort, SortOrder> sorts = ImmutableMap.of(
				BasicReferenceDataSort.POSITION, BasicReferenceDataSort.POSITION.getDefaultOrder(),
				BasicReferenceDataSort.LABEL_FR, BasicReferenceDataSort.LABEL_FR.getDefaultOrder(),
				BasicReferenceDataSort.LABEL_EN, BasicReferenceDataSort.LABEL_EN.getDefaultOrder(),
				BasicReferenceDataSort.ID, BasicReferenceDataSort.ID.getDefaultOrder()
			);
			response.addAll(referenceDataSearchQueryLookupCache.get().list(data, sorts, offset, limit));
		}
		
		@Override
		public void detach() {
			super.detach();
			Detachables.detach(referenceDataSearchQueryLookupCache);
		}
	}

}
