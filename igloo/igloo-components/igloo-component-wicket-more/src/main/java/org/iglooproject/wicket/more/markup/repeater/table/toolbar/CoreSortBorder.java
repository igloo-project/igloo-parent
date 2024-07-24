package org.iglooproject.wicket.more.markup.repeater.table.toolbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.border.Border;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.column.ICoreColumn;

/** A component that wraps markup by adding a {@link TableSortLink}. */
public class CoreSortBorder<S extends ISort<?>> extends Border {

  private static final long serialVersionUID = -5734785070168027755L;

  public CoreSortBorder(
      final String id,
      CoreDataTable<?, S> dataTable,
      CompositeSortModel<S> sortModel,
      IColumn<?, S> column) {
    super(id);

    addToBorder(newSortLink("sortLink", dataTable, sortModel, column));
  }

  public CoreSortBorder(
      final String id,
      CoreDataTable<?, S> dataTable,
      CompositeSortModel<S> sortModel,
      ICoreColumn<?, S> column) {
    super(id);

    addToBorder(newSortLink("sortLink", dataTable, sortModel, column));
  }

  protected TableSortLink<S> newSortLink(
      String id,
      final CoreDataTable<?, S> dataTable,
      CompositeSortModel<S> sortModel,
      IColumn<?, S> sortableColumn) {
    return new TableSortLink<S>(id, sortModel, sortableColumn.getSortProperty(), dataTable) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void refreshOnSort(AjaxRequestTarget target) {
        target.add(dataTable.getComponentToRefresh());
      }
    };
  }

  protected TableSortLink<S> newSortLink(
      String id,
      final CoreDataTable<?, S> dataTable,
      CompositeSortModel<S> sortModel,
      ICoreColumn<?, S> sortableColumn) {
    return new TableSortLink<S>(
        id,
        sortModel,
        sortableColumn.getSortProperty(),
        dataTable,
        sortableColumn.getSortTooltipTextModel()) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void refreshOnSort(AjaxRequestTarget target) {
        target.add(dataTable.getComponentToRefresh());
      }
    }.cycleMode(sortableColumn.getSortCycleMode()).iconStyle(sortableColumn.getSortIconStyle());
  }
}
