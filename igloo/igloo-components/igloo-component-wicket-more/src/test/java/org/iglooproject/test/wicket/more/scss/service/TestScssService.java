package org.iglooproject.test.wicket.more.scss.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.iglooproject.test.wicket.more.scss.service.resource.TestScssServiceResourceScope;
import org.iglooproject.test.wicket.more.scss.service.resource.other.scope.TestScssServiceOtherResourceScope;
import org.iglooproject.wicket.more.css.scss.model.ScssStylesheetInformation;
import org.iglooproject.wicket.more.css.scss.service.IScssService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestScssService extends AbstractWicketMoreTestCase {
	
	@Autowired
	private IScssService scssService;
	
	@Test
	public void testGetCompiledStylesheet() throws Exception {
		try {
			ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
					TestScssServiceResourceScope.class,
					"style.scss",
					false
			);
			
			Assert.assertEquals(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}\n", compiledStylesheet.getSource());
			Assert.assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
		} finally {
		}
	}
	
	@Test
	public void testGetCompiledStylesheetWithScope() throws Exception {
		InputStream is = null;
		try {
			scssService.registerImportScope("test", TestScssServiceOtherResourceScope.class);
			
			ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
					TestScssServiceResourceScope.class,
					"style-scope.scss",
					false
			);
			
			Assert.assertEquals(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}\n\n.test4 {\n\tcolor: #cccccc;\n}\n\n.test5 {\n\tcolor: #cccccc;\n}\n\ntest3 {\n\tcolor: #eeeeee;\n}\n", compiledStylesheet.getSource());
			Assert.assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Test webjars://[webjar]/[version]/path urls
	 */
	@Test
	public void testWebjarImport() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/[version]/path from webjars imported file
	 */
	@Test
	public void testWebjarImportChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-chained.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/path urls (versionless)
	 */
	@Test
	public void testWebjarImportVersionLess() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-versionless.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test relative import from webjar import
	 */
	@Test
	public void testWebjarImportRelativeChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-relative-chained.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/current/path urls. current is a magic version.
	 */
	@Test
	public void testWebjarImportCurrent() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-current.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

	/**
	 * Test webjars://[webjar]/current/path urls in a chain (@import done in imported file)
	 */
	@Test
	public void testWebjarImportCurrentChained() throws Exception {
		ScssStylesheetInformation compiledStylesheet = scssService.getCompiledStylesheet(
				TestScssServiceResourceScope.class,
				"style-webjars-current-chained.scss",
				false
		);
		assertThat(compiledStylesheet.getSource()).isEqualToNormalizingWhitespace("body { font-family: sans-serif; font-size: 15px; font-weight: 200; }");
	}

}