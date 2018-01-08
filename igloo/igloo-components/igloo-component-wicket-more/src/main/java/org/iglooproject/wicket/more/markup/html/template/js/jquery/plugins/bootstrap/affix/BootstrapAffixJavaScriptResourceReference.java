package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.affix;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

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
public final class BootstrapAffixJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapAffixJavaScriptResourceReference INSTANCE = new BootstrapAffixJavaScriptResourceReference();

	private BootstrapAffixJavaScriptResourceReference() {
		super(BootstrapAffixJavaScriptResourceReference.class, "affix.js");
	}

	public static BootstrapAffixJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
