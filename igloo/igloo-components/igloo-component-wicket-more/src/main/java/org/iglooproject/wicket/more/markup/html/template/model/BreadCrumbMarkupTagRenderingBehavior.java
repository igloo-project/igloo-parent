package org.iglooproject.wicket.more.markup.html.template.model;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/** Type-safe enum pattern. */
public class BreadCrumbMarkupTagRenderingBehavior extends Behavior {

  private static final long serialVersionUID = 3956060849411223519L;

  public static final BreadCrumbMarkupTagRenderingBehavior HTML_HEAD =
      new BreadCrumbMarkupTagRenderingBehavior() {
        private static final long serialVersionUID = -4178004922336641567L;

        @Override
        public void beforeRender(Component component) {
          component.setRenderBodyOnly(true);
        }
      };

  public static final BreadCrumbMarkupTagRenderingBehavior HTML_BODY =
      new BreadCrumbMarkupTagRenderingBehavior() {
        private static final long serialVersionUID = 7310978949663064632L;

        // No specific behavior
      };

  private BreadCrumbMarkupTagRenderingBehavior() {}
}
