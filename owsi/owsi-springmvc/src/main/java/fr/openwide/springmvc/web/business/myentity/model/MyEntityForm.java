package fr.openwide.springmvc.web.business.myentity.model;

import java.util.Date;

public class MyEntityForm {

	private int id;
	
	private String descr;
	
	private Date date;

	public MyEntityForm() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
