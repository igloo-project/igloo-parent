package basicapp.front.user.form;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSearchQueryData;
import basicapp.back.business.user.search.UserSort;
import basicapp.front.user.renderer.UserRenderer;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Map;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownMultipleChoice;
import org.wicketstuff.select2.Response;

public class UserAjaxDropDownMultipleChoice<C extends Collection<User>>
    extends GenericSelect2AjaxDropDownMultipleChoice<User> {

  private static final long serialVersionUID = 7076114890845943476L;

  public UserAjaxDropDownMultipleChoice(
      String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier) {
    this(id, model, collectionSupplier, new ChoiceProvider(null));
  }

  public UserAjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      UserType userType,
      SerializableSupplier2<? extends C> collectionSupplier) {
    this(id, model, collectionSupplier, new ChoiceProvider(userType));
  }

  public UserAjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      org.wicketstuff.select2.ChoiceProvider<User> choiceProvider) {
    super(id, model, collectionSupplier, choiceProvider);
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
