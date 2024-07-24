package org.iglooproject.jpa.more.test.junit.difference;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bindgen.Binding;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;

public final class TestHistoryDifferenceDescription {
  private final HistoryDifferenceEventType action;

  private final ListMultimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription>
      differences;

  public static Builder builder() {
    return new Builder();
  }

  /* package */ TestHistoryDifferenceDescription(HistoryDifferenceEventType action) {
    super();
    this.action = action;
    this.differences = ImmutableListMultimap.of();
  }

  /* package */ TestHistoryDifferenceDescription(
      HistoryDifferenceEventType action,
      Multimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> differences) {
    super();
    this.action = action;
    this.differences = ImmutableListMultimap.copyOf(differences);
  }

  public HistoryDifferenceEventType getAction() {
    return action;
  }

  public ListMultimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> getDifferences() {
    return differences;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TestHistoryDifferenceDescription) {
      TestHistoryDifferenceDescription other = (TestHistoryDifferenceDescription) obj;
      return new EqualsBuilder()
          .append(action, other.action)
          .append(differences, other.differences)
          .build();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(action).append(differences).build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .append("action", action)
        .append("differences", differences)
        .build();
  }

  public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
    protected final ImmutableListMultimap.Builder<
            TestHistoryDifferenceKey, TestHistoryDifferenceDescription>
        delegate = ImmutableListMultimap.builder();

    private AbstractBuilder() {}

    protected T put(
        FieldPath path, Object key, TestHistoryDifferenceDescription differenceDescription) {
      delegate.put(new TestHistoryDifferenceKey(path, key), differenceDescription);
      return thisAsT();
    }

    public T put(FieldPath path, HistoryDifferenceEventType action) {
      return put(path, null, new TestHistoryDifferenceDescription(action));
    }

    public T putItem(FieldPath path, Object key, HistoryDifferenceEventType action) {
      return put(toItem(path), key, new TestHistoryDifferenceDescription(action));
    }

    public T put(Binding<?> binding, HistoryDifferenceEventType action) {
      return put(
          FieldPath.fromBinding(binding), null, new TestHistoryDifferenceDescription(action));
    }

    public T putItem(Binding<?> binding, Object key, HistoryDifferenceEventType action) {
      return putItem(FieldPath.fromBinding(binding), key, action);
    }

    public ContainedBuilder<T> putComposite(FieldPath path, HistoryDifferenceEventType action) {
      return new ContainedBuilder<>(thisAsT(), path, null, action);
    }

    public ContainedBuilder<T> putItemComposite(
        FieldPath path, Object key, HistoryDifferenceEventType action) {
      return new ContainedBuilder<>(thisAsT(), toItem(path), key, action);
    }

    public ContainedBuilder<T> putComposite(Binding<?> binding, HistoryDifferenceEventType action) {
      return new ContainedBuilder<>(thisAsT(), FieldPath.fromBinding(binding), null, action);
    }

    public ContainedBuilder<T> putItemComposite(
        Binding<?> binding, Object key, HistoryDifferenceEventType action) {
      return putItemComposite(FieldPath.fromBinding(binding), key, action);
    }

    private static FieldPath toItem(FieldPath path) {
      return path.isItem() ? path : path.item();
    }

    protected abstract T thisAsT();
  }

  public static class Builder extends AbstractBuilder<Builder> {

    private Builder() {}

    @Override
    protected Builder thisAsT() {
      return this;
    }

    public ListMultimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> build() {
      return delegate.build();
    }
  }

  public static class ContainedBuilder<T extends AbstractBuilder<T>>
      extends AbstractBuilder<ContainedBuilder<T>> {
    private final T caller;
    private final FieldPath path;
    private final Object key;
    private final HistoryDifferenceEventType action;

    private ContainedBuilder(
        T caller, FieldPath path, Object key, HistoryDifferenceEventType action) {
      this.caller = caller;
      this.path = path;
      this.key = key;
      this.action = action;
    }

    public T and() {
      return caller.put(path, key, new TestHistoryDifferenceDescription(action, delegate.build()));
    }

    @Override
    protected ContainedBuilder<T> thisAsT() {
      return this;
    }
  }
}
