package org.iglooproject.imports.table.common.mapping.column.builder.state;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;

public abstract class TypeState<TTable, TRow, TCell, TCellReference> {

  public static final class DefaultNumericFormatSupplier
      implements SerializableSupplier2<NumberFormat> {
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

  public TypeState(
      AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet,
      ITableImportColumnMapper<TTable, TRow, TCell, TCellReference> columnMapper) {
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

  public abstract StringState<TTable, TRow, TCell, TCellReference> asString(
      Supplier2<? extends NumberFormat> formatIfNumeric);

  public abstract DateState<TTable, TRow, TCell, TCellReference> asDate();

  public abstract LocalDateState<TTable, TRow, TCell, TCellReference> asLocalDate();

  public abstract LocalDateTimeState<TTable, TRow, TCell, TCellReference> asLocalDateTime();

  protected class TypeStateSwitcher<T>
      implements ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, T> {

    private final Function2<? super TCell, ? extends T> function;

    private final Predicate2<? super T> mandatoryValuePredicate;

    public TypeStateSwitcher(Function2<? super TCell, ? extends T> function) {
      this(function, Predicates2.notNull());
    }

    public TypeStateSwitcher(
        Function2<? super TCell, ? extends T> function,
        Predicate2<? super T> mandatoryValuePredicate) {
      super();
      this.function = function;
      this.mandatoryValuePredicate = mandatoryValuePredicate;
    }

    private <T2> TypeStateSwitcher<T2> newSwitcher(Function2<? super T, ? extends T2> function2) {
      return new TypeStateSwitcher<>(Functions2.compose(function2, function));
    }

    private <T2> TypeStateSwitcher<T2> newSwitcher(
        Function2<? super T, ? extends T2> function2,
        Predicate2<? super T2> mandatoryValuePredicate) {
      return new TypeStateSwitcher<>(
          Functions2.compose(function2, function), mandatoryValuePredicate);
    }

    @Override
    public StringState<TTable, TRow, TCell, TCellReference> toString(
        final Function2<? super T, String> function) {
      final TypeStateSwitcher<String> switcher = newSwitcher(function, Predicates2.hasText());
      return new StringState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<String> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public IntegerState<TTable, TRow, TCell, TCellReference> toInteger(
        Function2<? super T, Integer> function) {
      final TypeStateSwitcher<Integer> switcher = newSwitcher(function);
      return new IntegerState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<Integer> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public LongState<TTable, TRow, TCell, TCellReference> toLong(
        Function2<? super T, Long> function) {
      final TypeStateSwitcher<Long> switcher = newSwitcher(function);
      return new LongState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<Long> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public DoubleState<TTable, TRow, TCell, TCellReference> toDouble(
        Function2<? super T, Double> function) {
      final TypeStateSwitcher<Double> switcher = newSwitcher(function);
      return new DoubleState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<Double> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public BigDecimalState<TTable, TRow, TCell, TCellReference> toBigDecimal(
        Function2<? super T, BigDecimal> function) {
      final TypeStateSwitcher<BigDecimal> switcher = newSwitcher(function);
      return new BigDecimalState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<BigDecimal> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public DateState<TTable, TRow, TCell, TCellReference> toDate(
        Function2<? super T, ? extends Date> function) {
      final TypeStateSwitcher<Date> switcher = newSwitcher(function);
      return new DateState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<Date> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public LocalDateState<TTable, TRow, TCell, TCellReference> toLocalDate(
        Function2<? super T, LocalDate> function) {
      final TypeStateSwitcher<LocalDate> switcher = newSwitcher(function);
      return new LocalDateState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<LocalDate> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public LocalDateTimeState<TTable, TRow, TCell, TCellReference> toLocalDateTime(
        Function2<? super T, LocalDateTime> function) {
      final TypeStateSwitcher<LocalDateTime> switcher = newSwitcher(function);
      return new LocalDateTimeState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<LocalDateTime> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public BooleanState<TTable, TRow, TCell, TCellReference> toBoolean(
        Function2<? super T, Boolean> function) {
      final TypeStateSwitcher<Boolean> switcher = newSwitcher(function);
      return new BooleanState<TTable, TRow, TCell, TCellReference>() {
        @Override
        protected TypeStateSwitcher<Boolean> getStateSwitcher() {
          return switcher;
        }
      };
    }

    @Override
    public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> toGeneric(
        Function2<? super T, TValue> function) {
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
      return columnSet.new Column<>(columnMapper, function, mandatoryValuePredicate);
    }
  }
}
