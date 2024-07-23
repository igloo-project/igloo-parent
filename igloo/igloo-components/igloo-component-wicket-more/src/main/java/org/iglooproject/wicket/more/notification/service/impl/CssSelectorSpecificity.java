package org.iglooproject.wicket.more.notification.service.impl;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @see {@link http://www.w3.org/TR/css3-selectors/#specificity}
 */
public class CssSelectorSpecificity implements Comparable<CssSelectorSpecificity> {

  public static final CssSelectorSpecificity STYLE = new CssSelectorSpecificity(true, 0, 0, 0);

  private final boolean isStyle;
  private final int idSelectors;
  private final int classAndPseudoClassSelectors;
  private final int typeSelectorsAndPseudoElements;

  public CssSelectorSpecificity(
      int idSelectors, int classAndPseudoClassSelectors, int typeSelectorsAndPseudoElements) {
    this(false, idSelectors, classAndPseudoClassSelectors, typeSelectorsAndPseudoElements);
  }

  private CssSelectorSpecificity(
      boolean isStyle,
      int idSelectors,
      int classAndPseudoClassSelectors,
      int typeSelectorsAndPseudoElements) {
    super();
    this.isStyle = isStyle;
    this.idSelectors = idSelectors;
    this.classAndPseudoClassSelectors = classAndPseudoClassSelectors;
    this.typeSelectorsAndPseudoElements = typeSelectorsAndPseudoElements;
  }

  @Override
  public int compareTo(CssSelectorSpecificity other) {
    return new CompareToBuilder()
        .append(isStyle, other.isStyle)
        .append(idSelectors, other.idSelectors)
        .append(classAndPseudoClassSelectors, other.classAndPseudoClassSelectors)
        .append(typeSelectorsAndPseudoElements, other.typeSelectorsAndPseudoElements)
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CssSelectorSpecificity)) {
      return false;
    }
    CssSelectorSpecificity other = (CssSelectorSpecificity) obj;
    return compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(isStyle)
        .append(idSelectors)
        .append(classAndPseudoClassSelectors)
        .append(typeSelectorsAndPseudoElements)
        .build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("isStyle", isStyle)
        .append("idSelectors", idSelectors)
        .append("classAndPseudoClassSelectors", classAndPseudoClassSelectors)
        .append("typeSelectorsAndPseudoElements", typeSelectorsAndPseudoElements)
        .build();
  }
}
