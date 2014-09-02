package fr.openwide.core.showcase.web.application.widgets.resource;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;
import fr.openwide.core.showcase.core.business.fileupload.service.IShowcaseFileService;
import fr.openwide.core.showcase.core.util.spring.ShowcaseConfigurer;
import fr.openwide.core.wicket.more.fileapi.model.FileApiFile;
import fr.openwide.core.wicket.more.fileapi.resource.AbstractFileUploadResource;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

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
		return new LinkDescriptorBuilder()
				.resource(REFERENCE)
				.build();
	}

	@SpringBean
	private IShowcaseFileService showcaseFileService;

	@SpringBean
	private ShowcaseConfigurer configurer;

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
					} catch (Exception e) {
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
