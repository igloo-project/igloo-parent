package igloo.juice;

public class InlinerOptions {

  private Boolean applyAttributesTableElements;

  private Boolean applyHeightAttributes;

  private Boolean applyStyleTags;

  private Boolean applyWidthAttributes;

  private String extraCss;

  private Boolean insertPreservedExtraCss;

  private Boolean inlinePseudoElements;

  private Boolean preserveFontFaces;

  private Boolean preserveImportant;

  private Boolean preserveMediaQueries;

  private Boolean preserveKeyFrames;

  private Boolean preservePseudos;

  private Boolean removeStyleTags;

  private Boolean xmlMode;

  public Boolean getApplyAttributesTableElements() {
    return applyAttributesTableElements;
  }

  public void setApplyAttributesTableElements(Boolean applyAttributesTableElements) {
    this.applyAttributesTableElements = applyAttributesTableElements;
  }

  public Boolean getApplyHeightAttributes() {
    return applyHeightAttributes;
  }

  public void setApplyHeightAttributes(Boolean applyHeightAttributes) {
    this.applyHeightAttributes = applyHeightAttributes;
  }

  public Boolean getApplyStyleTags() {
    return applyStyleTags;
  }

  public void setApplyStyleTags(Boolean applyStyleTags) {
    this.applyStyleTags = applyStyleTags;
  }

  public Boolean getApplyWidthAttributes() {
    return applyWidthAttributes;
  }

  public void setApplyWidthAttributes(Boolean applyWidthAttributes) {
    this.applyWidthAttributes = applyWidthAttributes;
  }

  public String getExtraCss() {
    return extraCss;
  }

  public void setExtraCss(String extraCss) {
    this.extraCss = extraCss;
  }

  public Boolean getInsertPreservedExtraCss() {
    return insertPreservedExtraCss;
  }

  public void setInsertPreservedExtraCss(Boolean insertPreservedExtraCss) {
    this.insertPreservedExtraCss = insertPreservedExtraCss;
  }

  public Boolean getInlinePseudoElements() {
    return inlinePseudoElements;
  }

  public void setInlinePseudoElements(Boolean inlinePseudoElements) {
    this.inlinePseudoElements = inlinePseudoElements;
  }

  public Boolean getPreserveFontFaces() {
    return preserveFontFaces;
  }

  public void setPreserveFontFaces(Boolean preserveFontFaces) {
    this.preserveFontFaces = preserveFontFaces;
  }

  public Boolean getPreserveImportant() {
    return preserveImportant;
  }

  public void setPreserveImportant(Boolean preserveImportant) {
    this.preserveImportant = preserveImportant;
  }

  public Boolean getPreserveMediaQueries() {
    return preserveMediaQueries;
  }

  public void setPreserveMediaQueries(Boolean preserveMediaQueries) {
    this.preserveMediaQueries = preserveMediaQueries;
  }

  public Boolean getPreserveKeyFrames() {
    return preserveKeyFrames;
  }

  public void setPreserveKeyFrames(Boolean preserveKeyFrames) {
    this.preserveKeyFrames = preserveKeyFrames;
  }

  public Boolean getPreservePseudos() {
    return preservePseudos;
  }

  public void setPreservePseudos(Boolean preservePseudos) {
    this.preservePseudos = preservePseudos;
  }

  public Boolean getRemoveStyleTags() {
    return removeStyleTags;
  }

  public void setRemoveStyleTags(Boolean removeStyleTags) {
    this.removeStyleTags = removeStyleTags;
  }

  public Boolean getXmlMode() {
    return xmlMode;
  }

  public void setXmlMode(Boolean xmlMode) {
    this.xmlMode = xmlMode;
  }
}
