package basicapp.front.referencedata.component;

import static basicapp.front.common.util.CssClassConstants.CELL_DISPLAY_LG;

import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.search.CitySearchQueryData;
import basicapp.back.business.referencedata.search.CitySort;
import basicapp.back.util.binding.Bindings;
import basicapp.front.referencedata.model.CityDataProvider;
import org.apache.wicket.Component;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;

public class CityListPanel
    extends AbstractReferenceDataSimpleListPanel<
        City, CitySort, CitySearchQueryData, CityDataProvider> {

  private static final long serialVersionUID = -2165029373966420708L;

  public CityListPanel(String id) {
    super(id, new CityDataProvider());
  }

  @Override
  protected IColumnState<City, CitySort> addColumns(DataTableBuilder<City, CitySort> builder) {
    return super.addColumns(builder)
        .addLabelColumn(
            new ResourceModel("business.referenceData.label.fr"), Bindings.city().label().fr())
        .withSort(CitySort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
        .withClass("cell-w-300")
        .addLabelColumn(
            new ResourceModel("business.referenceData.label.en"), Bindings.city().label().en())
        .withSort(CitySort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
        .withClass("cell-w-300")
        .withClass(CELL_DISPLAY_LG)
        .addLabelColumn(
            new ResourceModel("business.city.postalCode.short"), Bindings.city().postalCode())
        .withSort(CitySort.POSTAL_CODE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
        .withClass("cell-w-120");
  }

  @Override
  protected Component createSearchForm(
      String wicketId,
      CityDataProvider dataProvider,
      DecoratedCoreDataTablePanel<City, CitySort> table) {
    return new CitySearchPanel(wicketId, dataProvider, table);
  }
}
