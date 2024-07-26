package org.iglooproject.basicapp.core.business.referencedata.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;

@Entity
@Bindable
@Indexed
@Cacheable
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"label_fr", "postalcode"})})
public class City extends ReferenceData<City> {

  private static final long serialVersionUID = -5714475132350205234L;

  public static final String LABEL_AUTOCOMPLETE = "labelAutocomplete";

  @Basic(optional = false)
  private PostalCode postalCode;

  public City() {}

  public City(LocalizedText label) {
    super(label);
  }

  public PostalCode getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(PostalCode postalCode) {
    this.postalCode = postalCode;
  }

  @Override
  @Transient
  public String getCode() {
    return postalCode == null ? null : postalCode.getValue();
  }
}
