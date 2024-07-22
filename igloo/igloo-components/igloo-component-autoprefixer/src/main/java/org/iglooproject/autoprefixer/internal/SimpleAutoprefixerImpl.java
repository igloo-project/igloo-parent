package org.iglooproject.autoprefixer.internal;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.iglooproject.autoprefixer.AutoprefixerException;
import org.iglooproject.autoprefixer.IAutoprefixer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

public class SimpleAutoprefixerImpl implements IAutoprefixer {

  private static final String GET_CSS_VARIABLE_NAME = "css";
  private static final String AUTOPREFIXER_JS_CLASSPATH =
      "/autoprefixer/autoprefixer.js"; // NOSONAR

  @Override
  public String process(String css) throws AutoprefixerException {
    // prepare javaGetCss method as a callback to provide css code
    Context context = Context.enter();
    try {
      context.setLanguageVersion(Context.VERSION_ES6);
      Scriptable scope = context.initStandardObjects();
      scope.put("css", scope, Context.toString(css));
      String script = buildScript();
      return (String) context.evaluateString(scope, script, "autoprefixer.js", 1, null);
    } catch (IOException e) {
      // error loading autoprefixer runtime is considered as internal
      throw new IllegalStateException("Error initializing autoprefixer script", e);
    } catch (JavaScriptException cssException) {
      throw new AutoprefixerException("Error processing css", cssException);
    } finally {
      Context.exit();
    }
  }

  private String buildScript() throws IOException {
    // build script string
    StringBuilder scriptBuilder = new StringBuilder();
    // load autoprefixer library from static classpath resource
    scriptBuilder.append(loadAutoprefixerScriptAsString());
    scriptBuilder.append("\n");
    // we need a dummy Error#setMessage method (not needed with j2v8) to correctly store exceptions
    // if not added, any error is hidden by a setMessage not found error message
    scriptBuilder.append("Error.prototype.setMessage = function(message) {};");
    scriptBuilder.append(String.format("autoprefixer.process(%s).css;%n", GET_CSS_VARIABLE_NAME));
    return scriptBuilder.toString();
  }

  private String loadAutoprefixerScriptAsString() throws IOException {
    return IOUtils.toString(getClass().getResourceAsStream(AUTOPREFIXER_JS_CLASSPATH));
  }
}
