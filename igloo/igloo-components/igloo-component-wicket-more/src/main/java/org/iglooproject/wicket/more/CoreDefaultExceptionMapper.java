package org.iglooproject.wicket.more;

import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

public class CoreDefaultExceptionMapper implements IExceptionMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoreDefaultExceptionMapper.class);

  private static final IExceptionMapper DELEGATE = new DefaultExceptionMapper();

  @Override
  public IRequestHandler map(Exception e) {
    if (e instanceof AuthorizationException) {
      /*
       * Make Spring Security work for us: it will save the original request for later redirect and redirect
       * to the login page
       */
      throw new AccessDeniedException("Access denied by Wicket's security layer", e);
    }
    try {
      return DELEGATE.map(e);
    } catch (RuntimeException e2) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.error("An error occurred while handling a previous error: " + e2.getMessage(), e2);
      }

      // We were already handling an exception! give up
      LOGGER.error("unexpected exception when handling another exception: " + e.getMessage(), e);
      return new ErrorCodeRequestHandler(500);
    }
  }
}
