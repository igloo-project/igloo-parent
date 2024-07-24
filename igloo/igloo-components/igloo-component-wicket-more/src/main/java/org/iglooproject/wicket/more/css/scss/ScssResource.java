package org.iglooproject.wicket.more.css.scss;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Locale;
import org.apache.wicket.Application;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.wicket.more.css.WicketCssPrecompilationException;
import org.iglooproject.wicket.more.css.scss.service.ICachedScssService;

/** Exclusive usage by {@link ScssResourceReference}. */
class ScssResource extends PackageResource {

  private static final long serialVersionUID = 9067443522105165705L;

  private Locale locale;

  private String variation;

  @SpringBean private ICachedScssService scssService;

  public ScssResource(Class<?> scope, String name) {
    this(scope, name, null, null, null);
  }

  public ScssResource(Class<?> scope, String name, Locale locale, String style, String variation) {
    super(scope, name, locale, style, variation);

    Injector.get().inject(this);

    this.locale = locale;
    this.variation = variation;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getVariation() {
    return variation;
  }

  @Override
  public IResourceStream getResourceStream() {
    try (IResourceStream resourceStream = super.getResourceStream()) {
      ScssStylesheetInformation cssInformation =
          scssService.getCachedCompiledStylesheet(
              getScope(), getName(), Application.get().usesDevelopmentConfig());

      try (StringResourceStream scssResourceStream =
          new StringResourceStream(cssInformation.getSource(), "text/css")) {
        scssResourceStream.setCharset(Charset.forName("UTF-8"));
        scssResourceStream.setLastModified(
            Instant.ofEpochMilli(cssInformation.getLastModifiedTime()));
        return scssResourceStream;
      }
    } catch (RuntimeException | IOException e) {
      throw new WicketCssPrecompilationException(
          String.format(
              "Error reading SCSS source for %1$s (%2$s, %3$s, %4$s)",
              getName(), getLocale(), getStyle(), getVariation()),
          e);
    }
  }
}
