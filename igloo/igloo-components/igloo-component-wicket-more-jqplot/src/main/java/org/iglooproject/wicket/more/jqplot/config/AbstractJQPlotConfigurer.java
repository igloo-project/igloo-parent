package org.iglooproject.wicket.more.jqplot.config;

/** Abstract base class for inline implementations of {@link IJQPlotConfigurer} */
public abstract class AbstractJQPlotConfigurer<S, K> implements IJQPlotConfigurer<S, K> {

  private static final long serialVersionUID = 4674594835911474833L;

  @Override
  public void detach() {
    // Does nothing.
    // To be overriden by subclasses.
  }
}
