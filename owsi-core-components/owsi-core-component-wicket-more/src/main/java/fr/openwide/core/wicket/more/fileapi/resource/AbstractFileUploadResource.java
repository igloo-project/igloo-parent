package fr.openwide.core.wicket.more.fileapi.resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.wicket.more.fileapi.behavior.FileUploadBehavior;
import fr.openwide.core.wicket.more.fileapi.model.FileApiFile;

/**
 * From https://github.com/martin-g/blogs/tree/master/file-upload
 * 
 * The resource that handles the file uploads. Reads the file items from the
 * request parameters and uses FileManager to store them. Additionally cares
 * about the response's content type and body.
 */
public abstract class AbstractFileUploadResource extends AbstractResource {
	private static final long serialVersionUID = 7699763145818556913L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileUploadResource.class);

	public AbstractFileUploadResource() {
		super();
		Injector.get().inject(this);
	}

	/**
	 * Defines what is the maximum size of the uploaded files.
	 * 
	 * @return
	 */
	protected abstract Bytes getMaxSize();

	/**
	 * Delegates to store the uploaded files
	 * 
	 * @param fileItems
	 * @throws IOException
	 */
	protected abstract void saveFiles(Attributes attributes, List<FileApiFile> identifiers, List<FileItem> fileItems) throws IOException;

	/**
	 * Reads and stores the uploaded files
	 * 
	 * @param attributes
	 * @return
	 */
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		final ResourceResponse resourceResponse = new ResourceResponse();
		
		final ServletWebRequest webRequest = (ServletWebRequest) attributes.getRequest();
		
		try {
			MultipartServletWebRequest multiPartRequest = webRequest.newMultipartWebRequest(getMaxSize(), "ignored");
			
			Map<String, List<FileItem>> files = multiPartRequest.getFiles();
			List<FileItem> fileItems = files.get(FileUploadBehavior.PARAMETERS_FILE_UPLOAD);
			List<FileApiFile> fileApiFiles = FileUploadBehavior.readfileApiFiles(multiPartRequest.getRequestParameters());
			if (fileApiFiles.size() != fileItems.size()) {
				throw new IllegalStateException("Files and uploaded files list size inconsistent");
			}
			saveFiles(attributes, fileApiFiles, fileItems);
			
			prepareResponse(resourceResponse, webRequest, fileItems);
		} catch (Exception fux) {
			LOGGER.error("An error occurred while uploading a file", fux);
			throw new AbortWithHttpErrorCodeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fux.getMessage());
		}

		return resourceResponse;
	}

	/**
	 * Sets the response's content type and body
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param fileItems
	 * @throws FileUploadException
	 * @throws IOException
	 */
	protected void prepareResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest,
			List<FileItem> fileItems) throws FileUploadException, IOException {

		final String responseContent;
		String accept = webRequest.getHeader("Accept");
		if (wantsHtml(accept)) {
			// Internet Explorer
			resourceResponse.setContentType("text/html");

			responseContent = generateHtmlResponse(resourceResponse, webRequest, fileItems);
		} else {
			// a real browser
			resourceResponse.setContentType("application/json");

			responseContent = generateJsonResponse(resourceResponse, webRequest, fileItems);
		}

		resourceResponse.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(Attributes attributes) throws IOException {
				attributes.getResponse().write(responseContent);
			}
		});
	}

	/**
	 * Decides what should be the response's content type depending on the
	 * 'Accept' request header. HTML5 browsers work with "application/json",
	 * older ones use IFrame to make the upload and the response should be HTML.
	 * Read http://blueimp.github.com/jQuery-File-Upload/ docs for more info.
	 * 
	 * @param acceptHeader
	 * @return
	 */
	protected boolean wantsHtml(String acceptHeader) {
		return !Strings.isEmpty(acceptHeader) && acceptHeader.contains("text/html");
	}

	/**
	 * Should generate the response's body in JSON format
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param files
	 * @return
	 */
	protected abstract String generateJsonResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest,
			List<FileItem> files);

	/**
	 * Should generate the response's body in HTML format
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param files
	 * @return
	 */
	protected abstract String generateHtmlResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest,
			List<FileItem> files);

}