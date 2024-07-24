package org.iglooproject.export.excel;

/** Classe contenant toutes les informations nécessaires sur l'affichage d'une colonne */
public class ColumnInformation {

  private String headerKey;

  private boolean hidden = false;

  private int columnWidth = -1;

  private int columnMaxWidth = -1;

  public ColumnInformation(String headerKey) {
    this.headerKey = headerKey;
  }

  public ColumnInformation(String headerKey, boolean hidden) {
    this(headerKey);
    this.hidden = hidden;
  }

  public ColumnInformation(String headerKey, boolean hidden, int columnMaxWidth) {
    this(headerKey, hidden);
    this.columnMaxWidth = columnMaxWidth;
  }

  public ColumnInformation(String headerKey, boolean hidden, int columnMaxWidth, int columnWidth) {
    this(headerKey, hidden, columnMaxWidth);
    this.columnWidth = columnWidth;
  }

  public String getHeaderKey() {
    return headerKey;
  }

  public void setHeaderKey(String headerKey) {
    this.headerKey = headerKey;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public int getColumnWidth() {
    return columnWidth;
  }

  public void setColumnWidth(int columnWidth) {
    this.columnWidth = columnWidth;
  }

  public int getColumnMaxWidth() {
    return columnMaxWidth;
  }

  public void setColumnMaxWidth(int columnMaxWidth) {
    this.columnMaxWidth = columnMaxWidth;
  }
}
