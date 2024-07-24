package igloo.bootstrap.woption;

import com.google.common.collect.Lists;

public final class WVisitables {

  public static void visit(IWVisitor visitor, IWVisitable visitable) {
    visit(visitor, visitable);
  }

  public static void visit(IWVisitor visitor, IWVisitable visitable, IWVisitable... others) {
    visit(visitor, Lists.asList(visitable, others));
  }

  public static void visit(IWVisitor visitor, Iterable<? extends IWVisitable> visitables) {
    if (visitor == null) {
      return;
    }
    for (IWVisitable visitable : visitables) {
      if (visitable != null) {
        visitor.visit(visitable);
      }
    }
  }

  public static void accept(IWVisitor visitor, IWVisitable visitable) {
    if (visitor != null && visitable != null) {
      visitable.accept(visitor);
    }
  }
}
