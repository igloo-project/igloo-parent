package basicapp.front.user.form;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.search.BasicUserSearchQueryData;
import basicapp.back.business.user.search.BasicUserSort;
import basicapp.back.business.user.search.IBasicUserSearchQuery;
import basicapp.front.user.renderer.UserRenderer;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class BasicUserAjaxDropDownSingleChoice
    extends GenericSelect2AjaxDropDownSingleChoice<BasicUser> {

  private static final long serialVersionUID = 7076114890845943476L;

  public BasicUserAjaxDropDownSingleChoice(String id, IModel<BasicUser> model) {
    this(id, model, new ChoiceProvider());
  }

  public BasicUserAjaxDropDownSingleChoice(
      String id,
      IModel<BasicUser> model,
      org.wicketstuff.select2.ChoiceProvider<BasicUser> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider extends AbstractLongIdGenericEntityChoiceProvider<BasicUser> {

    private static final long serialVersionUID = 1L;

    @SpringBean private IBasicUserSearchQuery searchQuery;

    public ChoiceProvider() {
      super(BasicUser.class, UserRenderer.get());
    }

    @Override
    protected void query(String term, int offset, int limit, Response<BasicUser> response) {
      BasicUserSearchQueryData data = new BasicUserSearchQueryData();
      data.setTerm(term);
      data.setEnabledFilter(EnabledFilter.ENABLED_ONLY);
      Map<BasicUserSort, SortOrder> sorts =
          ImmutableMap.of(
              BasicUserSort.SCORE, BasicUserSort.SCORE.getDefaultOrder(),
              BasicUserSort.LAST_NAME, BasicUserSort.LAST_NAME.getDefaultOrder(),
              BasicUserSort.FIRST_NAME, BasicUserSort.FIRST_NAME.getDefaultOrder(),
              BasicUserSort.ID, BasicUserSort.ID.getDefaultOrder());
      response.addAll(searchQuery.list(data, sorts, offset, limit));
    }
  }
}