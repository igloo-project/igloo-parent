package fr.openwide.core.commons.util.fieldpath;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FieldPathPropertyComponent extends FieldPathComponent {

	private static final long serialVersionUID = 2111693502794024737L;

	public static final String PROPERTY_SEPARATOR = ".";
	
	private final String propertyName;

	public FieldPathPropertyComponent(String name) {
		super();
		this.propertyName = name;
	}

	@Override
	public String getName() {
		return propertyName;
	}
	
	@Override
	public void appendTo(StringBuilder builder) {
		builder.append(PROPERTY_SEPARATOR);
		builder.append(propertyName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldPathPropertyComponent) {
			FieldPathPropertyComponent other = (FieldPathPropertyComponent) obj;
			return new EqualsBuilder()
					.append(propertyName, other.propertyName)
					.build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(propertyName)
				.build();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		appendTo(builder);
		return builder.toString();
	}

}
