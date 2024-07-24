package org.iglooproject.wicket.more.module;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;

public interface IWicketHeaderModule {

  default void renderHead(Component component, IHeaderResponse response) {
    // nothing to do
  }
}
