package basicapp.front.history.renderer;

import basicapp.back.business.history.model.HistoryDifference;
import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.user.model.User;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import igloo.wicket.renderer.Renderer;
import java.util.Map;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.fieldpath.FieldPathComponent;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

public abstract class AbstractHistoryRenderer<T> extends Renderer<T> {

  private static final long serialVersionUID = 1447404930317013473L;

  protected static final Joiner JOINER = Joiner.on("");

  protected static final String BUSINESS_ROOT = "business";

  protected static final String HISTORY_DIFFERENCE_ROOT = "history.difference";

  private static final Map<Class<?>, String> ENTITY_RESOURCE_KEY_PARTS =
      ImmutableMap.<Class<?>, String>builder().put(User.class, ".user").build();

  protected static String getEntityResourceKeyPart(HistoryDifference difference) {
    HistoryLog log = difference.getRootLog();
    if (log != null) {
      String part = getEntityResourceKeyPart(log.getMainObject());
      if (part != null) {
        return part;
      }
    }
    return "";
  }

  private static String getEntityResourceKeyPart(HistoryValue object) {
    GenericEntityReference<?, ?> reference = object.getReference();
    if (reference != null) {
      Class<?> objectClass = object.getReference().getType();
      return ENTITY_RESOURCE_KEY_PARTS.get(objectClass);
    }
    return null;
  }

  protected static String getFieldPathKeyPart(FieldPath path) {
    return path.toString().replace(FieldPathComponent.ITEM.getName(), "");
  }
}
