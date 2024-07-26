package org.iglooproject.test.wicket.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.Component;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Extra helpers to execute test with {@link WicketTester}.
 *
 * <p>Please give attention to modified {@link CoreWicketTester#assertDisabled(String)}
 * implementation. We both check that component is <b>disabled</b> and <b>visible</b>. Wicket
 * implementation checks only that component is disabled.
 */
public class CoreWicketTester extends WicketTester {

  public CoreWicketTester(final WebApplication application) {
    super(application);
  }

  /** Assert that a Component is visible and of type clazz */
  public void assertVisible(String path, Class<?> clazz) {
    assertVisible(path);
    assertThat(assertExists(path)).isInstanceOf(clazz);
  }

  /** Assert that a Component is visible and/or enabled */
  public void assertUsability(String path) {
    assertUsability(getComponentFromLastRenderedPage(path));
  }

  /** Assert that a Component is visible and/or enabled and of type clazz */
  @SuppressWarnings("unchecked")
  public <T extends Component> T assertUsability(String path, Class<T> clazz) {
    assertUsability(path);
    assertComponent(path, clazz);
    return (T) getComponentFromLastRenderedPage(path);
  }

  /**
   * Assert that a Component is visible and enabled. This behavior is different from {@link
   * WicketTester#assertEnabled(String)} that only checks enable status.
   */
  @Override
  public void assertEnabled(String path) {
    assertVisible(path);
    super.assertEnabled(path);
  }

  /** Assert that a Component is visible, enabled and of type clazz */
  @SuppressWarnings("unchecked")
  public <T extends Component> T assertEnabled(String path, Class<T> clazz) {
    assertEnabled(path);
    assertComponent(path, clazz);
    return (T) getComponentFromLastRenderedPage(path);
  }

  /**
   * Assert that a Component is visible and disabled. This behavior is different from {@link
   * WicketTester#assertDisabled(String)} that only checks disabled status.
   */
  @Override
  public void assertDisabled(String path) {
    assertVisible(path);
    super.assertDisabled(path);
  }

  /** Assert that a Component is visible, disabled and of type clazz */
  @SuppressWarnings("unchecked")
  public <T extends Component> T assertDisabled(String path, Class<T> clazz) {
    assertDisabled(path);
    assertComponent(path, clazz);
    return (T) getComponentFromLastRenderedPage(path);
  }
}
