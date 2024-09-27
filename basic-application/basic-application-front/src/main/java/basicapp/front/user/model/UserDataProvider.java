package basicapp.front.user.model;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSearchQueryData;
import basicapp.back.business.user.search.UserSort;
import basicapp.back.util.binding.Bindings;
import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

public class UserDataProvider
    extends SearchQueryDataProvider<User, UserSort, UserSearchQueryData, IUserSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IUserSearchQuery searchQuery;

  private final CompositeSortModel<UserSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              UserSort.SCORE, UserSort.SCORE.getDefaultOrder(),
              UserSort.LAST_NAME, UserSort.LAST_NAME.getDefaultOrder(),
              UserSort.FIRST_NAME, UserSort.FIRST_NAME.getDefaultOrder(),
              UserSort.ID, UserSort.ID.getDefaultOrder()),
          ImmutableMap.of(UserSort.ID, UserSort.ID.getDefaultOrder()));

  public UserDataProvider() {
    this(UnaryOperator.identity());
  }

  public UserDataProvider(UnaryOperator<DataModel<UserSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(UserSearchQueryData::new)
                .bind(Bindings.userSearchQueryData().term(), Model.of())
                .bind(Bindings.userSearchQueryData().group(), new GenericEntityModel<>())
                .bind(Bindings.userSearchQueryData().enabledFilter(), Model.of())));
  }

  public UserDataProvider(IModel<UserSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<UserSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IUserSearchQuery searchQuery() {
    return searchQuery;
  }
}
