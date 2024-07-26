package org.iglooproject.wicket.more.util.validate.validators;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.commons.util.mime.MediaType;

public class FileUploadMediaTypeValidator implements IValidator<List<FileUpload>> {

  private static final long serialVersionUID = 1507793945782623835L;

  private final List<MediaType> mediaTypes;

  private String errorResourceKey;

  public FileUploadMediaTypeValidator(Collection<MediaType> mediaTypes) {
    this.mediaTypes = ImmutableList.copyOf(Args.notNull(mediaTypes, "mediaTypes"));
  }

  @Override
  public void validate(IValidatable<List<FileUpload>> validatable) {
    for (FileUpload fileUpload : validatable.getValue()) {
      String fileUploadExtension = FilenameUtils.getExtension(fileUpload.getClientFileName());
      MediaType fileUploadMediaType = MediaType.fromExtension(fileUploadExtension);

      if (fileUploadMediaType == null || !mediaTypes.contains(fileUploadMediaType)) {
        ValidationError error = new ValidationError();
        if (errorResourceKey != null) {
          error.addKey(errorResourceKey);
        }
        error.addKey(this);
        error.setVariable(
            "extensions",
            Joiner.on(", ")
                .skipNulls()
                .join(mediaTypes.stream().map(input -> input.extension()).iterator()));
        error.setVariable("clientFileName", fileUpload.getClientFileName());
        validatable.error(error);
      }
    }
  }

  public FileUploadMediaTypeValidator setErrorResourceKey(String errorResourceKey) {
    this.errorResourceKey = errorResourceKey;
    return this;
  }
}
