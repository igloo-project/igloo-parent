package igloo.jwt.service;

import igloo.jwt.exception.ConvertTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface IJwtToPrincipalConverter<P> {
  P convert(Jws<Claims> claims) throws ConvertTokenException;
}
