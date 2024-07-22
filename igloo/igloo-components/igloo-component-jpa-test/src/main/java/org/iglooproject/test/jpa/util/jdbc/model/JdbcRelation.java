package org.iglooproject.test.jpa.util.jdbc.model;

import java.sql.DatabaseMetaData;

/**
 * Store {@link DatabaseMetaData} result item.
 *
 * @see DatabaseMetaData#getTables(String, String, String, String[])
 * @author Laurent Almeras
 */
public class JdbcRelation {

  private String table_cat;
  private String table_schem;
  private String table_name;
  private String table_type;
  private String table_remarks;
  private String type_cat;
  private String type_schem;
  private String type_name;
  private String self_referencing_col_name;
  private String ref_generation;

  public String getTable_cat() {
    return table_cat;
  }

  public void setTable_cat(String table_cat) {
    this.table_cat = table_cat;
  }

  public String getTable_schem() {
    return table_schem;
  }

  public void setTable_schem(String table_schem) {
    this.table_schem = table_schem;
  }

  public String getTable_name() {
    return table_name;
  }

  public void setTable_name(String table_name) {
    this.table_name = table_name;
  }

  public String getTable_type() {
    return table_type;
  }

  public void setTable_type(String table_type) {
    this.table_type = table_type;
  }

  public String getTable_remarks() {
    return table_remarks;
  }

  public void setTable_remarks(String table_remarks) {
    this.table_remarks = table_remarks;
  }

  public String getType_cat() {
    return type_cat;
  }

  public void setType_cat(String type_cat) {
    this.type_cat = type_cat;
  }

  public String getType_schem() {
    return type_schem;
  }

  public void setType_schem(String type_schem) {
    this.type_schem = type_schem;
  }

  public String getType_name() {
    return type_name;
  }

  public void setType_name(String type_name) {
    this.type_name = type_name;
  }

  public String getSelf_referencing_col_name() {
    return self_referencing_col_name;
  }

  public void setSelf_referencing_col_name(String self_referencing_col_name) {
    this.self_referencing_col_name = self_referencing_col_name;
  }

  public String getRef_generation() {
    return ref_generation;
  }

  public void setRef_generation(String ref_generation) {
    this.ref_generation = ref_generation;
  }
}
