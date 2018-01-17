package org.iglooproject.test.wicket.more.scss.service;

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

}