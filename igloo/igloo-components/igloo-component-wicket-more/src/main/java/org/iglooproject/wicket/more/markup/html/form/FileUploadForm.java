package org.iglooproject.wicket.more.markup.html.form;

import java.util.Map;

import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.core.FileUploadSizeException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

/**
 * A form with a decent resource key for the file upload error message:
 * <ul>
 * <li><pre>FileUploadForm.uploadFailed</pre> when the upload failed
 * <li><pre>FileUploadForm.uploadTooLarge</pre> when the uploaded file is too large (model: "maxSize" and "actualSize")
 * <ul>
 */
public class FileUploadForm<T> extends Form<T> {
	
	private static final long serialVersionUID = 4537543017863239331L;
	
	private static final String UPLOAD_TOO_LARGE_RESOURCE_KEY = "uploadTooLarge";

	private static final String UPLOAD_FAILED_RESOURCE_KEY = "uploadFailed";
	
	public FileUploadForm(String id) {
		super(id);
	}
	
	public FileUploadForm(String id, IModel<T> model) {
		super(id, model);
	}
	
	@Override
	protected void onFileUploadException(final FileUploadException e, final Map<String, Object> model) {
		onFileUploadException(this, e, model);
	}
	
	public static void onFileUploadException(Form<?> form, FileUploadException e, Map<String, Object> model) {
		if (e instanceof FileUploadSizeException) {
			final String defaultValue = "Upload must be less than " + form.getMaxSize();
			model.put("actualSize", Bytes.bytes(((FileUploadSizeException)e).getActualSize()));
			String msg = form.getString(FileUploadForm.class.getSimpleName() + '.' + UPLOAD_TOO_LARGE_RESOURCE_KEY, Model.ofMap(model), defaultValue);
			form.error(msg);
		} else {
			final String defaultValue = "Upload failed: " + e.getLocalizedMessage();
			String msg = form.getString(FileUploadForm.class.getSimpleName() + '.' + UPLOAD_FAILED_RESOURCE_KEY, Model.ofMap(model), defaultValue);
			form.error(msg);
		}
	}
}
