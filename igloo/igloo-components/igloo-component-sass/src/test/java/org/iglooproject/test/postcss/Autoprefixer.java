package org.iglooproject.test.postcss;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

public class Autoprefixer {

	public static void main(String[] args) throws IOException {
		final V8 runtime = V8.createV8Runtime();
		try {
			JavaCallback callback = new JavaCallback() {
				@Override
				public Object invoke(V8Object receiver, V8Array parameters) {
					return " .sticky-top {\n" + 
							"  @supports (position: sticky) {\n" + 
							"    position: sticky;\n" + 
							"    top: 0;\n" + 
							"    z-index: $zindex-sticky;\n" + 
							"  }\n" + 
							"}";
				}
			};
	
			runtime.getRuntime().registerJavaMethod(callback, "javaGetCss");
			String script = IOUtils.toString(Autoprefixer.class.getClassLoader().getResourceAsStream("autoprefixer/autoprefixer.js"));
			script += ";\n";
			script += "\n";
			script += "autoprefixer.process(javaGetCss()).css;\n";
			String result = runtime.executeStringScript(script);
			System.out.println(result);
		} finally {
			runtime.release();
		}
	}

}
