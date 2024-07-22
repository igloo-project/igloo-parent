package org.iglooproject.config.bootstrap.spring.annotations;

public final class IglooPropertySourcePriority {

  /**
   * Level dedicated to exceptional overrides ; added to allow to override bootstrap configurations.
   */
  public static final String OVERRIDES = "igloo/overrides";

  /** Level dedicated to your bootstrap configuration (igloo.configurationLocations). */
  public static final String BOOTSTRAP = "igloo/bootstrap";

  /** Level dedicated to your application defaults. */
  public static final String APPLICATION = "igloo/application";

  /** Level dedicated to Igloo framework defaults. */
  public static final String FRAMEWORK = "igloo/framework";

  /** Level dedicated to component defaults. */
  public static final String COMPONENT = "igloo/component";

  private IglooPropertySourcePriority() {}
  ;
}
