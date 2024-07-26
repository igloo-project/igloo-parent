package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.conditionalinput;

public enum ConditionalInputAction {
  SHOW("show"),
  ENABLE("enable"),
  DISABLE("disable");

  private final String label;

  private ConditionalInputAction(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
