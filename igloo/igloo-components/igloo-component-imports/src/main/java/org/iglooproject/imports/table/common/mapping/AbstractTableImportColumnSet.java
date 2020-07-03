package org.iglooproject.imports.table.common.mapping;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.TableImportEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.exception.TableImportContentException;
import org.iglooproject.imports.table.common.event.exception.TableImportMappingException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.location.TableImportLocation;
import org.iglooproject.imports.table.common.location.TableImportLocationContext;
import org.iglooproject.imports.table.common.mapping.column.ITableImportColumnDefinition;
import org.iglooproject.imports.table.common.mapping.column.ITableImportColumnDefinition.IMappedExcelImportColumnDefinition;
import org.iglooproject.imports.table.common.mapping.column.MappedTableImportColumnDefinitionImpl;
import org.iglooproject.imports.table.common.mapping.column.builder.AbstractTableImportColumnBuilder;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import org.iglooproject.imports.table.common.mapping.column.builder.MappingConstraint;
import org.iglooproject.imports.table.common.mapping.column.builder.state.TypeState;

import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Streams;

/**
 * The central class of this Excel import framework.
 * See TestApachePoiExcelImporter for an example on how to use this class.
 * @author yrodiere
 */
public abstract class AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> {
	private static final Comparator<? super String> DEFAULT_HEADER_LABEL_COLLATOR;
	static {
		Collator collator = Collator.getInstance(Locale.ROOT);
		collator.setStrength(Collator.IDENTICAL);
		DEFAULT_HEADER_LABEL_COLLATOR = Ordering.from(collator).nullsFirst();
	}
	
	private final Comparator<? super String> defaultHeaderLabelCollator;
	
	private final AbstractTableImportColumnBuilder<TTable, TRow, TCell, TCellReference> builder;
	
	private final Collection<Column<?>> columns = Lists.newArrayList();
	
	public AbstractTableImportColumnSet(AbstractTableImportColumnBuilder<TTable, TRow, TCell, TCellReference> builder) {
		this(builder, DEFAULT_HEADER_LABEL_COLLATOR);
	}
	
	public AbstractTableImportColumnSet(AbstractTableImportColumnBuilder<TTable, TRow, TCell, TCellReference> builder, Comparator<? super String> defaultHeaderLabelCollator) {
		super();
		this.builder = builder;
		this.defaultHeaderLabelCollator = defaultHeaderLabelCollator;
	}

	public final TypeState<TTable, TRow, TCell, TCellReference> withHeader(String headerLabel) {
		return withHeader(headerLabel, MappingConstraint.REQUIRED);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withHeader(String headerLabel, Comparator<? super String> collator) {
		return withHeader(headerLabel, collator, 0, MappingConstraint.REQUIRED);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withHeader(String headerLabel, MappingConstraint mappingConstraint) {
		return withHeader(headerLabel, 0, mappingConstraint);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withHeader(String headerLabel, int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		return withHeader(headerLabel, defaultHeaderLabelCollator, indexAmongMatchedColumns, mappingConstraint);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withHeader(String headerLabel, Equivalence<? super String> headerEquivalence, int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		return builder.withHeader(this, headerLabel, Predicates2.from(headerEquivalence.equivalentTo(headerLabel)), indexAmongMatchedColumns, mappingConstraint);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withHeader(String headerLabel, Comparator<? super String> collator, int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		return builder.withHeader(this, headerLabel, Predicates2.comparesEqualTo(headerLabel, collator), indexAmongMatchedColumns, mappingConstraint);
	}
	
	public final TypeState<TTable, TRow, TCell, TCellReference>  withIndex(int index) {
		return builder.withIndex(this, index);
	}
	
	/**
	 * The actual column implementation.
	 * <p>This class is implemented as an inner class in order to get rid of the <TSheet, TRow, TCellReference, TCell, TValue> generic
	 * parameters when the client references columns.
	 */
	public class Column<TValue> implements ITableImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> {
		private final ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> mapper;
		
		private final Function2<? super TCell, ? extends TValue> cellToValueFunction;
		
		private final Predicate2<? super TValue> mandatoryValuePredicate;

		public Column(ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> mapper,
				Function2<? super TCell, ? extends TValue> cellToValueFunction, Predicate2<? super TValue> mandatoryValuePredicate) {
			super();
			this.mapper = mapper;
			this.cellToValueFunction = cellToValueFunction;
			this.mandatoryValuePredicate = mandatoryValuePredicate;
			
			// Register the new column
			AbstractTableImportColumnSet.this.columns.add(this);
		}

		@Override
		public IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> map(TTable sheet, ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator,
				ITableImportEventHandler eventHandler) throws TableImportMappingException {
			Function2<? super TRow, ? extends TCellReference> rowToCellReferenceFunction = mapper.tryMap(sheet, navigator, eventHandler);
			return new MappedTableImportColumnDefinitionImpl<>(sheet, rowToCellReferenceFunction, navigator, cellToValueFunction, mandatoryValuePredicate);
		}
	}
	
	public final TableContext map(TTable sheet, ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator, ITableImportEventHandler eventHandler) throws TableImportMappingException {
		return new TableContext(sheet, navigator, eventHandler);
	}
	
	public class TableContext extends TableImportLocationContext implements Iterable<RowContext> {
		
		private final TTable table;
		private final ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator;
		
		private final Map<Column<?>, IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, ?>> mappings;
		
		private TableContext(TTable table, ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator, ITableImportEventHandler eventHandler)
				throws TableImportMappingException {
			super(eventHandler);
			Validate.notNull(table, "table must not be null");
			
			this.table = table;
			this.navigator = navigator;

			Map<Column<?>, IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, ?>> mutableMappings = Maps.newHashMap();
			for (Column<?> columnDefinition : columns) {
				mutableMappings.put(columnDefinition, columnDefinition.map(table, navigator, eventHandler));
			}
			this.mappings = Collections.unmodifiableMap(mutableMappings);
			
			this.eventHandler.checkNoMappingErrorOccurred();
		}
		
		public TTable getTable() {
			return table;
		}
		
		@Override
		public Iterator<RowContext> iterator() {
			return toRowContexts(navigator.rows(table));
		}
		
		protected Iterator<RowContext> toRowContexts(Iterator<TRow> rows) {
			return Streams.stream(rows)
					.map(input -> row(input))
					.iterator();
		}
		
		public Iterable<RowContext> nonEmptyRows() {
			return new Iterable<RowContext>() {
				@Override
				public Iterator<RowContext> iterator() {
					return toRowContexts(navigator.nonEmptyRows(table));
				}
			};
		}

		@SuppressWarnings("unchecked")
		private <TValue> IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> getMappedColumn(
				ITableImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> columnDefinition) {
			IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> mappedColumn =
					(IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue>) mappings.get(columnDefinition);
			if (mappedColumn == null) {
				throw new IllegalStateException("Column " + columnDefinition
						+ " was not properly registered, hence it has not been mapped. Please use AbstractColumns.add() before using AbstractColumns.newMapping().");
			}
			return mappedColumn;
		}
		
		public RowContext row(TRow row) {
			return new RowContext(this, this.eventHandler, row);
		}
		
		public <TValue> ColumnContext<TValue> column(Column<TValue> columnDefinition) {
			return new ColumnContext<>(this, columnDefinition);
		}
		
		public <TValue> CellContext<TValue> cell(TRow row, Column<TValue> columnDefinition) {
			return row(row).cell(columnDefinition);
		}
		
		/**
		 * @see TableImportLocationContext The event recording methods error(), info(), etc. defined in the superclass.
		 */
		@Override
		public TableImportLocation getLocation() {
			return navigator.getLocation(table, null, null);
		}
		
		public void event(ExcelImportErrorEvent event, String message, TRow row, Object ... args) throws TableImportContentException {
			event(event, message, row, null, (Object[])args);
		}
		
		public void event(ExcelImportInfoEvent event, String message, TRow row, Object ... args) {
			event(event, message, row, null, (Object[])args);
		}
		
		public void event(ExcelImportErrorEvent event, String message, TRow row, TCellReference cellReference, Object ... args) throws TableImportContentException {
			eventHandler.event(event, navigator.getLocation(table, row, cellReference), message, (Object[])args);
		}
		
		public void event(ExcelImportInfoEvent event, String message, TRow row, TCellReference cellReference, Object ... args) {
			eventHandler.event(event, navigator.getLocation(table, row, cellReference), message, (Object[])args);
		}
	}
	
	public final class RowContext extends TableImportLocationContext {
		
		private final TableContext tableContext;
		private final TRow row;

		private RowContext(TableContext sheetContext, ITableImportEventHandler eventHandler, TRow row) {
			super(eventHandler);
			this.tableContext = sheetContext;
			this.row = row;
		}
		
		public boolean hasContent() {
			return tableContext.navigator.rowHasContent(row);
		}

		public <TValue> CellContext<TValue> cell(Column<TValue> columnDefinition) {
			return new CellContext<>(this, this.eventHandler, tableContext.getMappedColumn(columnDefinition));
		}
		
		/**
		 * @see TableImportLocationContext The event recording methods error(), info(), etc. defined in the superclass.
		 */
		@Override
		public TableImportLocation getLocation() {
			return tableContext.navigator.getLocation(tableContext.table, row, null);
		}
		
		public void event(ExcelImportErrorEvent event, String message, TCellReference cellReference, Object ... args) throws TableImportContentException {
			tableContext.event(event, message, row, cellReference, (Object[])args);
		}
		
		public void event(ExcelImportInfoEvent event, String message, TCellReference cellReference, Object ... args) {
			tableContext.event(event, message, row, cellReference, (Object[])args);
		}
	}
	
	public final class ColumnContext<TValue> {
		
		private final TableContext tableContext;
		private final Column<TValue> columnDefinition;

		private ColumnContext(TableContext sheetContext, Column<TValue> columnDefinition) {
			super();
			this.tableContext = sheetContext;
			this.columnDefinition = columnDefinition;
		}

		public CellContext<TValue> cell(TRow row) {
			return tableContext.row(row).cell(columnDefinition);
		}
		
		public boolean exists() {
			return tableContext.getMappedColumn(columnDefinition).isBound();
		}
	}
	
	public final class CellContext<T> extends TableImportLocationContext {

		private final RowContext rowContext;
		private final IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, T> mappedColumn;

		private CellContext(RowContext rowContext, ITableImportEventHandler eventHandler,
				IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, T> mappedColumn) {
			super(eventHandler);
			this.rowContext = rowContext;
			this.mappedColumn = mappedColumn;
		}

		public T get() {
			return mappedColumn.getValue(rowContext.row);
		}

		public T getMandatory(String message, Object ... args) throws TableImportContentException {
			return getMandatory(TableImportEvent.ERROR, message, (Object[])args);
		}

		public T getMandatory(ExcelImportErrorEvent event, String message, Object ... args) throws TableImportContentException {
			T value = mappedColumn.getMandatoryValue(rowContext.row);
			if (value == null) {
				event(event, message, (Object[])args);
			}
			return value;
		}

		public T getMandatory(ExcelImportInfoEvent event, String message, Object ... args) {
			T value = mappedColumn.getMandatoryValue(rowContext.row);
			if (value == null) {
				event(event, message, (Object[])args);
			}
			return value;
		}

		public boolean hasContent() {
			return mappedColumn.hasContent(rowContext.row);
		}

		/**
		 * @see TableImportLocationContext The event recording methods error(), info(), etc. defined in the superclass.
		 */
		@Override
		public TableImportLocation getLocation() {
			return rowContext.tableContext.navigator.getLocation(rowContext.tableContext.table, rowContext.row, mappedColumn.getCellReference(rowContext.row));
		}
	}
}
