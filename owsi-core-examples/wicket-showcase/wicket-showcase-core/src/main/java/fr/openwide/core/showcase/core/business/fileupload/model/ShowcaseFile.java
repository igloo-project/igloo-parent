package fr.openwide.core.showcase.core.business.fileupload.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Entity
@Bindable
public class ShowcaseFile extends GenericEntity<Long, GenericEntity<Long, ShowcaseFile>> {

	private static final long serialVersionUID = 6353983458892174115L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String extension;

	private String commentaire;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	@Override
	public String getNameForToString() {
		return name != null ? name : "null";
	}

	@Override
	public String getDisplayName() {
		return getNameForToString();
	}

}
