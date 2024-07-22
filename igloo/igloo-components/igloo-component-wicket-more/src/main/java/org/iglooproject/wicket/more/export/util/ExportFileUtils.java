package org.iglooproject.wicket.more.export.util;

import igloo.wicket.model.BindingModel;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.wicket.Application;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;

public class ExportFileUtils {

  public static final String FILE_NAME_DATE_PATTERN_KEY = "common.action.export.file.datePattern";

  public static IModel<String> getFileNameMediaTypeModel(
      IModel<String> fileNamePrefixModel, IModel<MediaType> mediaTypeModel) {
    return getFileNameExtensionModel(
        fileNamePrefixModel,
        BindingModel.of(mediaTypeModel, CoreWicketMoreBindings.mediaType().extension()));
  }

  public static IModel<String> getFileNameExtensionModel(
      final IModel<String> fileNamePrefixModel, final IModel<String> extensionModel) {
    return new IModel<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        return new StringBuilder(fileNamePrefixModel.getObject())
            .append(
                DateTimeFormatter.ofPattern(
                        Application.get()
                            .getResourceSettings()
                            .getLocalizer()
                            .getString(FILE_NAME_DATE_PATTERN_KEY, null))
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now()))
            .append(".")
            .append(extensionModel.getObject())
            .toString();
      }
    };
  }

  private ExportFileUtils() {}
}
