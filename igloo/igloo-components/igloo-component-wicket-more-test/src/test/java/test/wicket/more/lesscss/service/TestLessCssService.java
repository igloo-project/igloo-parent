package test.wicket.more.lesscss.service;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.iglooproject.wicket.more.css.lesscss.model.LessCssStylesheetInformation;
import org.iglooproject.wicket.more.css.lesscss.service.ILessCssService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import test.wicket.more.AbstractWicketMoreTestCase;
import test.wicket.more.lesscss.service.resource.TestLessCssServiceResourceScope;
import test.wicket.more.lesscss.service.resource.other.scope.TestLessCssServiceOtherResourceScope;

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
			
			LessCssStylesheetInformation compiledStylesheet = lessCssService.getCompiledStylesheet(
					new LessCssStylesheetInformation(
							TestLessCssServiceResourceScope.class,
							"style.less",
							rawSource,
							stylesheetResource.lastModified()
					),
					false
			);
			
			assertThat(
					compiledStylesheet.getSource(),
					CoreMatchers.startsWith(".test2 {\n  color: #eeeeee;\n}\n"
							+ ".test {\n  color: #cccccc;\n}\n")
			);
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
			
			LessCssStylesheetInformation compiledStylesheet = lessCssService.getCompiledStylesheet(
					new LessCssStylesheetInformation(
							TestLessCssServiceResourceScope.class,
							"style-scope.less",
							rawSource,
							stylesheetResource.lastModified()
					),
					false
			);

			assertThat(
					compiledStylesheet.getSource(),
					CoreMatchers.startsWith(".test2 {\n  color: #eeeeee;\n}\n"
							+ ".test {\n  color: #cccccc;\n}\n"
							+ ".test4 {\n  color: #cccccc;\n}\n"
							+ ".test5 {\n  color: #cccccc;\n}\n"
							+ "test3 {\n  color: #eeeeee;\n}\n")
			);
			Assert.assertTrue(compiledStylesheet.getLastModifiedTime() > 1324508163000l);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}