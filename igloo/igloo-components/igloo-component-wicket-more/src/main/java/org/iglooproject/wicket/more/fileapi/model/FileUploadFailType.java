package org.iglooproject.wicket.more.fileapi.model;

public enum FileUploadFailType {
	
	GENERIC("generic"),
	CANCEL("cancel");
	
	private final String name;
	
	private FileUploadFailType(String name) {
		this.name = name;
	}
	
	public static FileUploadFailType fromName(String name) {
		for (FileUploadFailType type : values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		return GENERIC;
	}
}
