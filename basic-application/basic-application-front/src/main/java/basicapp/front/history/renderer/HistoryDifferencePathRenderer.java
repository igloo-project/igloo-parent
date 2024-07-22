package basicapp.front.history.renderer;

import basicapp.back.business.history.model.HistoryDifference;
import com.google.common.base.Optional;
import igloo.wicket.model.Models;
import java.util.Locale;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HistoryDifferencePathRenderer
    extends AbstractHistoryRenderer<HistoryDifference> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(HistoryDifferencePathRenderer.class);

  private static final HistoryDifferencePathRenderer INSTANCE = new HistoryDifferencePathRenderer();

  public static HistoryDifferencePathRenderer get() {
    return INSTANCE;
  }

  private HistoryDifferencePathRenderer() {}

  @Override
  public String render(HistoryDifference difference, Locale locale) {
    FieldPath path = difference.getAbsolutePath();
    if (path.size() == 1 && path.isItem()) {
      return HistoryValueRenderer.get().render(difference.getPath().getKey(), locale);
    }

    String pathResourceKeyPart = getFieldPathKeyPart(path);
    IModel<?> keyDataModel = Models.transientModel(difference.getPath());
    String entityResourceKeyPart = getEntityResourceKeyPart(difference);

    Optional<String> result =
        getStringOptional(
            JOINER.join(HISTORY_DIFFERENCE_ROOT, entityResourceKeyPart, pathResourceKeyPart),
            locale,
            keyDataModel);
    if (!result.isPresent()) {
      result =
          getStringOptional(
              JOINER.join(BUSINESS_ROOT, entityResourceKeyPart, pathResourceKeyPart),
              locale,
              keyDataModel);
      if (!result.isPresent()) {
        result =
            getStringOptional(
                JOINER.join(HISTORY_DIFFERENCE_ROOT, ".common", pathResourceKeyPart),
                locale,
                keyDataModel);
        if (!result.isPresent()) {
          result =
              getStringOptional(
                  JOINER.join(BUSINESS_ROOT, pathResourceKeyPart), locale, keyDataModel);
        }
      }
    }

    if (result.isPresent()) {
      return result.get();
    } else {
      LOGGER.error(
          "Unable to find a localization key for the HistoryDifference (entity : '{}', path : '{}')",
          entityResourceKeyPart,
          pathResourceKeyPart);
      return pathResourceKeyPart;
    }
  }
}
