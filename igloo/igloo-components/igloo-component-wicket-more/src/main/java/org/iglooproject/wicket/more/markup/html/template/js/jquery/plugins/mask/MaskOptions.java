package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask;

import org.apache.wicket.model.IModel;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.Options;

public class MaskOptions extends Options {
	private static final long serialVersionUID = 8361789161890761478L;

	private final Options translationOptions = new Options();
	
	private static class MaskTranslationOptions implements IComplexOption {
		private static final long serialVersionUID = -7911528421294931614L;
		
		private final String pattern;
		private final boolean optional;
		private final boolean recursive;
		
		private final Character fallback;

		public MaskTranslationOptions(String pattern, boolean optional, boolean recursive, Character fallback) {
			super();
			this.pattern = pattern;
			this.optional = optional;
			this.recursive = recursive;
			this.fallback = fallback;
		}

		@Override
		public CharSequence getJavascriptOption() {
			Options options =  new Options()
					.put("pattern", pattern) // No quotes
					.put("optional", optional)
					.put("recursive", recursive);
			
			if (fallback != null) {
				options.put("fallback", JsUtils.quotes(String.valueOf(fallback)));
			}
			
			return options.getJavaScriptOptions();
		}
		
	}
	
	@Override
	public void detach() {
		super.detach();
		translationOptions.detach();
	}
	
	@Override
	public CharSequence getJavaScriptOptions() {
		put("translation", translationOptions.getJavaScriptOptions().toString());
		return super.getJavaScriptOptions();
	}

	public MaskOptions setOnKeyPress(JsScope onKeyPress) {
		put("onKeyPress", onKeyPress);
		return this;
	}

	public MaskOptions setOnComplete(JsScope onComplete) {
		put("onComplete", onComplete);
		return this;
	}

	public MaskOptions setOnChange(JsScope onChange) {
		put("onChange", onChange);
		return this;
	}
	
	public MaskOptions addTranslation(char character, String pattern) {
		return addTranslation(character, new MaskTranslationOptions(pattern, false, false, null));
	}
	
	public MaskOptions addTranslation(char character, String pattern, boolean optional, boolean recursive) {
		return addTranslation(character, new MaskTranslationOptions(pattern, optional, recursive, null));
	}
	
	public MaskOptions addTranslation(char character, String pattern, char fallback) {
		return addTranslation(character, new MaskTranslationOptions(pattern, false, false, fallback));
	}
	
	public MaskOptions addTranslation(char character, String pattern, boolean optional, boolean recursive, char fallback) {
		return addTranslation(character, new MaskTranslationOptions(pattern, optional, recursive, fallback));
	}
	
	protected MaskOptions addTranslation(char character, MaskTranslationOptions options) {
		translationOptions.put(JsUtils.quotes(String.valueOf(character)), options);
		return this;
	}
	
	public MaskOptions setPlaceholder(IModel<String> placeholder) {
		putLiteral("placeholder", placeholder);
		return this;
	}

}
