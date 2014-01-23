package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.affix;

import org.odlabs.wiquery.core.options.IComplexOption;
import org.odlabs.wiquery.core.options.Options;

public class BootstrapAffixOptions extends Options {

	private static final long serialVersionUID = 8139302328021897144L;

	// Bootstrap affix options
	private Integer top;

	private Integer bottom;

	public BootstrapAffixOptions() {
		super();
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (top != null || bottom != null) {
			put("offset", new BootstrapAffixOffsetOptions(top, bottom));
		}
		
		return super.getJavaScriptOptions();
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getBottom() {
		return bottom;
	}

	public void setBottom(Integer bottom) {
		this.bottom = bottom;
	}

	private static class BootstrapAffixOffsetOptions implements IComplexOption {

		private static final long serialVersionUID = 3993624725774828933L;

		private Integer top;

		private Integer bottom;

		public BootstrapAffixOffsetOptions(Integer top, Integer bottom) {
			super();
			this.top = top;
			this.bottom = bottom;
		}

		@Override
		public CharSequence getJavascriptOption() {
			Options options = new Options();
			if (top != null) {
				options.put("top", top);
			}
			
			if (bottom != null) {
				options.put("bottom", bottom);
			}
			return options.getJavaScriptOptions();
		}
	}
}
