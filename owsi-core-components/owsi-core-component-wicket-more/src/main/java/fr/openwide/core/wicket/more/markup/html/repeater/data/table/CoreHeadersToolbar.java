package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;

public class CoreHeadersToolbar<S extends ISort<?>> extends AbstractToolbar {

	private static final long serialVersionUID = 5382092664865344556L;
	
	private final DataTable<?, S> table;
	
	private final CompositeSortModel<S> sortModel;

	public <T> CoreHeadersToolbar(final DataTable<T, S> table, CompositeSortModel<S> sortModel) {
		super(table);
		this.table = table;
		this.sortModel = sortModel;

		RefreshingView<IColumn<T, S>> headers = new RefreshingView<IColumn<T, S>>("headers") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Iterator<IModel<IColumn<T, S>>> getItemModels() {
				List<IModel<IColumn<T, S>>> columnsModels = new LinkedList<IModel<IColumn<T, S>>>();
				
				for (IColumn<T, S> column : table.getColumns()) {
					columnsModels.add(Model.of(column));
				}
				
				return columnsModels.iterator();
			}

			@Override
			protected void populateItem(Item<IColumn<T, S>> item) {
				final IColumn<T, S> column = item.getModelObject();
				
				WebMarkupContainer header = null;
				if (column.isSortable()) {
					header = newSortableHeader("header", (ICoreColumn<?, S>) column);
				} else {
					header = new WebMarkupContainer("header");
				}

				if (column instanceof IStyledColumn) {
					header.add(new ClassAttributeAppender(new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;
						@Override
						public String getObject() {
							return ((IStyledColumn<?, ?>) column).getCssClass();
						}
						@Override
						public void detach() {
							column.detach();
						}
					}));
				}
				
				item.add(header);
				item.setRenderBodyOnly(true);
				header.add(column.getHeader("label"));
			}
		};
		add(headers);
	}

	/**
	 * Factory method for sortable header components. A sortable header component must have id of
	 * <code>headerId</code> and conform to markup specified in <code>HeadersToolbar.html</code>
	 * 
	 * @param headerId
	 *            header component id
	 * @param sortableColumn
	 *            column this header represents
	 * @return created header component
	 */
	protected WebMarkupContainer newSortableHeader(String headerId, IColumn<?, S> sortableColumn) {
		if (sortableColumn instanceof ICoreColumn) {
			return new CoreSortBorder<S>(headerId, table, sortModel, (ICoreColumn<?, S>) sortableColumn);
		} else {
			return new CoreSortBorder<S>(headerId, table, sortModel, sortableColumn);
		}
	}

}
