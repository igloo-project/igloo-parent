package fr.openwide.core.test.wicket.more.lesscss.service;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import fr.openwide.core.test.wicket.more.AbstractWicketMoreTestCase;
import fr.openwide.core.test.wicket.more.lesscss.service.resource.TestLessCssServiceResourceScope;
import fr.openwide.core.test.wicket.more.lesscss.service.resource.other.scope.TestLessCssServiceOtherResourceScope;
import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;
import fr.openwide.core.wicket.more.lesscss.service.ILessCssService;

public class TestLessCssService extends AbstractWicketMoreTestCase {
	
	@Autowired
	private ILessCssService lessCssService;
	
	@Test
	public void testGetCompiledStylesheet() throws Exception {
		InputStream is = null;
		try {
			ClassPathResource stylesheetResource = new ClassPathResource("style.less", TestLessCssServiceResourceScope.class);
			is = stylesheetResource.getInputStream();
			
			String rawSource = IOUtils.toString(is);
			
			CssStylesheetInformation compiledStylesheet = lessCssService.getCompiledStylesheet(
					TestLessCssServiceResourceScope.class,
					"style.less",
					new CssStylesheetInformation(rawSource, stylesheetResource.lastModified()),
					true);
			
			Assert.assertEquals(".test2 {\n  color: #eeeeee;\n}\n.test {\n  color: #cccccc;\n}\n", compiledStylesheet.getSource());
			Assert.assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	@Test
	public void testGetCompiledStylesheetWithScope() throws Exception {
		InputStream is = null;
		try {
			lessCssService.registerImportScope("test", TestLessCssServiceOtherResourceScope.class);
			
			ClassPathResource stylesheetResource = new ClassPathResource("style-scope.less", TestLessCssServiceResourceScope.class);
			is = stylesheetResource.getInputStream();
			
			String rawSource = IOUtils.toString(is);
			
			CssStylesheetInformation compiledStylesheet = lessCssService.getCompiledStylesheet(
					TestLessCssServiceResourceScope.class,
					"style-scope.less",
					new CssStylesheetInformation(rawSource, stylesheetResource.lastModified()),
					true);
			
			Assert.assertEquals(".test2 {\n  color: #eeeeee;\n}\n.test {\n  color: #cccccc;\n}\n.test4 {\n  color: #cccccc;\n}\n.test5 {\n  color: #cccccc;\n}\ntest3 {\n  color: #eeeeee;\n}\n", compiledStylesheet.getSource());
			Assert.assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}