package basicapp.front.administration.model;

import java.util.function.UnaryOperator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import com.google.common.collect.ImmutableMap;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.search.BasicUserSearchQueryData;
import basicapp.back.business.user.search.BasicUserSort;
import basicapp.back.business.user.search.IBasicUserSearchQuery;
import basicapp.back.util.binding.Bindings;

public class BasicUserDataProvider extends SearchQueryDataProvider<BasicUser, BasicUserSort, BasicUserSearchQueryData, IBasicUserSearchQuery> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private IBasicUserSearchQuery searchQuery;

	private final CompositeSortModel<BasicUserSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			BasicUserSort.SCORE, BasicUserSort.SCORE.getDefaultOrder(),
			BasicUserSort.LAST_NAME, BasicUserSort.LAST_NAME.getDefaultOrder(),
			BasicUserSort.FIRST_NAME, BasicUserSort.FIRST_NAME.getDefaultOrder(),
			BasicUserSort.ID, BasicUserSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			BasicUserSort.ID, BasicUserSort.ID.getDefaultOrder()
		)
	);

	public BasicUserDataProvider() {
		this(UnaryOperator.identity());
	}

	public BasicUserDataProvider(UnaryOperator<DataModel<BasicUserSearchQueryData>> dataModelOperator) {
		this(
			dataModelOperator.apply(
				new DataModel<>(BasicUserSearchQueryData::new)
					.bind(Bindings.basicUserDtoSearch().term(), Model.of())
					.bind(Bindings.basicUserDtoSearch().group(), new GenericEntityModel<>())
					.bind(Bindings.basicUserDtoSearch().enabledFilter(), Model.of())
			)
		);
	}

	public BasicUserDataProvider(IModel<BasicUserSearchQueryData> dataModel) {
		super(dataModel);
	}

	@Override
	public CompositeSortModel<BasicUserSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected IBasicUserSearchQuery searchQuery() {
		return searchQuery;
	}

}
