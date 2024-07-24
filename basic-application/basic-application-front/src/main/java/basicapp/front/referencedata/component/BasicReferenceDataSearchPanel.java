package basicapp.front.referencedata.component;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.search.BasicReferenceDataSort;
import basicapp.back.util.binding.Bindings;
import basicapp.front.referencedata.model.BasicReferenceDataDataProvider;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;

public class BasicReferenceDataSearchPanel<T extends ReferenceData<? super T>> extends Panel {

  private static final long serialVersionUID = 3027788723051745121L;

  public BasicReferenceDataSearchPanel(
      String id,
      BasicReferenceDataDataProvider<T> dataProvider,
      DecoratedCoreDataTablePanel<T, BasicReferenceDataSort> table) {
    super(id);

    PageableSearchForm<Void> form = new PageableSearchForm<>("form", table);
    add(form);

    form.add(
        new AjaxFormSubmitBehavior(form, StateEvent.CHANGE.getEventLabel()) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            // Just in case the dataProvider's content was loaded before search parameters changed
            dataProvider.detach();
            target.add(table);
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });

    form.add(
        new TextField<>(
                "label",
                BindingModel.of(
                    dataProvider.getDataModel(),
                    Bindings.basicReferenceDataSearchQueryData().label()),
                String.class)
            .setLabel(new ResourceModel("business.referenceData.label"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "enabledFilter",
                BindingModel.of(
                    dataProvider.getDataModel(),
                    Bindings.basicReferenceDataSearchQueryData().enabledFilter()),
                EnabledFilter.class)
            .setLabel(new ResourceModel("business.referenceData.enabled.state"))
            .add(new LabelPlaceholderBehavior()));
  }
}
