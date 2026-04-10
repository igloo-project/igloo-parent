package org.iglooproject.wicket.more.notification.service.impl;

import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @see {@link http://www.w3.org/TR/css3-selectors/#specificity}
 */
@Deprecated
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
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CssSelectorSpecificity other)) {
      return false;
    }
    return Objects.equals(isStyle, other.isStyle)
        && Objects.equals(idSelectors, other.idSelectors)
        && Objects.equals(classAndPseudoClassSelectors, other.classAndPseudoClassSelectors)
        && Objects.equals(typeSelectorsAndPseudoElements, other.typeSelectorsAndPseudoElements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        isStyle, idSelectors, classAndPseudoClassSelectors, typeSelectorsAndPseudoElements);
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
