package basicapp.front.administration.form;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.search.ITechnicalUserSearchQuery;
import basicapp.back.business.user.search.TechnicalUserSearchQueryData;
import basicapp.back.business.user.search.TechnicalUserSort;
import basicapp.front.common.renderer.UserRenderer;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class TechnicalUserAjaxDropDownSingleChoice
    extends GenericSelect2AjaxDropDownSingleChoice<TechnicalUser> {

  private static final long serialVersionUID = 7076114890845943476L;

  public TechnicalUserAjaxDropDownSingleChoice(String id, IModel<TechnicalUser> model) {
    this(id, model, new ChoiceProvider());
  }

  public TechnicalUserAjaxDropDownSingleChoice(
      String id,
      IModel<TechnicalUser> model,
      org.wicketstuff.select2.ChoiceProvider<TechnicalUser> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider
      extends AbstractLongIdGenericEntityChoiceProvider<TechnicalUser> {

    private static final long serialVersionUID = 1L;

    @SpringBean private ITechnicalUserSearchQuery searchQuery;

    public ChoiceProvider() {
      super(TechnicalUser.class, UserRenderer.get());
    }

    @Override
    protected void query(String term, int offset, int limit, Response<TechnicalUser> response) {
      TechnicalUserSearchQueryData data = new TechnicalUserSearchQueryData();
      data.setTerm(term);
      data.setEnabledFilter(EnabledFilter.ENABLED_ONLY);
      Map<TechnicalUserSort, SortOrder> sorts =
          ImmutableMap.of(
              TechnicalUserSort.SCORE, TechnicalUserSort.SCORE.getDefaultOrder(),
              TechnicalUserSort.LAST_NAME, TechnicalUserSort.LAST_NAME.getDefaultOrder(),
              TechnicalUserSort.FIRST_NAME, TechnicalUserSort.FIRST_NAME.getDefaultOrder(),
              TechnicalUserSort.ID, TechnicalUserSort.ID.getDefaultOrder());
      response.addAll(searchQuery.list(data, sorts, offset, limit));
    }
  }
}
