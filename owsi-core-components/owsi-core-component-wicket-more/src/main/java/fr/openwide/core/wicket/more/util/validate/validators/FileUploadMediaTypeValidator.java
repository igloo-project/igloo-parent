package fr.openwide.core.wicket.more.util.validate.validators;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.Args;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.mime.MediaType;

public class FileUploadMediaTypeValidator implements IValidator<List<FileUpload>> {

	private static final long serialVersionUID = 1507793945782623835L;

	private List<MediaType> mediaTypes;
	
	public FileUploadMediaTypeValidator(List<MediaType> mediaTypes) {
		Args.notNull(mediaTypes, "mediaTypes");
		this.mediaTypes = Lists.newArrayList(mediaTypes);
	}
	
	@Override
	public void validate(IValidatable<List<FileUpload>> validatable) {
		for (FileUpload fileUpload : validatable.getValue()) {
			String fileUploadExtension = FilenameUtils.getExtension(fileUpload.getClientFileName());
			MediaType fileUploadMediaType = MediaType.fromExtension(fileUploadExtension);
			
			if (fileUploadMediaType == null || !mediaTypes.contains(fileUploadMediaType)) {
				ValidationError error = new ValidationError(this);
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
