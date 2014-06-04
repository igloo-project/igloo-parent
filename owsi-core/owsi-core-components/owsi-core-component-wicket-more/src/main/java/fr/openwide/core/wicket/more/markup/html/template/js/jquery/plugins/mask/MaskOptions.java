package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.mask;

import java.util.Map;

import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.IComplexOption;
import org.odlabs.wiquery.core.options.Options;

import com.google.common.collect.Maps;

public class MaskOptions extends Options {
	private static final long serialVersionUID = 8361789161890761478L;

	private JsScope onKeyPress;

	private JsScope onComplete;

	private JsScope onChange;
	
	private Map<Character, MaskTranslationOptions> translation = Maps.newLinkedHashMap();
	
	private static class MaskTranslationOptions implements IComplexOption {
		private static final long serialVersionUID = -7911528421294931614L;
		
		private final String pattern;
		private final boolean optional;
		private final boolean recursive;

		public MaskTranslationOptions(String pattern, boolean optional, boolean recursive) {
			super();
			this.pattern = pattern;
			this.optional = optional;
			this.recursive = recursive;
		}

		@Override
		public CharSequence getJavascriptOption() {
			return new Options()
					.put("pattern", pattern) // No quotes
					.put("optional", optional)
					.put("recursive", recursive)
					.getJavaScriptOptions();
		}
		
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (onKeyPress != null) {
			put("onKeyPress", onKeyPress.render().toString());
		}
		if (onComplete != null) {
			put("onComplete", onComplete.render().toString());
		}
		if (onChange != null) {
			put("onChange", onChange.render().toString());
		}
		Options translationOptions = new Options();
		for (Map.Entry<Character, MaskTranslationOptions> entry : translation.entrySet()) {
			translationOptions.put(JsUtils.quotes(entry.getKey().toString()), entry.getValue());
		}
		put("translation", translationOptions.getJavaScriptOptions().toString());
		
		return super.getJavaScriptOptions();
	}

	public JsScope getOnKeyPress() {
		return onKeyPress;
	}

	public MaskOptions setOnKeyPress(JsScope onKeyPress) {
		this.onKeyPress = onKeyPress;
		return this;
	}

	public JsScope getOnComplete() {
		return onComplete;
	}

	public MaskOptions setOnComplete(JsScope onComplete) {
		this.onComplete = onComplete;
		return this;
	}

	public JsScope getOnChange() {
		return onChange;
	}

	public MaskOptions setOnChange(JsScope onChange) {
		this.onChange = onChange;
		return this;
	}
	
	public MaskOptions addTranslation(char character, String pattern) {
		return addTranslation(character, pattern, false, false);
	}
	
	public MaskOptions addTranslation(char character, String pattern, boolean optional, boolean recursive) {
		translation.put(character, new MaskTranslationOptions(pattern, optional, recursive));
		return this;
	}

}
