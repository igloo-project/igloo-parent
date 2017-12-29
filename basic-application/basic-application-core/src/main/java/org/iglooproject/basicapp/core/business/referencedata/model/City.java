package org.iglooproject.basicapp.core.business.referencedata.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;
import org.iglooproject.spring.util.StringUtils;

@Entity
@Bindable
@Indexed
@Cacheable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"label", "postalcode"}) })
public class City extends GenericListItem<City> {

	private static final long serialVersionUID = -5714475132350205234L;

	public static final String LABEL_AUTOCOMPLETE = "labelAutocomplete";
	
	public City() {
	}
	
	public City(String label) {
		super(label);
	}

	@Basic(optional = false)
	private PostalCode postalCode;

	public PostalCode getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
	}
	
	@Override
	public String getCode() {
		return postalCode == null ? null : postalCode.getValue();
	}

	@Override
	@Field(name = LABEL_AUTOCOMPLETE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD_CLEAN))
	public String getLabel() {
		return super.getLabel();
	}
	
	@Override
	public void setLabel(String label) {
		super.setLabel(StringUtils.upperCase(label));
	}
}
