package org.iglooproject.sass.service;

import com.google.common.base.Stopwatch;
import com.google.common.io.Resources;
import de.larsgrefer.sass.embedded.CompileSuccess;
import de.larsgrefer.sass.embedded.SassCompilationFailedException;
import de.larsgrefer.sass.embedded.SassCompiler;
import de.larsgrefer.sass.embedded.SassCompilerFactory;
import de.larsgrefer.sass.embedded.importer.WebjarsImporter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.iglooproject.autoprefixer.Autoprefixer;
import org.iglooproject.autoprefixer.AutoprefixerException;
import org.iglooproject.sass.config.ISassConfigurationProvider;
import org.iglooproject.sass.exceptions.UnknownScssScope;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScssServiceImpl implements IScssService, IScopeResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScssServiceImpl.class);

  private static final Pattern SCOPE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

  private final Map<String, Class<?>> scopes = new HashMap<>();
  private final ISassConfigurationProvider configurationProvider;

  /** Matches scoped import and extract associated name as first group. */
  private final Pattern scopePattern = Pattern.compile("^\\$\\(scope-([^)]+)\\).*");

  public ScssServiceImpl(final ISassConfigurationProvider configurationProvider) {
    super();
    this.configurationProvider = configurationProvider;
  }

  @Override
  public ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path) {
    try {
      URL scssPath = Resources.getResource(scope, path);
      // if useStatic == true, then try to load from resource path
      // if available, return an do-not-expire resource
      if (configurationProvider.useStatic()) {
        ScssStylesheetInformation information = tryLoadFromStatic(scope, path, scssPath);
        if (information != null) {
          LOGGER.info("useStatic=true, using static file to provide {}:{}", scope.getName(), path);
          return information;
        }
      }
      // else perform resource compilation
      return getCompiledDartClasspath(scssPath);
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  /** Check embedded resource for a build-time generated version. Else return null. */
  private ScssStylesheetInformation tryLoadFromStatic(Class<?> scope, String path, URL scssPath)
      throws URISyntaxException {
    try {
      Path outputFilename =
          StaticResourceHelper.getStaticResourcePath(
              configurationProvider.getResourcePath(), scope, path);
      URL staticResource = Resources.getResource(outputFilename.toString());
      String stylesheet = Resources.toString(staticResource, StandardCharsets.UTF_8);
      // lastModifiedDate is used to generate filename for URL. We need to ensure that filename is
      // unique so
      // that browser resource caching can be done (one file name by generation) and be refreshed
      // (filename
      // changes for each restart)
      ScssStylesheetInformation information =
          new ScssStylesheetInformation(scssPath.toString(), new Date().getTime());
      information.setSource(stylesheet);
      return information;
    } catch (IllegalArgumentException e) {
      LOGGER.warn(
          "useStatic=true but no static file found for {}:{}. Fallback to runtime processing.",
          scope.getName(),
          path);
    } catch (IOException e) {
      LOGGER.warn(
          "useStatic=true but static file for {}:{} cannot be read. Fallback to runtime processing.",
          scope.getName(),
          path,
          e);
    }
    return null;
  }

  private ScssStylesheetInformation getCompiledDartClasspath(URL path) {
    try (SassCompiler sassCompiler = SassCompilerFactory.bundled()) {
      // keep true; all messages are captured with slf4j
      // note: if false, then we can receive 1 warning message that notices there is
      // N deprecations warnings
      // note: deprecation warnings are at level info of SassCompiler
      sassCompiler.setVerbose(true);
      sassCompiler.setQuietDeps(false);
      WebjarsImporter webjarImporter = new WebjarsImporter();
      IglooDartImporter iglooDartImporter = new IglooDartImporter(webjarImporter, this);
      sassCompiler.registerImporter(iglooDartImporter.autoCanonicalize());
      CompileSuccess compileSuccess = sassCompiler.compile(path);
      String compiledOutput = compileSuccess.getCss();

      if (configurationProvider.isAutoprefixerEnabled()) {
        LOGGER.debug("Autoprefixer start for {}", path);
        Stopwatch stopwatch = Stopwatch.createStarted();
        compiledOutput = Autoprefixer.simple().process(compiledOutput);
        stopwatch.stop();
        LOGGER.debug(
            "Autoprefixer end for {} : {} ms", path, stopwatch.elapsed(TimeUnit.MILLISECONDS));
      }

      ScssStylesheetInformation compiledStylesheet =
          new ScssStylesheetInformation(path.toString(), compiledOutput);
      updateImportedStylesheets(compileSuccess, compiledStylesheet);

      return compiledStylesheet;
    } catch (RuntimeException
        | IOException
        | SassCompilationFailedException
        | AutoprefixerException e) {
      throw new IllegalStateException(String.format("Error compiling %s", path), e);
    }
  }

  @Override
  public void registerImportScope(String scopeName, Class<?> scope) {
    if (scopes.containsKey(scopeName)) {
      LOGGER.warn("Scope {} already registered: ignored", scopeName);
      return;
    }
    Matcher matcher = SCOPE_NAME_PATTERN.matcher(scopeName);
    if (!matcher.matches()) {
      LOGGER.error("Scope name {} invalid ({}): ignored", scopeName, SCOPE_NAME_PATTERN);
      return;
    }

    scopes.put(scopeName, scope);
  }

  protected String getFullPath(Class<?> scope, String path) {
    return scope.getPackageName().replace('.', '/') + "/" + path;
  }

  private void updateImportedStylesheets(
      CompileSuccess compileSuccess, ScssStylesheetInformation compiledStylesheet)
      throws IOException {
    for (String urlString : compileSuccess.getLoadedUrls()) {
      try {
        long lastModified = lastModified(urlString);
        compiledStylesheet.addImportedStylesheet(
            new ScssStylesheetInformation(urlString, lastModified));
      } catch (URISyntaxException e) {
        LOGGER.warn("Cannot extract lastModified from {}", urlString, e);
      }
    }
  }

  protected long lastModified(String urlString) throws URISyntaxException, IOException {
    URL url = new URL(urlString);
    long lastModified;
    if ("file".equals(url.toURI().getScheme())) {
      lastModified = new File(url.toURI().getPath()).lastModified();
    } else if ("jar".equals(url.toURI().getScheme())) {
      String jarFilePath = url.toURI().getRawSchemeSpecificPart();
      String jarPath = jarFilePath.substring("file:".length(), jarFilePath.indexOf("!"));
      lastModified = new File(jarPath).lastModified();
    } else {
      throw new IllegalStateException(String.format("URL %s not supported", urlString));
    }
    return lastModified;
  }

  @Override
  public String resolveScope(String scopedUrl) {
    Matcher matcher = scopePattern.matcher(scopedUrl);
    if (!matcher.matches()) {
      return scopedUrl;
    } else {
      Class<?> scope = scopes.getOrDefault(matcher.group(1), null);
      if (scope == null) {
        throw new UnknownScssScope(String.format("Scope %s unknown", matcher.group(1)));
      } else {
        return scopedUrl.replace(
            String.format("$(scope-%s)/", matcher.group(1)),
            scope.getPackageName().replace('.', '/') + "/");
      }
    }
  }
}
