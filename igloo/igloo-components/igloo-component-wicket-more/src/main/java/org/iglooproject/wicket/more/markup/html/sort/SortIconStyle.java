package org.iglooproject.wicket.more.markup.html.sort;

public enum SortIconStyle implements ISortIconStyle {
  DEFAULT {
    @Override
    public String getAscIconCssClasses() {
      return "fa fa-fw fa-sort-amount-asc fa-sort-amount-down";
    }

    @Override
    public String getDescIconCssClasses() {
      return "fa fa-fw fa-sort-amount-desc fa-sort-amount-up";
    }
  },
  ALPHABET {
    @Override
    public String getAscIconCssClasses() {
      return "fa fa-fw fa-sort-alpha-asc fa-sort-alpha-down";
    }

    @Override
    public String getDescIconCssClasses() {
      return "fa fa-fw fa-sort-alpha-desc fa-sort-alpha-up";
    }
  },
  NUMERIC {
    @Override
    public String getAscIconCssClasses() {
      return "fa fa-fw fa-sort-numeric-asc fa-sort-numeric-down";
    }

    @Override
    public String getDescIconCssClasses() {
      return "fa fa-fw fa-sort-numeric-desc fa-sort-numeric-up";
    }
  };

  @Override
  public abstract String getAscIconCssClasses();

  @Override
  public abstract String getDescIconCssClasses();
}
