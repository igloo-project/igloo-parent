package org.iglooproject.jpa.security.business.user.model;

/**
 * Minimal contract of the signed in user held by a session. Allows an application to store a
 * non-entity object (DTO...) in the session without exposing the sensitive information of {@link
 * IUser} (password hash...).
 */
public interface ISessionUser {

  String getUsername();
}
