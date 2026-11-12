package org.iglooproject.wicket.more.util.validate.validators;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.commons.util.mime.MediaType;

/**
 * Validates the extension of uploaded files against the given media types.
 *
 * <p>Also sets the {@code accept} attribute of the file input from these media types (extensions
 * and primary MIME types), unless the markup already defines one.
 */
public class FileUploadMediaTypeValidator extends Behavior implements IValidator<List<FileUpload>> {

  private static final long serialVersionUID = 1507793945782623835L;

  private static final String ACCEPT_ATTRIBUTE = "accept";

  private final List<MediaType> mediaTypes;

  private String errorResourceKey;

  public FileUploadMediaTypeValidator(Collection<MediaType> mediaTypes) {
    this.mediaTypes = List.copyOf(Args.notNull(mediaTypes, "mediaTypes"));
  }

  @Override
  public void onComponentTag(Component component, ComponentTag tag) {
    super.onComponentTag(component, tag);
    if (!tag.getAttributes().containsKey(ACCEPT_ATTRIBUTE)) {
      tag.put(ACCEPT_ATTRIBUTE, accept());
    }
  }

  @Override
  public void validate(IValidatable<List<FileUpload>> validatable) {
    for (FileUpload fileUpload : validatable.getValue()) {
      String fileUploadExtension = FilenameUtils.getExtension(fileUpload.getClientFileName());

      if (mediaTypes.stream().noneMatch(mediaType -> mediaType.supports(fileUploadExtension))) {
        ValidationError error = new ValidationError();
        if (errorResourceKey != null) {
          error.addKey(errorResourceKey);
        }
        error.addKey(this);
        error.setVariable("extensions", extensions());
        error.setVariable("clientFileName", fileUpload.getClientFileName());
        validatable.error(error);
      }
    }
  }

  public FileUploadMediaTypeValidator setErrorResourceKey(String errorResourceKey) {
    this.errorResourceKey = errorResourceKey;
    return this;
  }

  private String extensions() {
    return mediaTypes.stream()
        .map(MediaType::extension)
        .distinct()
        .collect(Collectors.joining(", "));
  }

  private String accept() {
    return Stream.concat(
            mediaTypes.stream()
                .flatMap(mediaType -> mediaType.extensions().stream())
                .map("."::concat),
            mediaTypes.stream().map(MediaType::mime))
        .distinct()
        .collect(Collectors.joining(","));
  }
}
