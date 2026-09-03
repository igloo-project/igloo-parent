package igloo.jwt.service;

import igloo.jwt.exception.ConvertTokenException;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IJwtToPrincipalConverter<P> {
  P convert(Jwt jwt) throws ConvertTokenException;
}
