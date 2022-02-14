package org.iglooproject.wicket.more.request.resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Application;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileStoreWebResource extends AbstractResource {

	private static final long serialVersionUID = 6019366659520380200L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileStoreWebResource.class);
	
	private ContentDisposition contentDisposition = ContentDisposition.INLINE;
	
	private String textEncoding;
	
	private Duration cacheDuration;
	
	private WebResponse.CacheScope cacheScope;

	public AbstractFileStoreWebResource() {
		super();
		Injector.get().inject(this);
	}
	
	protected abstract FileStoreResourceStream getFileStoreResourceStream(PageParameters parameters)
			throws ServiceException, SecurityServiceException;
	
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse data = new ResourceResponse();
		
		try {
			final FileStoreResourceStream stream = getFileStoreResourceStream(attributes.getParameters());
		
			Instant lastModifiedTime = stream.lastModifiedTime();
			if (lastModifiedTime != null) {
				data.setLastModified(lastModifiedTime);
			}
	
			// performance check; don't bother to do anything if the resource is
			// still cached by client
			if (data.dataNeedsToBeWritten(attributes)) {
				InputStream inputStream = null;
				try {
					inputStream = stream.getInputStream();
				} catch (ResourceStreamNotFoundException e) {
					data.setError(HttpServletResponse.SC_NOT_FOUND);
					close(stream);
				}
	
				data.setContentDisposition(contentDisposition);
				Bytes length = stream.length();
				if (length != null) {
					data.setContentLength(length.bytes());
				}
				
				String fileName = stream.getFileName();
				
				data.setFileName(fileName);
				
				String contentType = stream.getContentType();
				if (contentType == null && fileName != null && Application.exists()) {
					contentType = Application.get().getMimeType(fileName);
				}
				data.setContentType(contentType);
				data.setTextEncoding(textEncoding);
	
				if (cacheDuration != null) {
					data.setCacheDuration(cacheDuration);
				}
				if (cacheScope != null) {
					data.setCacheScope(cacheScope);
				}
	
				final InputStream s = inputStream;
				data.setWriteCallback(new WriteCallback() {
					@Override
					public void writeData(Attributes attributes) throws IOException {
						try {
							writeStream(attributes, s);
						} finally {
							close(stream);
						}
					}
				});
			}
		} catch (RuntimeException | ServiceException | SecurityServiceException e) {
			LOGGER.error("Unable to open the FileStoreResourceStream", e);
		}

		return data;
	}
	
	private void close(AbstractResourceStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			LOGGER.error("Couldn't close ResourceStream", e);
		}
	}
	
	public void setContentDisposition(ContentDisposition contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public void setTextEncoding(String textEncoding) {
		this.textEncoding = textEncoding;
	}

	public void setCacheDuration(Duration cacheDuration) {
		this.cacheDuration = cacheDuration;
	}

	public WebResponse.CacheScope getCacheScope() {
		return cacheScope;
	}

	public void setCacheScope(WebResponse.CacheScope cacheScope) {
		this.cacheScope = cacheScope;
	}
}
