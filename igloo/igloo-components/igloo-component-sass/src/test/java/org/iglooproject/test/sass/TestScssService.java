package org.iglooproject.test.sass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;
import org.iglooproject.sass.cli.ScssMain;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.sass.service.ScssServiceImpl;
import org.iglooproject.test.sass.config.TestSassConfigurationProvider;
import org.iglooproject.test.sass.resources.TestScssServiceResourceScope;
import org.iglooproject.test.sass.resources.other.scope.TestScssServiceOtherResourceScope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.common.io.Resources;

import de.larsgrefer.sass.embedded.importer.CustomUrlImporter;

class TestScssService {
	
	private IScssService scssService = new ScssServiceImpl(TestSassConfigurationProvider.of(false, false));
	private IScssService autoprefixerScssService = new ScssServiceImpl(TestSassConfigurationProvider.of(true, false));
	private IScssService staticScssService = new ScssServiceImpl(TestSassConfigurationProvider.of(true, true));
	
	@Test
	void testGetCompiledStylesheet() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style.scss"
		);
		
		assertEquals(".test2 {\n  color: #eeeeee;\n}\n\n.test {\n  color: #cccccc;\n}", compiledStylesheet.getSource());
		assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
	}
	
	@Test
	void testGetCompiledStylesheetWithScope() throws Exception {
		scssService.registerImportScope("test", TestScssServiceOtherResourceScope.class);
		
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-scope.scss"
		);
		
		assertThat(compiledStylesheet.getSource())
			.isEqualToIgnoringWhitespace(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}\n\n.test4 {\n\tcolor: #cccccc;\n}\n\n.test5 {\n\tcolor: #cccccc;\n}\n\ntest3 {\n\tcolor: #eeeeee;\n}\n");
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
		String source = Resources.toString(Resources.getResource("org/iglooproject/test/sass/resources/autoprefixer.scss"), StandardCharsets.UTF_8);
		String output =
				scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "autoprefixer.scss").getSource();
		Assertions.assertThat(output).isEqualToIgnoringWhitespace(source);
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

	@Test
	void testStatic() {
		// static disabled / generated file is empty
		String output = scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "static-source.scss").getSource();
		assertThat(output).isEmpty();
		// static enaabled / static file is used - igloo-static-scss/4c1d3b924a175dc799714bd2ef6955585f926b1c0fff34e48d61ec3c16956732.css
		// filename from StaticResourceHelper.getStaticResourcePath("", TestScssServiceResourceScope.class, "static-source.scss");
		String outputStatic = staticScssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "static-source.scss").getSource();
		assertThat(outputStatic).isEqualTo("/* not empty marker */");
	}

	@Test
	void testMain(@TempDir Path tempDir) {
		ScssMain.main(new String[] {
				"--generation-path",
				tempDir.toString(),
				String.format("%s:%s", TestScssServiceResourceScope.class.getName(), "main.scss")
		});
		// from StaticResourceHelper.getStaticResourcePath("", TestScssServiceResourceScope.class, "main.scss");
		assertThat(tempDir.resolve("igloo-static-scss").resolve("1b1044731aa57ea2c56bb4ea859c60b8f4591cea23b05aef4a04c0758f5e3631.css"))
			.content().contains("body {\n  font-weight: bold;\n}").contains("Generated from");
	}
	
	@Test
	void testBootstrap4() {
		assertThatCode(() -> scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "bootstrap4.scss")).doesNotThrowAnyException();
	}
	
	@Test
	void testBootstrap5() {
		assertThatCode(() -> scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "bootstrap5.scss")).doesNotThrowAnyException();
	}
	
	/**
	 * <p>dart-sass embedder behavior is not the same when {@link CustomUrlImporter}<code>usedPrefixes</code> contains
	 * a path. Resources resolution failure (null) that may be silently ignored triggers
	 * <code>has no known prefix</code> when getRelativePath is called.</p>
	 * 
	 * <p>Triggering this use-case is done by loading a $(scope-*) resource (it add a path in usedPrefixes) then
	 * loading a webjar resource inside a webjar.</p>
	 * 
	 * @see https://github.com/larsgrefer/dart-sass-java/issues/104
	 */
	@Test
	void testMixedWebjarsScope() {
		scssService.registerImportScope("test", TestScssServiceOtherResourceScope.class);
		assertThatCode(() -> scssService.getCompiledStylesheet(TestScssServiceResourceScope.class, "mixed-webjars-scope.scss")).doesNotThrowAnyException();
	}

}