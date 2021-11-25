package org.iglooproject.wicket.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.iglooproject.truevfs.registry.TFileRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.truevfs.access.TArchiveDetector;
import net.java.truevfs.access.TConfig;

public class OpenTFileRegistryFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenTFileRegistryFilter.class);
	
	private TConfig config;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = TConfig.open();
		configureGlobally(TConfig.open());
	}
	
	protected void configureGlobally(TConfig trueZipConfig) {
		trueZipConfig.setArchiveDetector(new TArchiveDetector("zip|jar"));
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try (TConfig conf = TConfig.open(); TFileRegistry registry = TFileRegistry.open()) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		if (config != null) {
			config.close();
		} else {
			LOGGER.warn("Unexpected null value for TConfig");
		}
	}
}

