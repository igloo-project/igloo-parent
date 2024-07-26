package org.iglooproject.basicapp.web.application.administration.form;

import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.search.IUserGroupSearchQuery;
import org.iglooproject.basicapp.web.application.common.form.impl.UserGroupChoiceRenderer;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class UserGroupDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<UserGroup> {

  private static final long serialVersionUID = 8845987968858565378L;

  public UserGroupDropDownSingleChoice(String id, IModel<UserGroup> model) {
    super(id, model, new ChoicesModel(), UserGroupChoiceRenderer.get());
  }

  private static class ChoicesModel extends LoadableDetachableModel<List<UserGroup>> {

    private static final long serialVersionUID = 1L;

    @Override
    protected List<UserGroup> load() {
      return CoreWicketApplication.get()
          .getApplicationContext()
          .getBean(IUserGroupSearchQuery.class)
          .fullList();
    }
  }
}
