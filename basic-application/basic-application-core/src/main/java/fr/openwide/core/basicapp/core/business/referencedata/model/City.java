package fr.openwide.core.basicapp.core.business.referencedata.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.basicapp.core.business.common.model.CodePostal;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.search.bridge.MaterializedStringValueFieldBridge;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.spring.util.StringUtils;

@Entity
@Bindable
@Indexed
@Cacheable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"label", "codepostal"}) })
public class City extends GenericListItem<City> {

	private static final long serialVersionUID = -5714475132350205234L;

	public static final String LABEL_AUTOCOMPLETE = "labelAutocomplete";
	
	public static final String CODE_POSTAL = "codePostal";
	
	public City() {
	}
	
	public City(String label) {
		super(label);
	}

	@Basic(optional = false)
	@Field(name = CODE_POSTAL, bridge = @FieldBridge(impl = MaterializedStringValueFieldBridge.class), analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD_CLEAN))
	private CodePostal codePostal;

	public CodePostal getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(CodePostal codePostal) {
		this.codePostal = codePostal;
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
