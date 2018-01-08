package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.affix;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.Options;

/**
 * @deprecated From Bootstrap migration guide :<br />
 * 
 * <strong>Dropped the Affix jQuery plugin.</strong>
 * <ul>
 * 	<li>
 * 		We recommend using <code>position: sticky</code> instead. <a href="http://html5please.com/#sticky">See the HTML5 Please entry</a> for details and specific polyfill recommendations.
 * 		One suggestion is to use an <code>@supports</code> rule for implementing it (e.g., <code>@supports (position: sticky) { ... }</code>)/
 * 	</li>
 * 	<li>
 * 		If you were using Affix to apply additional, non-<code>position</code> styles, the polyfills might not support your use case.
 * 		One option for such uses is the third-party <a href="https://github.com/acch/scrollpos-styler">ScrollPos-Styler</a> library.
 * 	</li>
 * </ul>
 */
@Deprecated
public class BootstrapAffixOptions extends Options {

	private static final long serialVersionUID = 8139302328021897144L;

	@Deprecated
	private Integer top;

	@Deprecated
	private Integer bottom;

	public BootstrapAffixOptions() {
		super();
	}

	public BootstrapAffixOffsetOptions getOffsetOptions() {
		BootstrapAffixOffsetOptions options = (BootstrapAffixOffsetOptions) getComplexOption("offset");
		if (options == null) {
			options = new BootstrapAffixOffsetOptions();
			put("offset", options);
		}
		return options;
	}

	@Deprecated
	public Integer getTop() {
		return top;
	}

	public BootstrapAffixOptions setTop(Integer top) {
		this.top = top;
		getOffsetOptions().setTop(top);
		return this;
	}

	public BootstrapAffixOptions setTop(JsScope scope) {
		this.top = null;
		getOffsetOptions().setTop(scope);
		return this;
	}

	public BootstrapAffixOptions setEnclosing(Component enclosingComponent) {
		return setEnclosing(enclosingComponent, 0);
	}

	public BootstrapAffixOptions setEnclosing(Component enclosingComponent, int offset) {
		return setEnclosing(enclosingComponent, offset, offset);
	}

	public BootstrapAffixOptions setEnclosing(Component enclosingComponent, int topOffset, int bottomOffset) {
		this.top = null;
		setTop(JsScope.quickScope(new JsStatement()
				.append("return ").$(enclosingComponent).chain("offset").append(".top").append(" + ").append(String.valueOf(topOffset))
		));
		setBottom(JsScope.quickScope(new JsStatement()
				.append("return ").append("$(document)").chain("height")
				.append(" - ").$(enclosingComponent).chain("offset").append(".top")
				.append(" - ").$(enclosingComponent).chain("outerHeight")
				.append(" + ").append(String.valueOf(bottomOffset))
		));
		return this;
	}

	@Deprecated
	public Integer getBottom() {
		return bottom;
	}

	public BootstrapAffixOptions setBottom(Integer bottom) {
		this.bottom = bottom;
		getOffsetOptions().setBottom(bottom);
		return this;
	}

	public BootstrapAffixOptions setBottom(JsScope scope) {
		this.bottom = null;
		getOffsetOptions().setBottom(scope);
		return this;
	}
	
	public BootstrapAffixOptions setTarget(Component target) {
		put("target", new JsStatement().$(target).render(false).toString());
		return this;
	}

	private static class BootstrapAffixOffsetOptions implements IComplexOption {

		private static final long serialVersionUID = 3993624725774828933L;
		private final Options options = new Options();

		public BootstrapAffixOffsetOptions() {
			super();
		}

		public void setTop(Integer top) {
			if (top != null) {
				options.put("top", top);
			}
		}

		public void setTop(JsScope scope) {
			if (scope != null) {
				options.put("top", scope);
			}
		}

		public void setBottom(Integer bottom) {
			if (bottom != null) {
				options.put("bottom", bottom);
			}
		}

		public void setBottom(JsScope scope) {
			if (scope != null) {
				options.put("bottom", scope);
			}
		}

		@Override
		public CharSequence getJavascriptOption() {
			
			return options.getJavaScriptOptions();
		}
	}
}
