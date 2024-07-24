package org.iglooproject.imports.table.opencsv.model;

import com.google.common.collect.AbstractSequentialIterator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CsvTable implements Serializable, Iterable<CsvRow> {

  private static final long serialVersionUID = -4479121548776416896L;

  private final List<List<String>> rows;

  public CsvTable(List<String[]> rows) {
    super();
    Objects.requireNonNull(rows);

    this.rows = rows.stream().map(input -> Arrays.asList(input)).collect(Collectors.toList());
  }

  @Override
  public Iterator<CsvRow> iterator() {
    return new AbstractSequentialIterator<CsvRow>(getRow(0)) {
      @Override
      protected CsvRow computeNext(CsvRow previous) {
        return getRow(previous.getIndex() + 1);
      }
    };
  }

  public List<List<String>> getContent() {
    return rows;
  }

  public CsvRow getRow(int index) {
    if (0 <= index && index < rows.size()) {
      return new CsvRow(this, index);
    } else {
      return null;
    }
  }

  // equals and hashcode defined as identity (==)

}
