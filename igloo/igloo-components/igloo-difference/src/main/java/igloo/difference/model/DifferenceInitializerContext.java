package igloo.difference.model;

import java.util.ArrayDeque;

public class DifferenceInitializerContext {
  private final ArrayDeque<Object> items = new ArrayDeque<>();
  private final ArrayDeque<DifferenceField> rootFields = new ArrayDeque<>();

  public void push(DifferenceField rootField, Object object) {
    items.push(object);
    rootFields.push(rootField);
  }

  public void pop() {
    items.pop();
    rootFields.pop();
  }

  public Object peekRootValue() {
    return items.peek();
  }

  public DifferenceField peekRootField() {
    return rootFields.peek();
  }

  public boolean isEmpty() {
    return rootFields.isEmpty() && items.isEmpty();
  }
}
