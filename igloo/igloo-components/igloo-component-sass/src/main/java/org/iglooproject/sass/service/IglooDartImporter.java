package org.iglooproject.sass.service;

import com.sass_lang.embedded_protocol.Syntax;
import de.larsgrefer.sass.embedded.importer.ClasspathImporter;
import de.larsgrefer.sass.embedded.importer.WebjarsImporter;
import de.larsgrefer.sass.embedded.util.SyntaxUtil;
import java.io.IOException;
import java.net.URL;

/**
 * Handle both webjars (<code>META-INF/resources/webjars/...</code>) and scoped (<code>$(scope-NAME)/path/file<code>)
 * URLs.
 */
public final class IglooDartImporter
    extends ClasspathImporter { // NOSONAR no interest to override equals/hashcode

  private final WebjarsImporter webjarImporter;
  private final IScopeResolver scopeResolver;

  public IglooDartImporter(WebjarsImporter webjarImporter, IScopeResolver scopeResolver) {
    this.webjarImporter = webjarImporter;
    this.scopeResolver = scopeResolver;
  }

  @Override
  public String canonicalize(String url, boolean fromImport) throws Exception {
    // handle file URLs
    if (url.startsWith("file:///")) {
      return super.canonicalize(url, fromImport);
    }
    // handle classpath URLs
    if (url.startsWith("jar:file:/") && url.contains("!")) {
      // directory on openStream does not fail so we need to handle the directory case specifically
      // we artificially exclude directory case by checking file extension
      // example : bootstrap 4 has a mixins folder and use /mixins URL to load _mixins.scss
      // we need to return null for mixins, so that _mixins.scss is tried
      var tried = new URL(url);
      Syntax syntax = SyntaxUtil.guessSyntax(tried);
      if (Syntax.UNRECOGNIZED.equals(syntax)) {
        // do not accept extension-less or unknown extensions
        return null;
      }
      // check resource existence
      try (var is = tried.openStream()) {
        return tried.toString();
      } catch (IOException e) {
        // Not existing path must return null
        // Do not let super.canonicalize handle it as it cannot handle version-less webjars urls
        // as once usedPrefixes is populated it will enforce prefix checking and initial prefix
        // cannot be registered as META-INF/resources/webjars/lib/res.scss is not a subpath
        // or jar:file:.../META-INF/resources/webjars/lib/VERSION/res.scss
        return null;
      }
    }
    // handle webjars URL
    if (url.startsWith("META-INF/resources/webjars/")) {
      // will be handled by canonicalizeUrl
      return super.canonicalize(url, fromImport);
    }
    // handle $(scope-NAME)
    if (url.startsWith("$(scope-")) {
      return super.canonicalize(scopeResolver.resolveScope(url), fromImport);
    }
    return super.canonicalize(url, fromImport);
  }

  @Override
  public URL canonicalizeUrl(String url) throws IOException {
    if (url.startsWith("META-INF/resources/webjars/")) {
      return webjarImporter.canonicalizeUrl(url);
    }
    return super.canonicalizeUrl(url);
  }
}
