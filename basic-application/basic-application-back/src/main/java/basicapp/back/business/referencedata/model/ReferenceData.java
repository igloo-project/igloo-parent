package basicapp.back.business.referencedata.model;

import basicapp.back.business.common.model.embeddable.LocalizedText;
import com.querydsl.core.annotations.QueryInit;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

@MappedSuperclass
@Bindable
public class ReferenceData<E extends ReferenceData<?>>
    extends GenericReferenceData<E, LocalizedText> implements IReferenceDataBindingInterface {

  private static final long serialVersionUID = -1779439527249543663L;

  public static final String LABEL = "label";
  public static final String LABEL_PREFIX = LABEL + ".";
  public static final String LABEL_FR_AUTOCOMPLETE = LABEL_PREFIX + LocalizedText.FR_AUTOCOMPLETE;
  public static final String LABEL_FR_SORT = LABEL_PREFIX + LocalizedText.FR_SORT;
  public static final String LABEL_EN_AUTOCOMPLETE = LABEL_PREFIX + LocalizedText.EN_AUTOCOMPLETE;
  public static final String LABEL_EN_SORT = LABEL_PREFIX + LocalizedText.EN_SORT;

  @Embedded
  @IndexedEmbedded(name = LABEL)
  @QueryInit("*")
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private LocalizedText label;

  public ReferenceData() {
    this(new LocalizedText());
  }

  public ReferenceData(LocalizedText label) {
    setLabel(label);
  }

  @Override
  public LocalizedText getLabel() {
    if (label == null) {
      label = new LocalizedText();
    }
    return label;
  }

  @Override
  public void setLabel(LocalizedText label) {
    this.label = (label == null ? null : new LocalizedText(label));
  }
}
