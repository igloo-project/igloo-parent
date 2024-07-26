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

    public MaskTranslationOptions(
        String pattern, boolean optional, boolean recursive, Character fallback) {
      super();
      this.pattern = pattern;
      this.optional = optional;
      this.recursive = recursive;
      this.fallback = fallback;
    }

    @Override
    public CharSequence getJavascriptOption() {
      Options options =
          new Options()
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

  public MaskOptions translation(char character, String pattern) {
    return translation(character, new MaskTranslationOptions(pattern, false, false, null));
  }

  public MaskOptions translation(
      char character, String pattern, boolean optional, boolean recursive) {
    return translation(character, new MaskTranslationOptions(pattern, optional, recursive, null));
  }

  public MaskOptions translation(char character, String pattern, char fallback) {
    return translation(character, new MaskTranslationOptions(pattern, false, false, fallback));
  }

  public MaskOptions translation(
      char character, String pattern, boolean optional, boolean recursive, char fallback) {
    return translation(
        character, new MaskTranslationOptions(pattern, optional, recursive, fallback));
  }

  protected MaskOptions translation(char character, MaskTranslationOptions options) {
    translationOptions.put(JsUtils.quotes(String.valueOf(character)), options);
    return this;
  }

  public MaskOptions placeholder(IModel<String> placeholder) {
    putLiteral("placeholder", placeholder);
    return this;
  }

  public MaskOptions reverse() {
    return reverse(true);
  }

  public MaskOptions reverse(boolean reverse) {
    put("reverse", reverse);
    return this;
  }

  public MaskOptions clearIfNotMatch() {
    return clearIfNotMatch(true);
  }

  public MaskOptions clearIfNotMatch(boolean clearIfNotMatch) {
    put("clearIfNotMatch", clearIfNotMatch);
    return this;
  }

  public MaskOptions selectOnFocus() {
    return selectOnFocus(true);
  }

  public MaskOptions selectOnFocus(boolean selectOnFocus) {
    put("selectOnFocus", selectOnFocus);
    return this;
  }

  public MaskOptions onComplete(JsScope onComplete) {
    put("onComplete", onComplete);
    return this;
  }

  public MaskOptions onKeyPress(JsScope onKeyPress) {
    put("onKeyPress", onKeyPress);
    return this;
  }

  public MaskOptions onChange(JsScope onChange) {
    put("onChange", onChange);
    return this;
  }

  public MaskOptions onInvalid(JsScope onInvalid) {
    put("onInvalid", onInvalid);
    return this;
  }
}
