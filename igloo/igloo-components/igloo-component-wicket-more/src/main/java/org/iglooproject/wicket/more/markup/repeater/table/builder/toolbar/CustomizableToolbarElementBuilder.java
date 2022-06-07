package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.factory.IOneParameterComponentFactory;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class CustomizableToolbarElementBuilder<T, S extends ISort<?>>
		implements IOneParameterComponentFactory<Component, CoreDataTable<T, S>> {

	private static final long serialVersionUID = 8327298869880437772L;

	private final IOneParameterComponentFactory<Component, CoreDataTable<T, S>> factory;

	private final int colspanAccumulation;

	private Integer colspan = 1;

	private final Set<String> cssClasses = Sets.newHashSet();

	private Condition condition = Condition.alwaysTrue();

	public CustomizableToolbarElementBuilder(int colspanAccumulation, IOneParameterComponentFactory<Component, CoreDataTable<T, S>> factory) {
		super();
		this.colspanAccumulation = checkNotNull(colspanAccumulation);
		this.factory = checkNotNull(factory);
	}

	@Override
	public Component create(String wicketId, final CoreDataTable<T, S> dataTable) {
		IModel<Integer> computedColspanModel = new LoadableDetachableModel<Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Integer load() {
				if (colspan == null) {
					dataTable.configure(); // Update the displayed columns
					return dataTable.getDisplayedColumns().size() - colspanAccumulation;
				}
				
				Map<IColumn<T, S>, Condition> columnToConditionMap = dataTable.getColumnToConditionMap();
				
				FluentIterable<Entry<IColumn<T, S>, Condition>> columnEntryIterable =
						FluentIterable.from(columnToConditionMap .entrySet())
						.skip(colspanAccumulation);
				
				// Make sure we don't span further than the table width
				int cappedColspan;
				int dataTableColumnsSize = columnToConditionMap.size();
				if (colspanAccumulation + colspan > dataTableColumnsSize) {
					cappedColspan = dataTableColumnsSize - colspanAccumulation;
				} else {
					cappedColspan = colspan;
				}
				
				// Make sure we don't span further than the column width
				columnEntryIterable = columnEntryIterable.limit(cappedColspan);
				
				int colspanWithoutHiddenColumns = cappedColspan;
				for (Entry<IColumn<T, S>, Condition> column : columnEntryIterable) {
					Condition columnCondition = column.getValue();
					if (columnCondition != null && !columnCondition.applies()) {
						--colspanWithoutHiddenColumns;
					}
				}
				
				return Math.max(colspanWithoutHiddenColumns, 0);
			}
		};
		
		return factory.create(wicketId, dataTable)
				.add(
						new AttributeModifier("colspan", computedColspanModel),
						condition.and(Condition.predicate(computedColspanModel, Predicates2.from(Range.atLeast(1)))).thenShow(),
						new AttributeModifier("class", Joiner.on(" ").join(cssClasses))
				);
	}

	public Integer getColspan() {
		return colspan;
	}

	public void colspan(Integer colspan) {
		this.colspan = colspan;
	}

	public void full() {
		this.colspan = null;
	}

	public void when(Condition condition) {
		this.condition = condition;
	}

	public void withClass(String cssClass) {
		this.cssClasses.add(cssClass);
	}

	@Override
	public void detach() {
		Detachables.detach(factory, condition);
	}

}
