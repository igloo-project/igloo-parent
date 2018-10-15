package org.iglooproject.basicapp.web.application.administration.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.search.GeneralMessageSort;
import org.iglooproject.basicapp.core.business.message.search.IGeneralMessageSearchQuery;
import org.iglooproject.basicapp.core.business.message.service.IGeneralMessageService;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;

import com.google.common.collect.ImmutableMap;

public class GeneralMessageDataProvider extends AbstractSearchQueryDataProvider<GeneralMessage, GeneralMessageSort> {

	private static final long serialVersionUID = 2391048007215147522L;

	@SpringBean
	private IGeneralMessageService generalMessageService;

	// Tri
	private final CompositeSortModel<GeneralMessageSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			GeneralMessageSort.PUBLICATION_START_DATE_TIME, GeneralMessageSort.PUBLICATION_START_DATE_TIME.getDefaultOrder(),
			GeneralMessageSort.ID, GeneralMessageSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			GeneralMessageSort.ID, GeneralMessageSort.ID.getDefaultOrder()
		)
	);

	public GeneralMessageDataProvider() {
		Injector.get().inject(this);
	}

	@Override
	public IModel<GeneralMessage> model(GeneralMessage object) {
		return GenericEntityModel.of(object);
	}

	public CompositeSortModel<GeneralMessageSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected ISearchQuery<GeneralMessage, GeneralMessageSort> getSearchQuery() {
		return createSearchQuery(IGeneralMessageSearchQuery.class)
			.sort(sortModel.getObject());
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(sortModel);
	}

}
