package basicapp.front.user.form;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSearchQueryData;
import basicapp.back.business.user.search.UserSort;
import basicapp.front.user.renderer.UserRenderer;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class UserAjaxDropDownSingleChoice extends GenericSelect2AjaxDropDownSingleChoice<User> {

  private static final long serialVersionUID = 7076114890845943476L;

  public UserAjaxDropDownSingleChoice(String id, IModel<User> model) {
    this(id, model, new ChoiceProvider(null));
  }

  public UserAjaxDropDownSingleChoice(String id, IModel<User> model, UserType userType) {
    this(id, model, new ChoiceProvider(userType));
  }

  public UserAjaxDropDownSingleChoice(
      String id, IModel<User> model, org.wicketstuff.select2.ChoiceProvider<User> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider extends AbstractLongIdGenericEntityChoiceProvider<User> {

    private static final long serialVersionUID = 1L;

    @SpringBean private IUserSearchQuery searchQuery;

    private final UserType userType;

    public ChoiceProvider(UserType userType) {
      super(User.class, UserRenderer.get());
      this.userType = userType;
      Injector.get().inject(this);
    }

    @Override
    protected void query(String term, int offset, int limit, Response<User> response) {
      UserSearchQueryData data = new UserSearchQueryData();
      data.setTerm(term);
      if (userType != null) {
        data.setType(userType);
      }
      data.setActive(EnabledFilter.ENABLED_ONLY);
      Map<UserSort, SortOrder> sorts =
          ImmutableMap.of(
              UserSort.SCORE, UserSort.SCORE.getDefaultOrder(),
              UserSort.LAST_NAME, UserSort.LAST_NAME.getDefaultOrder(),
              UserSort.FIRST_NAME, UserSort.FIRST_NAME.getDefaultOrder(),
              UserSort.ID, UserSort.ID.getDefaultOrder());
      response.addAll(searchQuery.list(data, sorts, offset, limit));
    }
  }
}
