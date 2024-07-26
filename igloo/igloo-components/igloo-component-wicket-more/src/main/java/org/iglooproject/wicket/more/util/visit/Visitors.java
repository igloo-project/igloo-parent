package org.iglooproject.wicket.more.util.visit;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public final class Visitors {

  private Visitors() {}

  public static IVisitor<Component, Void> addToTarget(AjaxRequestTarget target) {
    return new AddToTargetVisitor(target);
  }

  private static final class AddToTargetVisitor implements IVisitor<Component, Void> {
    private final AjaxRequestTarget target;

    public AddToTargetVisitor(AjaxRequestTarget target) {
      super();
      this.target = target;
    }

    @Override
    public void component(Component object, IVisit<Void> visit) {
      target.add(object);
      visit.dontGoDeeper();
    }
  }
}
