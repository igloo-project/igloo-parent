package org.iglooproject.showcase.web.application.widgets.resource;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.showcase.core.business.fileupload.model.ShowcaseFile;
import org.iglooproject.showcase.core.business.fileupload.service.IShowcaseFileService;
import org.iglooproject.wicket.more.fileapi.model.FileApiFile;
import org.iglooproject.wicket.more.fileapi.resource.AbstractFileUploadResource;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class FileUploadResource extends AbstractFileUploadResource {

	private static final long serialVersionUID = -5800661087560130202L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadResource.class);
	
	public static final ResourceReference REFERENCE = new ResourceReference(FileUploadResource.class, 
			"fileUploadResourceReference") {
				private static final long serialVersionUID = 1L;
				@Override
				public IResource getResource() {
					return new FileUploadResource();
				}
			};
			
	public static IResourceLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.resource(REFERENCE);
	}

	@SpringBean
	private IShowcaseFileService showcaseFileService;

	public FileUploadResource() {
		super();
	}

	@Override
	protected void saveFiles(Attributes attributes, List<FileApiFile> fileApiFiles, List<FileItem> fileItems,
			List<FileApiFile> successFiles, List<FileApiFile> errorFiles) throws IOException {
		for (FileItem fileItem : fileItems) {
			int i = fileItems.indexOf(fileItem);
			FileApiFile fileApiFile = fileApiFiles.get(i);
			List<ShowcaseFile> files = showcaseFileService.list();
			boolean found = false;
			for (ShowcaseFile showcaseFile : files) {
				// on cherche l'entité correspondant au fichier persisté
				if (showcaseFile.getId().toString().equals(fileApiFile.getIdentifier())) {
					try {
						showcaseFileService.addFile(showcaseFile, fileItem.getInputStream());
						successFiles.add(fileApiFile);
						found = true;
					} catch (RuntimeException | IOException | ServiceException | SecurityServiceException e) {
						LOGGER.error("Error uploading file.", e);
						fileApiFile.setErrorMessage(e.getMessage());
						errorFiles.add(fileApiFile);
					}
				}
			}
			if (found == false) {
				LOGGER.error("File not found");
			}
		}
	}

	@Override
	protected Bytes getMaxSize() {
		return Bytes.megabytes(750);
	}

	@Override
	protected String getFileSizeErrorMessage() {
		return "widgets.fileupload.globalError";
	}

}
