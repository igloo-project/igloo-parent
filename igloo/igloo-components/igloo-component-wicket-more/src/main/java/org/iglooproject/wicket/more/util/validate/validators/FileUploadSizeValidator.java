package org.iglooproject.wicket.more.util.validate.validators;

import java.util.List;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Checks that, for each uploaded file, the file size is smaller than or equal to a maximum expected
 * size.
 *
 * <p>Resource key: {@code <class.simpleName>}
 *
 * <p>Error Message Variables:
 *
 * <ul>
 *   <li>the default variables (see {@link FormComponent})
 *   <li>{@code maximum}: the maximum allowed value (as a {@link Bytes} object - see {@link
 *       BytesConverter} to display)
 *   <li>{@code clientFileName}: the name of the invalid file
 *   <li>{@code size}: the size of the invalid file (as a {@link Bytes} object - see {@link
 *       BytesConverter} to display)
 * </ul>
 */
public class FileUploadSizeValidator implements IValidator<List<FileUpload>> {

  private static final long serialVersionUID = 3922765020138195477L;

  protected final Bytes maximum;

  private String errorResourceKey;

  public FileUploadSizeValidator(Bytes maximum) {
    this.maximum = maximum;
  }

  /**
   * Allows subclasses to decorate reported errors
   *
   * @param error
   * @param validatable
   * @return decorated error
   */
  protected ValidationError decorate(
      ValidationError error, IValidatable<List<FileUpload>> validatable) {
    return error;
  }

  @Override
  public void validate(IValidatable<List<FileUpload>> validatable) {
    for (FileUpload fileUpload : validatable.getValue()) {
      Bytes fileSize = Bytes.bytes(fileUpload.getSize());
      if (fileSize.greaterThan(maximum)) {
        ValidationError error = new ValidationError();
        if (errorResourceKey != null) {
          error.addKey(errorResourceKey);
        }
        error.addKey(this);
        error.setVariable("maximum", maximum);
        error.setVariable("clientFileName", fileUpload.getClientFileName());
        error.setVariable("size", fileSize);
        validatable.error(error);
      }
    }
  }

  public FileUploadSizeValidator setErrorResourceKey(String errorResourceKey) {
    this.errorResourceKey = errorResourceKey;
    return this;
  }
}
