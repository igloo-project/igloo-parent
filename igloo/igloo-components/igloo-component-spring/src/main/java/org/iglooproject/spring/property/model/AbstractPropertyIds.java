package org.iglooproject.spring.property.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractPropertyIds {

  private static ConcurrentMap<String, PropertyRegistryKeyClassDeclaration>
      declarationsByDeclaringClassName = new ConcurrentHashMap<>();

  protected AbstractPropertyIds() {}

  public static <T> ImmutablePropertyId<T> immutable(String key) {
    PropertyRegistryKeyClassDeclaration declaration = getDeclaration();
    ImmutablePropertyId<T> id = new ImmutablePropertyId<>(declaration, key);
    declaration.addKey(id);
    return id;
  }

  public static <T> MutablePropertyId<T> mutable(String key) {
    PropertyRegistryKeyClassDeclaration declaration = getDeclaration();
    MutablePropertyId<T> id = new MutablePropertyId<>(declaration, key);
    declaration.addKey(id);
    return id;
  }

  public static <T> ImmutablePropertyIdTemplate<T> immutableTemplate(String key) {
    PropertyRegistryKeyClassDeclaration declaration = getDeclaration();
    ImmutablePropertyIdTemplate<T> template = new ImmutablePropertyIdTemplate<>(declaration, key);
    declaration.addKey(template);
    return template;
  }

  public static <T> MutablePropertyIdTemplate<T> mutableTemplate(String key) {
    PropertyRegistryKeyClassDeclaration declaration = getDeclaration();
    MutablePropertyIdTemplate<T> template = new MutablePropertyIdTemplate<>(declaration, key);
    declaration.addKey(template);
    return template;
  }

  private static PropertyRegistryKeyClassDeclaration getDeclaration() {
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    String callingClassName = stackTraceElements[3].getClassName();
    return getDeclaration(callingClassName);
  }

  private static PropertyRegistryKeyClassDeclaration getDeclaration(String declaringClassName) {
    PropertyRegistryKeyClassDeclaration declaration =
        new PropertyRegistryKeyClassDeclaration(declaringClassName);
    return MoreObjects.firstNonNull(
        declarationsByDeclaringClassName.putIfAbsent(declaringClassName, declaration), declaration);
  }

  /*
   * No need to override equals: there is only one instance of this class for each declaringClassName, due to
   * how instantiation is done and to how serialization/deserialization are done.
   */
  private static final class PropertyRegistryKeyClassDeclaration
      implements IPropertyRegistryKeyDeclaration {
    private static final long serialVersionUID = 1L;

    private final String declaringClassName;

    private final Set<IPropertyRegistryKey<?>> declaredKeys = Sets.newLinkedHashSet();

    public PropertyRegistryKeyClassDeclaration(String declaringClass) {
      super();
      this.declaringClassName = declaringClass;
    }

    @Override
    public String toString() {
      return "Properties declared in class " + declaringClassName;
    }

    @Override
    public Set<IPropertyRegistryKey<?>> getDeclaredKeys() {
      return Collections.unmodifiableSet(declaredKeys);
    }

    public void addKey(IPropertyRegistryKey<?> key) {
      synchronized (declaredKeys) {
        declaredKeys.add(key);
      }
    }

    protected Object writeReplace() {
      return new SerializedForm(this);
    }

    private static final class SerializedForm implements Serializable {
      private static final long serialVersionUID = 1L;

      private String declaringClassName;

      public SerializedForm(PropertyRegistryKeyClassDeclaration declaration) {
        super();
        this.declaringClassName = declaration.declaringClassName;
      }

      protected Object readResolve() {
        return getDeclaration(declaringClassName);
      }
    }
  }
}
