package basicapp.front.user.panel;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.search.BasicUserSearchQueryData;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.form.BasicUserAjaxDropDownSingleChoice;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.usergroup.form.UserGroupDropDownSingleChoice;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.model.ComponentPageModel;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class BasicUserListSearchPanel extends Panel {

  private static final long serialVersionUID = -4624527265796845060L;

  private final IModel<BasicUser> quickAccessModel = new GenericEntityModel<>();

  public BasicUserListSearchPanel(
      String id, IPageable pageable, IModel<BasicUserSearchQueryData> dataModel) {
    super(id);

    add(
        new PageableSearchForm<>("form", pageable)
            .add(
                new TextField<String>(
                        "name", BindingModel.of(dataModel, Bindings.basicUserDtoSearch().term()))
                    .setLabel(new ResourceModel("business.user.name"))
                    .add(new LabelPlaceholderBehavior()),
                new UserGroupDropDownSingleChoice(
                        "userGroup",
                        BindingModel.of(dataModel, Bindings.basicUserDtoSearch().group()))
                    .setLabel(new ResourceModel("business.user.group"))
                    .add(new LabelPlaceholderBehavior()),
                new EnumDropDownSingleChoice<>(
                        "enabledFilter",
                        BindingModel.of(dataModel, Bindings.basicUserDtoSearch().enabledFilter()),
                        EnabledFilter.class)
                    .setLabel(new ResourceModel("business.user.enabled.state"))
                    .add(new LabelPlaceholderBehavior()),
                new BasicUserAjaxDropDownSingleChoice("quickAccess", quickAccessModel)
                    .setLabel(new ResourceModel("common.quickAccess"))
                    .add(new LabelPlaceholderBehavior())
                    .add(
                        new UpdateOnChangeAjaxEventBehavior()
                            .onChange(
                                new SerializableListener() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void onBeforeRespond(
                                      Map<String, Component> map, AjaxRequestTarget target) {
                                    IPageLinkDescriptor linkDescriptor =
                                        BasicUserDetailPage.MAPPER
                                            .setParameter2(
                                                new ComponentPageModel(
                                                    BasicUserListSearchPanel.this))
                                            .map(
                                                new GenericEntityModel<>(
                                                    quickAccessModel.getObject()));

                                    quickAccessModel.setObject(null);
                                    quickAccessModel.detach();

                                    if (linkDescriptor.isAccessible()) {
                                      throw linkDescriptor.newRestartResponseException();
                                    }
                                  }
                                }))));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(quickAccessModel);
  }
}
