package basicapp.front.administration.component;

import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import basicapp.front.history.column.HistoryLogDetailColumn;
import basicapp.front.history.model.HistoryLogDataProvider;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;

public class UserDetailHistoryLogPanel extends GenericPanel<User> {

  private static final long serialVersionUID = -5322394816999794266L;

  @SpringBean private IPropertyService propertyService;

  public UserDetailHistoryLogPanel(String id, final IModel<? extends User> userModel) {
    super(id, userModel);
    setOutputMarkupPlaceholderTag(true);

    HistoryLogDataProvider dataProvider =
        new HistoryLogDataProvider(
            dataModel ->
                dataModel.bind(
                    Bindings.historyLogSearchQueryData().allObjects(),
                    LoadableDetachableModel.of(
                        () ->
                            HistoryEntityReference.from(
                                GenericEntityReference.of(userModel.getObject())))));
    dataProvider
        .getDataModel()
        .getObject()
        .addMandatoryDifferencesEventType(HistoryEventType.UPDATE);

    add(
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(
                new ResourceModel("business.history.date"), Bindings.historyLog().date())
            .withSort(HistoryLogSort.DATE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-150")
            .addLabelColumn(
                new ResourceModel("business.history.subject"), Bindings.historyLog().subject())
            .withClass("cell-w-250")
            .addColumn(new HistoryLogDetailColumn())
            .withClass("cell-w-500")
            .bootstrapCard()
            .title("administration.user.detail.audits")
            .ajaxPager(AddInPlacement.FOOTER_RIGHT)
            .build("history", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE)));
  }
}
