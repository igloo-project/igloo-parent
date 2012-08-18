package fr.openwide.core.wicket.more;

import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.wicket.more.request.cycle.RequestCycleUtils;

public class CoreDefaultExceptionMapper implements IExceptionMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreDefaultExceptionMapper.class);
	
	private static final IExceptionMapper DELEGATE = new DefaultExceptionMapper();

	@Override
	public IRequestHandler map(Exception e) {
		try {
			if (e instanceof AuthorizationException) {
				// récupérer l'url + enregistrer dans la session
				
				if (!AuthenticatedWebSession.exists() || !AuthenticatedWebSession.get().isSignedIn()) {
					String currentUrl = RequestCycleUtils.getCurrentRequestUrl();
					if (currentUrl != null) {
						AbstractCoreSession.get().registerRedirectUrl(currentUrl);
					}
				}
			}
			
			return DELEGATE.map(e);
		} catch (RuntimeException e2) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("An error occurred while handling a previous error: " + e2.getMessage(), e2);
			}

			// hmmm, we were already handling an exception! give up
			LOGGER.error("unexpected exception when handling another exception: " + e.getMessage(), e);
			return new ErrorCodeRequestHandler(500);
		}
	}

}