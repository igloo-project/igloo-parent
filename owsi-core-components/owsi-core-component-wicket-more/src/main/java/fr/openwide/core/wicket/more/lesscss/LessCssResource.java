package fr.openwide.core.wicket.more.lesscss;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.wicket.more.lesscss.model.CssStylesheetInformation;
import fr.openwide.core.wicket.more.lesscss.service.ILessCssService;

public class LessCssResource extends PackageResource {

	private static final long serialVersionUID = 9067443522105165705L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LessCssResource.class);
	
	private String name;

	private Locale locale;

	private String variation;
	
	@SpringBean
	private ILessCssService lessCssService;

	public LessCssResource(Class<?> scope, String name) {
		this(scope, name, null, null, null);
	}

	public LessCssResource(Class<?> scope, String name, Locale locale, String style, String variation) {
		super(scope, name, locale, style, variation);
		
		Injector.get().inject(this);
		
		this.name = name;
		this.locale = locale;
		this.variation = variation;
	}

	public String getName() {
		return name;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getVariation() {
		return variation;
	}

	@Override
	public IResourceStream getResourceStream() {
		IResourceStream resourceStream = null;
		try {
			resourceStream = super.getResourceStream();
			
			String lessSource = IOUtils.toString(resourceStream.getInputStream(), "UTF-8");
			long lastModifiedTime = resourceStream.lastModifiedTime().getMilliseconds();
			
			CssStylesheetInformation cssInformation = lessCssService.getCompiledStylesheet(getScope(), getName(),
					new CssStylesheetInformation(lessSource, lastModifiedTime),
					Application.get().usesDeploymentConfig());
			
			StringResourceStream lessCssResourceStream = new StringResourceStream(cssInformation.getSource(), "text/css");
			lessCssResourceStream.setCharset(Charset.forName("UTF-8"));
			lessCssResourceStream.setLastModified(Time.millis(cssInformation.getLastModifiedTime()));
			
			return lessCssResourceStream;
		} catch (Exception e) {
			throw new WicketLessCssException(String.format("Error reading lesscss source for %1$s (%2$s, %3$s, %4$s)",
					getName(), getLocale(), getStyle(), getVariation()), e);
		} finally {
			if (resourceStream != null) {
				try {
					resourceStream.close();
				} catch (IOException e) {
					LOGGER.error(String.format("Error closing the original resource stream"));
				}
			}
		}
	}

}
