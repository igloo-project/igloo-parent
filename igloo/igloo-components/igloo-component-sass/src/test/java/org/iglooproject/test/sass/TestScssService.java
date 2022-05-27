package org.iglooproject.test.sass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.sass.service.ScssServiceImpl;
import org.iglooproject.test.sass.config.TestSassConfigurationProvider;
import org.iglooproject.test.sass.resources.TestScssServiceResourceScope;
import org.iglooproject.test.sass.resources.other.scope.TestScssServiceOtherResourceScope;
import org.junit.jupiter.api.Test;

class TestScssService {
	
	private IScssService scssService = new ScssServiceImpl(TestSassConfigurationProvider.of(false));
	private IScssService autoprefixerScssService = new ScssServiceImpl(TestSassConfigurationProvider.of());
	
	@Test
	void testGetCompiledStylesheet() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style.scss"
		);
		
		assertEquals(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}\n", compiledStylesheet.getSource());
		assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
	}
	
	@Test
	void testGetCompiledStylesheetWithScope() throws Exception {
		scssService.registerImportScope("test", TestScssServiceOtherResourceScope.class);
		
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-scope.scss"
		);
		
		assertEquals(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}\n\n.test4 {\n\tcolor: #cccccc;\n}\n\n.test5 {\n\tcolor: #cccccc;\n}\n\ntest3 {\n\tcolor: #eeeeee;\n}\n", compiledStylesheet.getSource());
		assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
	}

	/**
	 * Test webjars://[webjar]/[version]/path urls
	 */
	@Test
	void testWebjarImport() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/[version]/path with extension
	 */
	@Test
	void testWebjarImportWithExtension() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-with-extension.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/[version]/path from webjars imported file
	 */
	@Test
	void testWebjarImportChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-chained.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/path urls (versionless)
	 */
	@Test
	void testWebjarImportVersionLess() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-versionless.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test relative import from webjar import
	 */
	@Test
	void testWebjarImportRelativeChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-relative-chained.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/current/path urls. current is a magic version.
	 */
	@Test
	void testWebjarImportCurrent() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-current.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/current/path urls in a chain (@import done in imported file)
	 */
	@Test
	void testWebjarImportCurrentChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-current-chained.scss"
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Forbidden protocol
	 */
	@Test
	void forbiddenProtocol() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"forbidden-protocol.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * Forbidden absolute
	 */
	@Test
	void forbiddenAbsolute() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"forbidden-absolute.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * Forbidden relative
	 */
	@Test
	void forbiddenRelative() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"forbidden-relative.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * Forbidden relative (do not start by ../, but contains ../)
	 */
	@Test
	void forbiddenRelative2() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"forbidden-relative-2.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * Not found
	 */
	@Test
	void notFound() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"not-found.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * unknown-scope
	 */
	@Test
	void unknownScope() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"unknown-scope.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * forbidden extension
	 */
	@Test
	void forbiddenExtension() throws Exception {
		assertThatCode(() -> scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"forbidden-extension.scss"
		)).isInstanceOf(RuntimeException.class);
	}

	/**
	 * disable autoprefixer: input == output
	 */
	@Test
	void noAutoprefixer() throws IOException {
		// about autoprefixer.scss:
		// use indent with tab as spaces are processed to tab by scss
		// (we want to check input == output if no autoprefixer)
		String source = IOUtils.toString(
				getClass().getResourceAsStream("/org/iglooproject/test/sass/resources/autoprefixer.scss"));
		String output =
				scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "autoprefixer.scss").getSource();
		Assertions.assertThat(output).isEqualTo(source);
	}

	/**
	 * enable autoprefixer: as sticky is present in input, output must rewritten to include -webkit-sticky
	 */
	@Test
	void autoprefixer() throws IOException {
		String output =
				autoprefixerScssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "autoprefixer.scss")
				.getSource();
		Assertions.assertThat(output).describedAs("-webkit-sticky must be added to the output")
			.contains("-webkit-sticky");
	}

}