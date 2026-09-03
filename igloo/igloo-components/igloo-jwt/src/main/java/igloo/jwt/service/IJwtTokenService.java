package igloo.jwt.service;

import igloo.jwt.exception.InvalidTokenException;
import java.util.function.Consumer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

public interface IJwtTokenService<P> {

  Jwt verifyToken(String token) throws InvalidTokenException;

  String issueToken(P principal);

  String issueToken(P principal, Consumer<JwtClaimsSet.Builder> builderConsumer);
}
