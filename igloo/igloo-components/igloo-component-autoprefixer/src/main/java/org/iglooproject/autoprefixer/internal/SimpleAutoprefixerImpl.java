package org.iglooproject.autoprefixer.internal;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.iglooproject.autoprefixer.AutoprefixerException;
import org.iglooproject.autoprefixer.IAutoprefixer;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ResultUndefined;
import com.eclipsesource.v8.V8ScriptCompilationException;
import com.eclipsesource.v8.V8ScriptExecutionException;

public class SimpleAutoprefixerImpl implements IAutoprefixer {

	private static final String GET_CSS_CALLBACK_METHOD_NAME = "javaGetCss";
	private static final String AUTOPREFIXER_JS_CLASSPATH = "/autoprefixer/autoprefixer.js"; // NOSONAR

	@Override
	public String process(String css) throws AutoprefixerException {
		final V8 runtime = V8.createV8Runtime();
		try {
			// prepare javaGetCss method as a callback to provide css code
			JavaCallback callback = (V8Object receiver, V8Array parameters) -> css;
			runtime.getRuntime().registerJavaMethod(callback, GET_CSS_CALLBACK_METHOD_NAME);
			String scriptBuilder = buildScript();
			return runtime.executeStringScript(scriptBuilder);
		} catch (IOException e) {
			// error loading autoprefixer runtime is considered as internal
			throw new IllegalStateException("Error initializing autoprefixer script", e);
		} catch (V8ScriptCompilationException e) {
			// error compiling autoprefixer runtime is considered as internal
			throw new IllegalStateException("Error compiling autoprefixer script", e);
		} catch (V8ScriptExecutionException|V8ResultUndefined v8Exception) {
			throw new AutoprefixerException("Error processing css", v8Exception);
		} finally {
			runtime.release();
		}
	}

	private String buildScript() throws IOException {
		// build script string
		StringBuilder scriptBuilder = new StringBuilder();
		// load autoprefixer library from static classpath resource
		scriptBuilder.append(loadAutoprefixerScriptAsString());
		scriptBuilder.append("\n");
		scriptBuilder.append(String.format("autoprefixer.process(%s()).css;%n", GET_CSS_CALLBACK_METHOD_NAME));
		return scriptBuilder.toString();
	}

	private String loadAutoprefixerScriptAsString() throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(AUTOPREFIXER_JS_CLASSPATH));
	}

}
