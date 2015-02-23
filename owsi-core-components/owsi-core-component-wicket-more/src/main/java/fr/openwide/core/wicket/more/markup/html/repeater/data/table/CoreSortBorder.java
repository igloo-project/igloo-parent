package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.border.Border;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;

/**
 * A component that wraps markup by adding a {@link TableSortLink}.
 */
public class CoreSortBorder<S extends ISort<?>> extends Border {

	private static final long serialVersionUID = -5734785070168027755L;

	public CoreSortBorder(final String id, DataTable<?, S> dataTable, CompositeSortModel<S> sortModel, IColumn<?, S> column) {
		super(id);
		
		addToBorder(newSortLink("sortLink", dataTable, sortModel, column));
	}

	public CoreSortBorder(final String id, DataTable<?, S> dataTable, CompositeSortModel<S> sortModel, ICoreColumn<?, S> column) {
		super(id);
		
		addToBorder(newSortLink("sortLink", dataTable, sortModel, column));
	}

	protected TableSortLink<S> newSortLink(String id, final DataTable<?, S> dataTable, CompositeSortModel<S> sortModel, IColumn<?, S> sortableColumn) {
		dataTable.setOutputMarkupId(true);
		return new TableSortLink<S>(id, sortModel, sortableColumn.getSortProperty(), dataTable) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refreshOnSort(AjaxRequestTarget target) {
				target.add(dataTable);
			}
		};
	}

	protected TableSortLink<S> newSortLink(String id, final DataTable<?, S> dataTable, CompositeSortModel<S> sortModel, ICoreColumn<?, S> sortableColumn) {
		dataTable.setOutputMarkupId(true);
		return new TableSortLink<S>(id, sortModel, sortableColumn.getSortProperty(), dataTable, sortableColumn.getSortTooltipTextModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refreshOnSort(AjaxRequestTarget target) {
				target.add(dataTable);
			}
		}
				.cycleMode(sortableColumn.getSortCycleMode())
				.iconStyle(sortableColumn.getSortIconStyle());
	}

}
