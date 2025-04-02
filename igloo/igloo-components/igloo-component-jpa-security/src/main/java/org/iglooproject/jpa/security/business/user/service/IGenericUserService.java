package org.iglooproject.jpa.security.business.user.service;

import java.util.Locale;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.user.model.GenericUser;

public interface IGenericUserService<U extends GenericUser<U, ?>>
    extends IGenericEntityService<Long, U>, ISecurityUserService<U> {

  void setEnabled(U user, boolean enabled) throws ServiceException, SecurityServiceException;

  /**
   * Encode and set Password to user.
   *
   * <p>check that Password cannot be more than 72 bytes.
   *
   * @see <a href="https://spring.io/security/cve-2025-22228">CVE-2025-22228</a>
   */
  void setPasswords(U user, String clearTextPassword)
      throws ServiceException, SecurityServiceException;

  void updateLastLoginDate(U user) throws ServiceException, SecurityServiceException;

  void updateLocale(U user, Locale locale) throws ServiceException, SecurityServiceException;

  void addAuthority(U user, Authority authority) throws ServiceException, SecurityServiceException;

  void addAuthority(U user, String authorityName) throws ServiceException, SecurityServiceException;
}
