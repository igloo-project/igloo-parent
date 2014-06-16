package fr.openwide.core.imports.excel.location;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExcelImportLocation implements Serializable {
	private static final long serialVersionUID = 6866449558201453287L;
	
	private final String fileName;
	private final String sheetName;
	private final Integer rowIndexZeroBased;
	private final String cellAddress;
	
	public ExcelImportLocation(String fileName, String sheetName, Integer rowIndexZeroBased, String cellAddress) {
		super();
		this.fileName = fileName;
		this.sheetName = sheetName;
		this.rowIndexZeroBased = rowIndexZeroBased;
		this.cellAddress = cellAddress;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	
	public Integer getRowIndexZeroBased() {
		return rowIndexZeroBased;
	}
	
	public Integer getRowIndexOneBased() {
		return rowIndexZeroBased == null ? null : rowIndexZeroBased + 1;
	}
	
	public String getCellAddress() {
		return cellAddress;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ExcelImportLocation)) {
			return false;
		}
		ExcelImportLocation other = (ExcelImportLocation) obj;
		return new EqualsBuilder()
				.append(fileName, other.fileName)
				.append(sheetName, other.sheetName)
				.append(rowIndexZeroBased, other.rowIndexZeroBased)
				.append(cellAddress, other.cellAddress)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(fileName)
				.append(sheetName)
				.append(rowIndexZeroBased)
				.append(cellAddress)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("fileName", fileName)
				.append("sheetName", sheetName)
				.append("rowIndex (1-based)", getRowIndexOneBased())
				.append("cellAddress", cellAddress)
				.build();
	}
}