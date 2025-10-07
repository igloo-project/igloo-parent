package test;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Locator;

/**
 * TODO: test with mocks:
 *
 * <ul>
 *   <li>Basic generation, creation callback is called
 *   <li>Basic validation
 *   <li>Token customization
 *   <li>Custom validation: requireIssuer
 *   <li>Custom validation: custom callback is called
 *   <li>Custom validation: custom callback is called and interrupts validation
 *   <li>Custom validation: custom callback is called and {@link RuntimeException} is wrapped in
 *       {@link JwtException}
 *   <li>Custom validation: fails if signature not in {@link Locator}
 * </ul>
 */
public class TestJwtTokenService {}
