package basicapp.back.config.spring;

/**
 * Constants regarding the execution order of Spring's advices (those added by <code>@Cache</code>,
 * <code>@Transactional</code>, <code>@PreAuthorize</code>, and so on).
 */
public final class BasicApplicationAdviceOrder {

  private BasicApplicationAdviceOrder() {}

  /**
   * DON'T CHANGE THIS, it won't have any effect. See <code>igloo-component-jpa-security-context.xml
   * </code> in Igloo for the actual order definition.
   */
  public static final int SECURITY = 0;

  /**
   * THIS CONSTANT IS NOT USED, initially. If you add an <code>@EnableCaching</code> annotation to
   * your JavaConfig, please reference this constant. Caches are used only if the security checks (
   * <code>@PreAuthorize</code>) pass, so as not to bypass security. Also, <code>@PostAuthorize
   * </code> security checks are applied to results taken from the cache.
   */
  public static final int CACHE = 1;

  /** Transactions are opened only when there is no cache entry, so as to avoid performance hit. */
  public static final int TRANSACTION = 2;
}
