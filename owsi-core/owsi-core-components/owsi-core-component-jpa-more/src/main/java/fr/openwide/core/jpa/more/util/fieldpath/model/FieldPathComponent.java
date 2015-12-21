package fr.openwide.core.jpa.more.util.fieldpath.model;

import java.io.Serializable;

public abstract class FieldPathComponent implements Serializable {

	private static final long serialVersionUID = -8327444733903379047L;

	public abstract String getName();
	
	public abstract void appendTo(StringBuilder builder);
	
	public static final FieldPathComponent ITEM = new FieldPathComponent() {
		private static final long serialVersionUID = 1L;
		@Override
		public String getName() {
			return "[*]";
		}
		@Override
		public void appendTo(StringBuilder builder) {
			builder.append(getName());
		}
		public Object readResolve() {
			return ITEM;
		}
	};

}
