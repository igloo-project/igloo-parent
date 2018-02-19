package org.iglooproject.wicket.more.util.validate.validators;

import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.commons.util.mime.MediaType;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class FileUploadMediaTypeValidator implements IValidator<List<FileUpload>> {

	private static final long serialVersionUID = 1507793945782623835L;

	private final List<MediaType> mediaTypes;

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
				error.addKey(this);
				error.setVariable("extensions", Joiner.on(", ").skipNulls().join(Lists.transform(mediaTypes, new Function<MediaType, String>() {
					@Override
					public String apply(MediaType input) {
						return input.extension();
					}
				})));
				error.setVariable("clientFileName", fileUpload.getClientFileName());
				validatable.error(error);
			}
		}
	}

}
