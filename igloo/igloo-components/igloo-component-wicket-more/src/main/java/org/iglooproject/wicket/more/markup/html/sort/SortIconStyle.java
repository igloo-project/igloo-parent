package org.iglooproject.wicket.more.markup.html.sort;

public enum SortIconStyle implements ISortIconStyle {
	
	DEFAULT {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-attributes fa fa-sort-amount-asc fa-sort-amount-up";
		}

		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-attributes-alt fa fa-sort-amount-desc fa-sort-amount-down";
		}
	},
	ALPHABET {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-alphabet fa fa-sort-alpha-asc fa-sort-alpha-up";
		}
		
		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-alphabet-alt fa fa-sort-alpha-desc fa-sort-alpha-down";
		}
	},
	NUMERIC {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-order fa fa-sort-numeric-asc fa-sort-numeric-up";
		}
		
		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-order-alt fa fa-sort-numeric-desc fa-sort-numeric-down";
		}
	};

	@Override
	public abstract String getAscIconCssClasses();

	@Override
	public abstract String getDescIconCssClasses();

}
