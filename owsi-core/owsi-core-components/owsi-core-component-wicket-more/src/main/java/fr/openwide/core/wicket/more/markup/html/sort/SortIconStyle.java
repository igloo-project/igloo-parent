package fr.openwide.core.wicket.more.markup.html.sort;

public enum SortIconStyle implements ISortIconStyle {
	
	DEFAULT {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-attributes fa fa-sort-amount-asc";
		}

		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-attributes-alt fa fa-sort-amount-desc";
		}
	},
	ALPHABET {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-alphabet fa fa-sort-alpha-asc";
		}
		
		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-alphabet-alt fa fa-sort-alpha-desc";
		}
	},
	NUMERIC {
		@Override
		public String getAscIconCssClasses() {
			return "icon-sort-by-order fa fa-sort-numeric-asc";
		}
		
		@Override
		public String getDescIconCssClasses() {
			return "icon-sort-by-order-alt fa fa-sort-numeric-desc";
		}
	};

	@Override
	public abstract String getAscIconCssClasses();

	@Override
	public abstract String getDescIconCssClasses();

}
