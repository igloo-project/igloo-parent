package org.iglooproject.wicket.more.util.validate.validators;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.IMediaTypeRegistry;

public class FileUploadMediaTypeValidator extends Behavior implements IValidator<List<FileUpload>> {

  private static final long serialVersionUID = 1507793945782623835L;

  private static final String ACCEPT_ATTRIBUTE = "accept";

  private final List<IMediaType> mediaTypes;

  private String errorResourceKey;

  @SpringBean private IMediaTypeRegistry mediaTypeRegistry;

  public FileUploadMediaTypeValidator(Collection<? extends IMediaType> mediaTypes) {
    this.mediaTypes = List.copyOf(Args.notNull(mediaTypes, "mediaTypes"));
    Injector.get().inject(this);
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
      IMediaType fileUploadMediaType =
          mediaTypeRegistry.fromFilename(fileUpload.getClientFileName());

      if (!mediaTypeRegistry.accepts(mediaTypes, fileUploadMediaType)) {
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

  private Stream<IMediaType> canonicalMediaTypes() {
    return mediaTypes.stream().map(mediaTypeRegistry::canonical).distinct();
  }

  private String extensions() {
    return canonicalMediaTypes()
        .map(IMediaType::extension)
        .filter(Objects::nonNull)
        .collect(Collectors.joining(", "));
  }

  private String accept() {
    return Stream.concat(
            canonicalMediaTypes()
                .flatMap(mediaType -> mediaType.extensions().stream())
                .map("."::concat),
            canonicalMediaTypes().map(IMediaType::mime))
        .distinct()
        .collect(Collectors.joining(","));
  }
}
