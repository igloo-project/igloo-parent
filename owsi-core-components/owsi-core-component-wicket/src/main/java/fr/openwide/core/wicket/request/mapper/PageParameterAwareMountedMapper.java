package fr.openwide.core.wicket.request.mapper;

import java.lang.reflect.Method;

import org.apache.wicket.Application;
import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.core.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.handler.PageAndComponentProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.ComponentInfo;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.IProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class comes from: https://github.com/unterstein/wicket-tales/
 * 
 * This mapper represents exactly the same behavior like the default
 * {@link MountedMapper}, except one behavior:<br/>
 * The default {@link MountedMapper} implementation prefers the pageId parameter
 * with a higher priority then the rest of the {@link PageParameters}.<br>
 * This preference leads to the following behavior:
 * 
 * <pre>
 * ...myApplication/User/4?15
 * </pre>
 * 
 * leads to the UserPage with the indexed {@link PageParameters} "4" at index 0.
 * So the User with id 4 is shown. When a user manually changes the URL to
 * something like this:
 * 
 * <pre>
 * ...myApplication/User/5?15
 * </pre>
 * 
 * The {@link MountedMapper} would deliver the same page as before and would
 * show the user with id 4 and not - like it is implicated in the url - the user
 * with id 5, because the pageId parameter is preferred during mapping process. <br/>
 * <br/>
 * Therefore this implementation compares the existing {@link PageParameters} of
 * the cached page and the {@link PageParameters} of the current request. If a
 * difference between this two {@link PageParameters} are recognized, this
 * mapper redirects to a fresh bookmarkable instance of the current requested
 * page. <br/>
 * <br/>
 * To use this mapper to mount your pages, you must declare the mapper
 * programmatically in your {@link Application} by overwriting the
 * "public void init()" method, like shown below:
 * 
 * <pre>
 * &#064;Override
 * public void init() {
 * 	super.init();
 * 	// Use our own mapper to mount the mountpage
 * 	mount(new PageParameterAwareMountedMapper(&quot;Home&quot;, HomePage.class));
 * }
 * </pre>
 * 
 * @author <a href="mailto:unterstein@me.com">Johannes Unterstein</a>
 * 
 */
public class PageParameterAwareMountedMapper extends MountedMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PageParameterAwareMountedMapper.class);
	
	public PageParameterAwareMountedMapper(String mountPath, Class<? extends IRequestablePage> pageClass,
			IPageParametersEncoder pageParametersEncoder) {
		super(mountPath, pageClass, pageParametersEncoder);
	}

	public PageParameterAwareMountedMapper(String mountPath, Class<? extends IRequestablePage> pageClass) {
		super(mountPath, pageClass);
	}

	public PageParameterAwareMountedMapper(String mountPath,
			IProvider<Class<? extends IRequestablePage>> pageClassProvider, IPageParametersEncoder pageParametersEncoder) {
		super(mountPath, pageClassProvider, pageParametersEncoder);
	}

	public PageParameterAwareMountedMapper(String mountPath,
			IProvider<Class<? extends IRequestablePage>> pageClassProvider) {
		super(mountPath, pageClassProvider);
	}

	@Override
	protected IRequestHandler processHybrid(PageInfo pageInfo, Class<? extends IRequestablePage> pageClass,
			PageParameters pageParameters, Integer renderCount) {
		IRequestHandler handler = super.processHybrid(pageInfo, pageClass, pageParameters, renderCount);
		if (handler instanceof RenderPageRequestHandler) {
			// in the current implementation (wicket 1.5.6) super.processHybrid
			// returns a RenderPageRequestHandler
			RenderPageRequestHandler renderPageHandler = (RenderPageRequestHandler) handler;
			if (renderPageHandler.getPageProvider() instanceof PageProvider) {
				PageProvider provider = (PageProvider) renderPageHandler.getPageProvider();
				// This check is necessary to prevent a
				// RestartResponseAtInterceptPageException at the wrong time in
				// request cycle
				if (provider.hasPageInstance()) {
					PageParameters newPageParameters = renderPageHandler.getPageParameters();
					PageParameters oldPageParameters = renderPageHandler.getPageProvider().getPageInstance()
							.getPageParameters();
					// if we recognize a change between the page parameter of
					// the loaded
					// page and the page parameter of the current request, we
					// redirect
					// to a fresh bookmarkable instance of that page.
					if (!PageParameters.equals(oldPageParameters, newPageParameters)) {
						handler = processBookmarkable(pageClass, newPageParameters);
					}
				}
			}
		}
		return handler;
	}
	
	@Override
	/**
	 * See https://issues.apache.org/jira/browse/WICKET-5539.
	 * 
	 * This implementation:
	 * - doesn't set the page parameters to null if there is a page id: a page id 
	 * - overwrite the page parameters with the ones of the page instance if we can find a valid page instance
	 */
	protected IRequestHandler processListener(PageComponentInfo pageComponentInfo,
			Class<? extends IRequestablePage> pageClass, PageParameters pageParameters) {
		PageInfo pageInfo = pageComponentInfo.getPageInfo();
		ComponentInfo componentInfo = pageComponentInfo.getComponentInfo();
		Integer renderCount = null;
		RequestListenerInterface listenerInterface = null;

		if (componentInfo != null) {
			renderCount = componentInfo.getRenderCount();
			listenerInterface = requestListenerInterfaceFromString(componentInfo.getListenerInterface());
		}

		if (listenerInterface != null) {
			PageAndComponentProvider provider = new PageAndComponentProvider(pageInfo.getPageId(), pageClass,
					pageParameters, renderCount, componentInfo.getComponentPath());
			if (!provider.isNewPageInstance()) {
				try {
					Method setPageParametersMethod = PageProvider.class.getDeclaredMethod("setPageParameters",
							PageParameters.class);
					setPageParametersMethod.setAccessible(true);
					setPageParametersMethod.invoke(provider, provider.getPageInstance().getPageParameters());
				} catch (Exception e) {
					LOGGER.error("Error while trying to override the PageParameters of the provider with the instance PageParameters", e);
				}
			}

			provider.setPageSource(getContext());

			return new ListenerInterfaceRequestHandler(provider, listenerInterface, componentInfo.getBehaviorId());
		} else {
			if (LOGGER.isWarnEnabled()) {
				if (componentInfo != null) {
					LOGGER.warn("Unknown listener interface '{}'", componentInfo.getListenerInterface());
				} else {
					LOGGER.warn("Cannot extract the listener interface for PageComponentInfo: '{}'" + pageComponentInfo);
				}
			}
			return null;
		}
	}
}
