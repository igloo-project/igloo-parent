package fr.openwide.core.wicket.more.fileapi.model;

import java.io.Serializable;

import org.bindgen.Bindable;

/**
 * File API Json binding
 */
@Bindable
public class FileApiFile implements Serializable {

	private static final long serialVersionUID = 1198717709622037528L;

	private String identifier;

	private String name;

	private Long size;

	private String type;

	private String objectUrl;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getSize() {
		return size;
	}
	
	public void setSize(Long size) {
		this.size = size;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getObjectUrl() {
		return objectUrl;
	}

	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}
}
