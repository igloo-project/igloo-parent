package igloo.difference;

import com.google.common.base.Preconditions;
import igloo.difference.model.DifferenceField;
import igloo.difference.model.DifferenceFields;
import igloo.difference.model.DifferenceInitializerContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bindgen.BindingRoot;
import org.hibernate.proxy.HibernateProxy;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;
import org.iglooproject.jpa.util.HibernateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DifferenceFieldsProxyInitializer implements IProxyInitializer<Object> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DifferenceFieldsProxyInitializer.class);

  private final DifferenceField rootField;
  private final DifferenceFields fields;

  public DifferenceFieldsProxyInitializer(DifferenceFields fields) {
    this.fields = fields;
    this.rootField = fields.getFieldList().get(0);
    Preconditions.checkArgument(rootField.getPath().isRoot(), "First field must be root field");
  }

  @Override
  public void initialize(Object rootValue) {
    if (rootValue == null) {
      return;
    }
    List<DifferenceField> fieldList = new ArrayList<>();
    fieldList.addAll(fields.getFieldList());
    DifferenceField skippedRootField = fieldList.get(0);
    Preconditions.checkArgument(
        skippedRootField.getPath().isRoot(), "First field must be root field");
    fieldList.remove(0);

    DifferenceInitializerContext context = new DifferenceInitializerContext();
    context.push(skippedRootField, rootValue);
    initialize(fieldList, context);
    context.pop();
    Preconditions.checkArgument(context.isEmpty(), "Context must be empty after initialization");
  }

  private void initialize(
      List<DifferenceField> remainingFields, DifferenceInitializerContext context) {
    while (!remainingFields.isEmpty()) {
      DifferenceField field = remainingFields.remove(0);
      if (!isChildOf(field, context.peekRootField())) {
        return;
      }
      if (field.isContainer()) {
        // container: collect all childFields
        // * remove from remaining fields
        // * store in collectionChildFields to call with each item
        List<DifferenceField> collectionChildFields = collectChildFields(field, remainingFields);
        LOGGER.debug("Initialized collection: {}", field);
        Object initializedValue = initialize(field, context.peekRootValue());
        HibernateUtils.initialize(initializedValue);
        if (initializedValue instanceof Collection<?> collection) {
          for (Object collectionItem : collection) {
            // perform item initialization
            initializeItem(field, collectionItem);

            // copy field list
            List<DifferenceField> collectionChildFieldsCopy = new ArrayList<>();
            collectionChildFieldsCopy.addAll(collectionChildFields);
            context.push(field, collectionItem);
            initialize(collectionChildFieldsCopy, context);
            context.pop();
          }
        } else if (initializedValue != null) {
          throw new IllegalStateException(
              "Unexpected type %s".formatted(initializedValue.getClass().getName()));
        }
      } else {
        // initialize property
        Object initializedValue = initialize(field, context.peekRootValue());
        HibernateUtils.initialize(initializedValue);
        initializeItem(field, initializedValue);
      }
    }
  }

  private void initializeItem(DifferenceField field, Object initializedValue) {
    if (field.getInitializer() != null && initializedValue != null) {
      // proxy initializer cast so that it accepts untyped initializedValue
      @SuppressWarnings("unchecked")
      IProxyInitializer<Object> unsafeInitializer =
          (IProxyInitializer<Object>) field.getInitializer();
      unsafeInitializer.initialize(initializedValue);
    }
  }

  private List<DifferenceField> collectChildFields(
      DifferenceField currentField, List<DifferenceField> remainingField) {
    List<DifferenceField> childFields = new ArrayList<>();
    while (!remainingField.isEmpty()) {
      DifferenceField field = remainingField.get(0);
      if (isChildOf(field, currentField)) {
        childFields.add(remainingField.remove(0));
      } else {
        break;
      }
    }
    return childFields;
  }

  /** Fetch field from a root value. */
  private Object initialize(DifferenceField field, Object rootValue) {
    BindingRoot<Object, ?> unsafedBinding = getBinding(field);
    try {
      Object fieldValue = unsafedBinding.getSafelyWithRoot(rootValue);
      if (fieldValue instanceof HibernateProxy) {
        LOGGER.debug("Initialized value: {}", field);
        Object unwrapped = HibernateUtils.unwrap(fieldValue);
        HibernateUtils.initialize(unwrapped);
      }
      return fieldValue;
    } catch (RuntimeException e) {
      LOGGER.error("Failed to load {} on {}", field, rootValue);
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private BindingRoot<Object, ?> getBinding(DifferenceField field) {
    return (BindingRoot<Object, ?>) field.getBinding();
  }

  private boolean isChildOf(DifferenceField child, DifferenceField parent) {
    DifferenceField effectiveParent = child;
    do {
      effectiveParent = effectiveParent.getParent();
      if (parent.equals(effectiveParent)) {
        return true;
      }
    } while (!effectiveParent.getPath().isRoot());
    return false;
  }
}
