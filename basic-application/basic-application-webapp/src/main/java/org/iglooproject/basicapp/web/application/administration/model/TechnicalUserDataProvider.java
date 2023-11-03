package org.iglooproject.basicapp.web.application.administration.model;

import java.util.function.UnaryOperator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.search.ITechnicalUserSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.TechnicalUserSearchQueryData;
import org.iglooproject.basicapp.core.business.user.search.TechnicalUserSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import com.google.common.collect.ImmutableMap;

public class TechnicalUserDataProvider extends SearchQueryDataProvider<TechnicalUser, TechnicalUserSort, TechnicalUserSearchQueryData, ITechnicalUserSearchQuery> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private ITechnicalUserSearchQuery searchQuery;

	private final CompositeSortModel<TechnicalUserSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			TechnicalUserSort.SCORE, TechnicalUserSort.SCORE.getDefaultOrder(),
			TechnicalUserSort.LAST_NAME, TechnicalUserSort.LAST_NAME.getDefaultOrder(),
			TechnicalUserSort.FIRST_NAME, TechnicalUserSort.FIRST_NAME.getDefaultOrder(),
			TechnicalUserSort.ID, TechnicalUserSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			TechnicalUserSort.ID, TechnicalUserSort.ID.getDefaultOrder()
		)
	);

	public TechnicalUserDataProvider() {
		this(UnaryOperator.identity());
	}

	public TechnicalUserDataProvider(UnaryOperator<DataModel<TechnicalUserSearchQueryData>> dataModelOperator) {
		this(
			dataModelOperator.apply(
				new DataModel<>(TechnicalUserSearchQueryData::new)
					.bind(Bindings.technicalUserDtoSearch().term(), Model.of())
					.bind(Bindings.technicalUserDtoSearch().group(), new GenericEntityModel<>())
					.bind(Bindings.technicalUserDtoSearch().enabledFilter(), Model.of())
			)
		);
	}

	public TechnicalUserDataProvider(IModel<TechnicalUserSearchQueryData> dataModel) {
		super(dataModel);
	}

	@Override
	public CompositeSortModel<TechnicalUserSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected ITechnicalUserSearchQuery searchQuery() {
		return searchQuery;
	}

}
