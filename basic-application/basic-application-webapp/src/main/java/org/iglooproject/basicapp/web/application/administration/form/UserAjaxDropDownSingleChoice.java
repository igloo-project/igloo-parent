package org.iglooproject.basicapp.web.application.administration.form;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.search.IUserSearchQuery;
import org.iglooproject.basicapp.web.application.common.renderer.UserRenderer;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class UserAjaxDropDownSingleChoice<U extends User>
    extends GenericSelect2AjaxDropDownSingleChoice<U> {

  private static final long serialVersionUID = 7076114890845943476L;

  public UserAjaxDropDownSingleChoice(String id, IModel<U> model, Class<U> targetTypeClass) {
    this(id, model, targetTypeClass, targetTypeClass);
  }

  public UserAjaxDropDownSingleChoice(
      String id, IModel<U> model, Class<U> targetTypeClass, Class<? extends U> searchTypeClass) {
    this(id, model, new ChoiceProvider<>(targetTypeClass, searchTypeClass));
  }

  public UserAjaxDropDownSingleChoice(
      String id, IModel<U> model, ChoiceProvider<U> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider<U extends User>
      extends AbstractLongIdGenericEntityChoiceProvider<U> {

    private static final long serialVersionUID = 1L;

    private final Class<? extends U> searchTypeClass;

    public ChoiceProvider(Class<U> targetTypeClass, Class<? extends U> searchTypeClass) {
      super(targetTypeClass, UserRenderer.get());
      this.searchTypeClass = searchTypeClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void query(String term, int offset, int limit, Response<U> response) {
      response.addAll(
          getBean(IUserSearchQuery.class, searchTypeClass)
              .nameAutocomplete(term)
              .list(offset, limit));
    }
  }
}
