package igloo.difference.model;

import java.util.ArrayList;
import java.util.List;

public class DifferenceFields {
	private final List<DifferenceField> fieldList = new ArrayList<>();

	public List<DifferenceField> getFieldList() {
		return fieldList;
	}

	public String toPlainString() {
		StringBuilder sb = new StringBuilder();
		for (DifferenceField field : fieldList) {
			sb.append("%s".formatted(field)).append("\n");
		}
		return sb.toString();
	}
}