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

  public synchronized Boolean getApplyAttributesTableElements() {
    return applyAttributesTableElements;
  }

  public synchronized void setApplyAttributesTableElements(Boolean applyAttributesTableElements) {
    this.applyAttributesTableElements = applyAttributesTableElements;
  }

  public synchronized Boolean getApplyHeightAttributes() {
    return applyHeightAttributes;
  }

  public synchronized void setApplyHeightAttributes(Boolean applyHeightAttributes) {
    this.applyHeightAttributes = applyHeightAttributes;
  }

  public synchronized Boolean getApplyStyleTags() {
    return applyStyleTags;
  }

  public synchronized void setApplyStyleTags(Boolean applyStyleTags) {
    this.applyStyleTags = applyStyleTags;
  }

  public synchronized Boolean getApplyWidthAttributes() {
    return applyWidthAttributes;
  }

  public synchronized void setApplyWidthAttributes(Boolean applyWidthAttributes) {
    this.applyWidthAttributes = applyWidthAttributes;
  }

  public synchronized String getExtraCss() {
    return extraCss;
  }

  public synchronized void setExtraCss(String extraCss) {
    this.extraCss = extraCss;
  }

  public synchronized Boolean getInsertPreservedExtraCss() {
    return insertPreservedExtraCss;
  }

  public synchronized void setInsertPreservedExtraCss(Boolean insertPreservedExtraCss) {
    this.insertPreservedExtraCss = insertPreservedExtraCss;
  }

  public synchronized Boolean getInlinePseudoElements() {
    return inlinePseudoElements;
  }

  public synchronized void setInlinePseudoElements(Boolean inlinePseudoElements) {
    this.inlinePseudoElements = inlinePseudoElements;
  }

  public synchronized Boolean getPreserveFontFaces() {
    return preserveFontFaces;
  }

  public synchronized void setPreserveFontFaces(Boolean preserveFontFaces) {
    this.preserveFontFaces = preserveFontFaces;
  }

  public synchronized Boolean getPreserveImportant() {
    return preserveImportant;
  }

  public synchronized void setPreserveImportant(Boolean preserveImportant) {
    this.preserveImportant = preserveImportant;
  }

  public synchronized Boolean getPreserveMediaQueries() {
    return preserveMediaQueries;
  }

  public synchronized void setPreserveMediaQueries(Boolean preserveMediaQueries) {
    this.preserveMediaQueries = preserveMediaQueries;
  }

  public synchronized Boolean getPreserveKeyFrames() {
    return preserveKeyFrames;
  }

  public synchronized void setPreserveKeyFrames(Boolean preserveKeyFrames) {
    this.preserveKeyFrames = preserveKeyFrames;
  }

  public synchronized Boolean getPreservePseudos() {
    return preservePseudos;
  }

  public synchronized void setPreservePseudos(Boolean preservePseudos) {
    this.preservePseudos = preservePseudos;
  }

  public synchronized Boolean getRemoveStyleTags() {
    return removeStyleTags;
  }

  public synchronized void setRemoveStyleTags(Boolean removeStyleTags) {
    this.removeStyleTags = removeStyleTags;
  }

  public synchronized Boolean getXmlMode() {
    return xmlMode;
  }

  public synchronized void setXmlMode(Boolean xmlMode) {
    this.xmlMode = xmlMode;
  }
}
