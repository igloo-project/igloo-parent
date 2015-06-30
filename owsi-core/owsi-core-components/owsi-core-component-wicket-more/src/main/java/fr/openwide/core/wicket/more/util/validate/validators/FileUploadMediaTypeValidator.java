package fr.openwide.core.wicket.more.util.validate.validators;

import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.Args;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.mime.MediaType;

public class FileUploadMediaTypeValidator implements IValidator<List<FileUpload>> {

	private static final long serialVersionUID = 1507793945782623835L;

	private final List<MediaType> mediaTypes;

	private String errorResourceKey = null;

	public FileUploadMediaTypeValidator(Collection<MediaType> mediaTypes) {
		this.mediaTypes = ImmutableList.copyOf(Args.notNull(mediaTypes, "mediaTypes"));
	}
	
	@Deprecated
	public FileUploadMediaTypeValidator(String errorResourceKey, Collection<MediaType> mediaTypes) {
		this(mediaTypes);
		this.errorResourceKey = errorResourceKey;
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

	public FileUploadMediaTypeValidator setErrorResourceKey(String errorResourceKey) {
		this.errorResourceKey = errorResourceKey;
		return this;
	}

}
