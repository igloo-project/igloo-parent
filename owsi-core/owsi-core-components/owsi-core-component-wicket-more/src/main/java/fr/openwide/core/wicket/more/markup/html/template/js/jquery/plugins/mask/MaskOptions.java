package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.mask;

import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.IComplexOption;
import org.odlabs.wiquery.core.options.Options;

public class MaskOptions extends Options {
	private static final long serialVersionUID = 8361789161890761478L;

	private final Options translationOptions = new Options();
	
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
		return addTranslation(character, pattern, false, false);
	}
	
	public MaskOptions addTranslation(char character, String pattern, boolean optional, boolean recursive) {
		translationOptions.put(JsUtils.quotes(String.valueOf(character)), new MaskTranslationOptions(pattern, optional, recursive));
		return this;
	}
	
	public MaskOptions setPlaceholder(IModel<String> placeholder) {
		putLiteral("placeholder", placeholder);
		return this;
	}

}
