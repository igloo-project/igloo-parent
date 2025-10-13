package basicapp.front.referencedata.component;

import static basicapp.front.common.util.CssClassConstants.CELL_DISPLAY_MD;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.search.BasicReferenceDataSearchQueryData;
import basicapp.back.business.referencedata.search.BasicReferenceDataSort;
import basicapp.back.util.binding.Bindings;
import basicapp.front.referencedata.model.BasicReferenceDataDataProvider;
import basicapp.front.referencedata.popup.AbstractReferenceDataPopup;
import basicapp.front.referencedata.popup.BasicReferenceDataPopup;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;

public class BasicReferenceDataListPanel<T extends ReferenceData<? super T>>
    extends AbstractReferenceDataListPanel<
        T,
        BasicReferenceDataSort,
        BasicReferenceDataSearchQueryData<T>,
        BasicReferenceDataDataProvider<T>> {

  private static final long serialVersionUID = -4026683202098875499L;

  private final Class<T> clazz;

  public BasicReferenceDataListPanel(String id, SerializableSupplier2<T> supplier, Class<T> clazz) {
    super(id, new BasicReferenceDataDataProvider<>(clazz), supplier);
    this.clazz = clazz;
    setOutputMarkupId(true);
  }

  @Override
  protected AbstractReferenceDataPopup<T> createPopup(String wicketId) {
    return new BasicReferenceDataPopup<>(wicketId, clazz) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit(T referenceData, AjaxRequestTarget target) {
        super.onSubmit(referenceData, target);
        target.add(results);
      }
    };
  }

  @Override
  protected IColumnState<T, BasicReferenceDataSort> addColumns(
      DataTableBuilder<T, BasicReferenceDataSort> builder) {
    return super.addColumns(builder)
        .addLabelColumn(
            new ResourceModel("business.referenceData.label.fr"),
            Bindings.referenceData().label().fr())
        .withSort(
            BasicReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-300")
        .addLabelColumn(
            new ResourceModel("business.referenceData.label.en"),
            Bindings.referenceData().label().en())
        .withSort(
            BasicReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-300")
        .withClass(CELL_DISPLAY_MD);
  }

  @Override
  protected Component createSearchForm(
      String wicketId,
      BasicReferenceDataDataProvider<T> dataProvider,
      DecoratedCoreDataTablePanel<T, BasicReferenceDataSort> table) {
    return new BasicReferenceDataSearchPanel<>(wicketId, dataProvider, table);
  }
}
