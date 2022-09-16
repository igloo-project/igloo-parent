package igloo.bootstrap;

import java.util.Optional;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestHandlerDelegate;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapRequestCycle {

	private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapRequestCycle.class);

	private BootstrapRequestCycle() {}

	@SuppressWarnings("serial")
	public static final MetaDataKey<BootstrapVersion> VERSION_KEY = new MetaDataKey<BootstrapVersion>() {};

	/**
	 * Get bootstrap version from request cycle cache, or resolve it from currently processed page.
	 */
	private static BootstrapVersion getVersion() {
		return Optional.ofNullable(RequestCycle.get().getMetaData(VERSION_KEY)).orElse(resolveVersion());
	}

	/**
	 * Resolve version from page, cache on request cycle and return.
	 */
	private static BootstrapVersion resolveVersion() {
		BootstrapVersion version = findVersion();
		RequestCycle.get().setMetaData(VERSION_KEY, version);
		return version;
	}

	/**
	 * Resolve bootstrap version from currently processed page.
	 */
	private static BootstrapVersion findVersion() {
		IRequestHandler requestHandler = RequestCycle.get().getActiveRequestHandler();
		if (requestHandler instanceof IRequestHandlerDelegate) {
			requestHandler = ((IRequestHandlerDelegate) requestHandler).getDelegateHandler();
		}
		if (!(requestHandler instanceof IPageRequestHandler)) {
			throw new IllegalStateException(String.format("requestHandler not a IPageRequestHandler; version cannot be resolved (%s)", requestHandler.getClass().getName()));
		} else {
			IPageRequestHandler pageRequestHandler = (IPageRequestHandler) requestHandler;
			Class<?> pageClass = pageRequestHandler.getPageClass();
			boolean bootstrap4 = IBootstrap4Page.class.isAssignableFrom(pageClass);
			boolean bootstrap5 = IBootstrap5Page.class.isAssignableFrom(pageClass);
			if (bootstrap4 && bootstrap5) {
				LOGGER.warn("Both bootstrap 4 and bootstrap 5 enabled on {}, fallback to default {}; please use only one version", pageClass.getName(), BootstrapVersion.BOOTSTRAP_5.name());
			}
			if (bootstrap5) {
				return BootstrapVersion.BOOTSTRAP_5;
			} else if (bootstrap4) {
				return BootstrapVersion.BOOTSTRAP_4;
			} else {
				return ((IBootstrapApplication) Application.get()).getBootstrapSettings().getDefaultVersion();
			}
		}
	}

	public static IBootstrapProvider getSettings() {
		switch (getVersion()) {
		case BOOTSTRAP_4:
			return ((IBootstrapApplication) Application.get()).getBootstrapSettings().getBootstrap4Provider();
		case BOOTSTRAP_5:
			return ((IBootstrapApplication) Application.get()).getBootstrapSettings().getBootstrap5Provider();
		default:
			throw new IllegalStateException();
		}
	}

	public static String getVariation() {
		switch (getVersion()) {
		case BOOTSTRAP_4:
			return "bs4";
		case BOOTSTRAP_5:
			return null;
		default:
			throw new IllegalStateException();
		}
	}

}
