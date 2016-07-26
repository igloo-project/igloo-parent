package fr.openwide.core.wicket.more.css.scss;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.css.WicketCssPrecompilationException;
import fr.openwide.core.wicket.more.css.scss.model.ScssStylesheetInformation;
import fr.openwide.core.wicket.more.css.scss.service.IScssService;

public class ScssResource extends PackageResource {

	private static final long serialVersionUID = 9067443522105165705L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScssResource.class);
	
	private String name;

	private Locale locale;

	private String variation;
	
	@SpringBean
	private IScssService scssService;

	public ScssResource(Class<?> scope, String name) {
		this(scope, name, null, null, null);
	}

	public ScssResource(Class<?> scope, String name, Locale locale, String style, String variation) {
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
			
			ScssStylesheetInformation cssInformation = scssService.getCompiledStylesheet(
					getScope(), getName(),
					Application.get().usesDevelopmentConfig()
			);
			
			StringResourceStream scssResourceStream = new StringResourceStream(cssInformation.getSource(), "text/css");
			scssResourceStream.setCharset(Charset.forName("UTF-8"));
			scssResourceStream.setLastModified(Time.millis(cssInformation.getLastModifiedTime()));
			
			return scssResourceStream;
		} catch (RuntimeException | ServiceException e) {
			throw new WicketCssPrecompilationException(String.format("Error reading SCSS source for %1$s (%2$s, %3$s, %4$s)",
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
