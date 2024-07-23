package org.iglooproject.imports.table.common.mapping.column.builder;

import org.iglooproject.functional.Predicate2;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.state.TypeState;

public abstract class AbstractTableImportColumnBuilder<TTable, TRow, TCell, TCellReference> {

  public abstract TypeState<TTable, TRow, TCell, TCellReference> withHeader(
      AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet,
      String headerLabel,
      Predicate2<? super String> predicate,
      int indexAmongMatchedColumns,
      MappingConstraint mappingConstraint);

  public abstract TypeState<TTable, TRow, TCell, TCellReference> withIndex(
      AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet, int index);

  public abstract TypeState<TTable, TRow, TCell, TCellReference> unmapped(
      AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference> columnSet);
}
