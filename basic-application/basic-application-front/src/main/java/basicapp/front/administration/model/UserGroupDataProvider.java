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

import basicapp.back.business.user.model.UserGroup;
import basicapp.back.business.user.search.IUserGroupSearchQuery;
import basicapp.back.business.user.search.UserGroupSearchQueryData;
import basicapp.back.business.user.search.UserGroupSort;
import basicapp.back.util.binding.Bindings;

public class UserGroupDataProvider extends SearchQueryDataProvider<UserGroup, UserGroupSort, UserGroupSearchQueryData, IUserGroupSearchQuery> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private IUserGroupSearchQuery searchQuery;

	private final CompositeSortModel<UserGroupSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			UserGroupSort.SCORE, UserGroupSort.SCORE.getDefaultOrder(),
			UserGroupSort.NAME, UserGroupSort.NAME.getDefaultOrder(),
			UserGroupSort.ID, UserGroupSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			UserGroupSort.ID, UserGroupSort.ID.getDefaultOrder()
		)
	);

	public UserGroupDataProvider() {
		this(UnaryOperator.identity());
	}

	public UserGroupDataProvider(UnaryOperator<DataModel<UserGroupSearchQueryData>> dataModelOperator) {
		this(
			dataModelOperator.apply(
				new DataModel<>(UserGroupSearchQueryData::new)
					.bind(Bindings.userGroupSearchQueryData().name(), Model.of())
					.bind(Bindings.userGroupSearchQueryData().user(), new GenericEntityModel<>())
					.bind(Bindings.userGroupSearchQueryData().basicUser(), new GenericEntityModel<>())
					.bind(Bindings.userGroupSearchQueryData().technicalUser(), new GenericEntityModel<>())
			)
		);
	}

	public UserGroupDataProvider(IModel<UserGroupSearchQueryData> dataModel) {
		super(dataModel);
	}

	@Override
	public CompositeSortModel<UserGroupSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected IUserGroupSearchQuery searchQuery() {
		return searchQuery;
	}

}
