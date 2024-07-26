package org.iglooproject.basicapp.web.application.administration.form;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.search.IUserSearchQuery;
import org.iglooproject.basicapp.web.application.common.renderer.UserRenderer;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownMultipleChoice;
import org.wicketstuff.select2.Response;

public class UserAjaxDropDownMultipleChoice<U extends User, C extends Collection<U>>
    extends GenericSelect2AjaxDropDownMultipleChoice<U> {

  private static final long serialVersionUID = 7076114890845943476L;

  public UserAjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      Class<U> targetTypeClass) {
    this(id, model, collectionSupplier, targetTypeClass, targetTypeClass);
  }

  public UserAjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      Class<U> targetTypeClass,
      Class<? extends U> searchTypeClass) {
    this(id, model, collectionSupplier, new ChoiceProvider<>(targetTypeClass, searchTypeClass));
  }

  public UserAjaxDropDownMultipleChoice(
      String id,
      IModel<C> model,
      SerializableSupplier2<? extends C> collectionSupplier,
      ChoiceProvider<U> choiceProvider) {
    super(id, model, collectionSupplier, choiceProvider);
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
