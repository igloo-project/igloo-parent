package org.iglooproject.imports.table.opencsv.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

import com.google.common.collect.AbstractSequentialIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class CsvRow implements Serializable, Iterable<CsvCell> {

  private static final long serialVersionUID = -6092290249182587998L;

  private final CsvTable sheet;

  private final int index;

  public CsvRow(CsvTable sheet, int index) {
    super();
    checkNotNull(sheet);
    checkPositionIndex(index, sheet.getContent().size());
    this.sheet = sheet;
    this.index = index;
  }

  @Override
  public Iterator<CsvCell> iterator() {
    return new AbstractSequentialIterator<CsvCell>(getCell(0)) {
      @Override
      protected CsvCell computeNext(CsvCell previous) {
        return getCell(previous.getIndex() + 1);
      }
    };
  }

  public CsvTable getSheet() {
    return sheet;
  }

  public int getIndex() {
    return index;
  }

  public List<String> getContent() {
    return sheet.getContent().get(index);
  }

  public CsvCell getCell(int index) {
    List<String> content = getContent();
    if (0 <= index && index < content.size()) {
      return new CsvCell(this, index);
    } else {
      return null;
    }
  }
}
