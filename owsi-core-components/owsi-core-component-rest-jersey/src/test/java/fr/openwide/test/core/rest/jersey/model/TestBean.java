package fr.openwide.test.core.rest.jersey.model;

import java.io.Serializable;

public class TestBean implements Serializable {

	private static final long serialVersionUID = -6547406988628216538L;

	private Integer id;

	public TestBean() {
	}

	public TestBean(Integer id) {
		this.setId(id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
