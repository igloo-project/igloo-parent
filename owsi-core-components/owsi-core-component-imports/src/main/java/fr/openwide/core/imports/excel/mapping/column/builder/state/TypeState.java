package fr.openwide.core.imports.excel.mapping.column.builder.state;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.commons.util.functional.builder.function.generic.FunctionBuildStateSwitcher;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.IExcelImportColumnMapper;

public abstract class TypeState<TSheet, TRow, TCell, TCellReference> {
	
	public static final class DefaultNumericFormatSupplier implements Supplier<NumberFormat>, Serializable {
		private static final long serialVersionUID = 1L;
		@Override
		public NumberFormat get() {
			NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
			format.setRoundingMode(RoundingMode.HALF_UP);
			return format;
		}
	}
	
	private final AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference> columnSet;
	
	private final IExcelImportColumnMapper<TSheet, TRow, TCell, TCellReference> columnMapper;
	
	public TypeState(AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference> columnSet, IExcelImportColumnMapper<TSheet, TRow, TCell, TCellReference> columnMapper) {
		this.columnSet = columnSet;
		this.columnMapper = columnMapper;
	}
	
	public abstract IntegerState<TSheet, TRow, TCell, TCellReference> asInteger();
	
	public StringState<TSheet, TRow, TCell, TCellReference> asString() {
		return asString(new DefaultNumericFormatSupplier());
	}
	
	public abstract StringState<TSheet, TRow, TCell, TCellReference> asString(Supplier<? extends NumberFormat> formatIfNumeric);
	
	public abstract DateState<TSheet, TRow, TCell, TCellReference> asDate();
	
	protected class TypeStateSwitcher<T> implements FunctionBuildStateSwitcher
			<
			AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<T>,
			T,
			BooleanState<TSheet, TRow, TCell, TCellReference>,
			DateState<TSheet, TRow, TCell, TCellReference>,
			IntegerState<TSheet, TRow, TCell, TCellReference>,
			StringState<TSheet, TRow, TCell, TCellReference>
			> {
		
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
		public StringState<TSheet, TRow, TCell, TCellReference> toString(final Function<? super T, String> function) {
			final TypeStateSwitcher<String> switcher = newSwitcher(function, Predicates2.hasText());
			return new StringState<TSheet, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<String> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public IntegerState<TSheet, TRow, TCell, TCellReference> toInteger(Function<? super T, Integer> function) {
			final TypeStateSwitcher<Integer> switcher = newSwitcher(function);
			return new IntegerState<TSheet, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Integer> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public DateState<TSheet, TRow, TCell, TCellReference> toDate(Function<? super T, ? extends Date> function) {
			final TypeStateSwitcher<Date> switcher = newSwitcher(function);
			return new DateState<TSheet, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Date> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public BooleanState<TSheet, TRow, TCell, TCellReference> toBoolean(Function<? super T, Boolean> function) {
			final TypeStateSwitcher<Boolean> switcher = newSwitcher(function);
			return new BooleanState<TSheet, TRow, TCell, TCellReference>() {
				@Override
				protected TypeStateSwitcher<Boolean> getStateSwitcher() {
					return switcher;
				}
			};
		}

		@Override
		public AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<T> build() {
			return columnSet.new Column<T>(columnMapper, function, mandatoryValuePredicate);
		}
	}
}
