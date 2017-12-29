package org.iglooproject.imports.table.common.mapping.column.builder.state;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;

import org.iglooproject.commons.util.functional.Predicates2;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;

public abstract class TypeState<TTable, TRow, TCell, TCellReference> {
	
	public static final class DefaultNumericFormatSupplier implements Supplier<NumberFormat>, Serializable {
		private static final long serialVersionUID = 1L;
		@Override
		public NumberFormat get() {
			NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
			format.setGroupingUsed(false);
			format.setRoundingMode(RoundingMode.HALF_UP);
			return format;
		}
	}
	
	private final AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet;
	
	private final ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> columnMapper;
	
	public TypeState(AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet, ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> columnMapper) {
		this.columnSet = columnSet;
		this.columnMapper = columnMapper;
	}
	
	public abstract IntegerState<TTable, TRow, TCell, TCellReference> asInteger();
	
	public abstract LongState<TTable, TRow, TCell, TCellReference> asLong();
	
	public abstract DoubleState<TTable, TRow, TCell, TCellReference> asDouble();
	
	public abstract BigDecimalState<TTable, TRow, TCell, TCellReference> asBigDecimal();
	
	public StringState<TTable, TRow, TCell, TCellReference> asString() {
		return asString(new DefaultNumericFormatSupplier());
	}
	
	public abstract StringState<TTable, TRow, TCell, TCellReference> asString(Supplier<? extends NumberFormat> formatIfNumeric);
	
	public abstract DateState<TTable, TRow, TCell, TCellReference> asDate();
	
	protected class TypeStateSwitcher<T> implements ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, T> {
		
		private final Function<? super TCell, ? extends T> function;
		
		private final Predicate<? super T> mandatoryValuePredicate;
		
		public TypeStateSwitcher(Function<? super TCell, ? extends T> function) {
			this(function, Predicates.notNull());
		}
		
		public TypeStateSwitcher(Function<? super TCell , ? extends T> function, Predicate<? super T> mandatoryValuePredicate) {
			super();
			this.function = function;
			this.mandatoryValuePredicate = mandatoryValuePredicate;
		}

		private <T2> TypeStateSwitcher<T2> newSwitcher(Function<? super T, ? extends T2> function2) {
			return new TypeStateSwitcher<T2>(Functions.compose(function2, function));
		}

		private <T2> TypeStateSwitcher<T2> newSwitcher(Function<? super T, ? extends T2> function2, Predicate<? super T2> mandatoryValuePredicate) {
			return new TypeStateSwitcher<T2>(Functions.compose(function2, function), mandatoryValuePredicate);
		}

		@Override
		public StringState<TTable, TRow, TCell, TCellReference> toString(final Function<? super T, String> function) {
			final TypeStateSwitcher<String> switcher = newSwitcher(function, Predicates2.hasText());
			return new StringState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<String> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public IntegerState<TTable, TRow, TCell, TCellReference> toInteger(Function<? super T, Integer> function) {
			final TypeStateSwitcher<Integer> switcher = newSwitcher(function);
			return new IntegerState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Integer> getStateSwitcher() {
					return switcher;
				}
			};
		}
		
		@Override
		public LongState<TTable, TRow, TCell, TCellReference> toLong(Function<? super T, Long> function) {
			final TypeStateSwitcher<Long> switcher = newSwitcher(function);
			return new LongState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Long> getStateSwitcher() {
					return switcher;
				}
			};
		}
		
		@Override
		public DoubleState<TTable, TRow, TCell, TCellReference> toDouble(Function<? super T, Double> function) {
			final TypeStateSwitcher<Double> switcher = newSwitcher(function);
			return new DoubleState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Double> getStateSwitcher() {
					return switcher;
				}
			};
		}
		
		@Override
		public BigDecimalState<TTable, TRow, TCell, TCellReference> toBigDecimal(Function<? super T, BigDecimal> function) {
			final TypeStateSwitcher<BigDecimal> switcher = newSwitcher(function);
			return new BigDecimalState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<BigDecimal> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public DateState<TTable, TRow, TCell, TCellReference> toDate(Function<? super T, ? extends Date> function) {
			final TypeStateSwitcher<Date> switcher = newSwitcher(function);
			return new DateState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Date> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public BooleanState<TTable, TRow, TCell, TCellReference> toBoolean(Function<? super T, Boolean> function) {
			final TypeStateSwitcher<Boolean> switcher = newSwitcher(function);
			return new BooleanState<TTable, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Boolean> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> toGeneric(Function<? super T, TValue> function) {
			final TypeStateSwitcher<TValue> switcher = newSwitcher(function);
			return new GenericState<TTable, TRow, TCell, TCellReference, TValue>() {
				@Override
				protected TypeStateSwitcher<TValue> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<T> build() {
			return columnSet.new Column<T>(columnMapper, function, mandatoryValuePredicate);
		}
	}
}
