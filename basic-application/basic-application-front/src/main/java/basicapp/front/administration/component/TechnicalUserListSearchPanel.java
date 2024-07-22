package basicapp.front.administration.component;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.search.TechnicalUserSearchQueryData;
import basicapp.back.util.binding.Bindings;
import basicapp.front.administration.form.TechnicalUserAjaxDropDownSingleChoice;
import basicapp.front.administration.form.UserGroupDropDownSingleChoice;
import basicapp.front.administration.page.AdministrationTechnicalUserDetailPage;
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

public class TechnicalUserListSearchPanel extends Panel {

  private static final long serialVersionUID = -4624527265796845060L;

  private final IModel<TechnicalUser> quickAccessModel = new GenericEntityModel<>();

  public TechnicalUserListSearchPanel(
      String id, IPageable pageable, IModel<TechnicalUserSearchQueryData> dataModel) {
    super(id);

    add(
        new PageableSearchForm<>("form", pageable)
            .add(
                new TextField<String>(
                        "name",
                        BindingModel.of(dataModel, Bindings.technicalUserDtoSearch().term()))
                    .setLabel(new ResourceModel("business.user.name"))
                    .add(new LabelPlaceholderBehavior()),
                new UserGroupDropDownSingleChoice(
                        "userGroup",
                        BindingModel.of(dataModel, Bindings.technicalUserDtoSearch().group()))
                    .setLabel(new ResourceModel("business.user.group"))
                    .add(new LabelPlaceholderBehavior()),
                new EnumDropDownSingleChoice<>(
                        "enabledFilter",
                        BindingModel.of(
                            dataModel, Bindings.technicalUserDtoSearch().enabledFilter()),
                        EnabledFilter.class)
                    .setLabel(new ResourceModel("business.user.enabled.state"))
                    .add(new LabelPlaceholderBehavior()),
                new TechnicalUserAjaxDropDownSingleChoice("quickAccess", quickAccessModel)
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
                                        AdministrationTechnicalUserDetailPage.MAPPER
                                            .setParameter2(
                                                new ComponentPageModel(
                                                    TechnicalUserListSearchPanel.this))
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
