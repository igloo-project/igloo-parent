package igloo.jwt.service;

import igloo.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import java.util.function.Consumer;

public interface IJwtTokenService<P> {

  Jws<Claims> verifyToken(String token) throws InvalidTokenException;

  String issueToken(P principal);

  String issueToken(P principal, Consumer<JwtBuilder> builderConsumer);
}
