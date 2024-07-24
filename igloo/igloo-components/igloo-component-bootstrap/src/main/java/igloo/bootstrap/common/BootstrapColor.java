package igloo.bootstrap.common;

public enum BootstrapColor implements IBootstrapColor {
  PRIMARY("primary"),
  SECONDARY("secondary"),
  SUCCESS("success"),
  INFO("info"),
  WARNING("warning"),
  DANGER("danger"),
  LIGHT("light"),
  DARK("dark"),
  BRAND("brand");

  private final String cssClassSuffix;

  private BootstrapColor(String cssClassSuffix) {
    this.cssClassSuffix = cssClassSuffix;
  }

  @Override
  public String getCssClassSuffix() {
    return cssClassSuffix;
  }
}
