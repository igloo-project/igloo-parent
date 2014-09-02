package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;
import fr.openwide.core.showcase.core.business.fileupload.service.IShowcaseFileService;
import fr.openwide.core.showcase.web.application.widgets.resource.FileUploadResource;
import fr.openwide.core.wicket.more.fileapi.behavior.FileUploadBehavior;
import fr.openwide.core.wicket.more.fileapi.model.FileApiFile;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;

public class FileUploadPanel extends Panel {

	private static final long serialVersionUID = 4147662803478838954L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadPanel.class);

	@SpringBean
	private IShowcaseFileService showcaseFileService;

	private IModel<List<FileApiFile>> fileListModel = new ListModel<FileApiFile>(Lists.<FileApiFile>newArrayList());

	public FileUploadPanel(String id) {
		super(id);
		
		final WebMarkupContainer fileListContainer = new WebMarkupContainer("fileListContainer");
		fileListContainer.setOutputMarkupId(true);
		ListView<FileApiFile> fileList = new ListView<FileApiFile>("fileList", fileListModel) {
			private static final long serialVersionUID = -4186637455523902479L;
			
			@Override
			protected void populateItem(ListItem<FileApiFile> item) {
				item.add(new Label("name", item.getModelObject().getName()));
				item.add(new Label("type", item.getModelObject().getType()));
				item.add(new Label("size", item.getModelObject().getSize()));
				item.add(new ExternalLink("link", item.getModelObject().getObjectUrl()));
			}
		};
		
		WebMarkupContainer progress = new WebMarkupContainer("progress");
		progress.setOutputMarkupId(true);
		
		Form<?> form = new Form<Void>("form");
		FileUploadField fileUpload = new FileUploadField("fileUpload");
		fileUpload.add(new FileUploadBehavior(progress) {
			private static final long serialVersionUID = 8090452933671159353L;
			
			@Override
			protected List<FileApiFile> onFileChange(AjaxRequestTarget target, List<FileApiFile> fileList) {
				List<FileApiFile> acceptedFiles = Lists.newArrayList();
				for (FileApiFile fileApiFile : fileList) {
					if (fileList.indexOf(fileApiFile) == 0) {
						continue;
					}
					ShowcaseFile file = new ShowcaseFile();
					file.setExtension(FilenameUtils.getExtension(fileApiFile.getName()));
					file.setName(fileApiFile.getName());
					try {
						showcaseFileService.create(file);
						fileApiFile.setIdentifier(file.getId().toString());
						acceptedFiles.add(fileApiFile);
					} catch (Exception e) {
						LOGGER.error("Erreur d'enregistrement du fichier");
					}
				}
				fileListModel.getObject().addAll(acceptedFiles);
				target.add(fileListContainer);
				return acceptedFiles;
			}

			@Override
			protected String getFileUploadUrl() {
				return FileUploadResource.linkDescriptor().fullUrl();
			}

			@Override
			protected void onFileUploadDone(AjaxRequestTarget target, List<FileApiFile> successFileList, List<FileApiFile> errorFileList) {
				if (!errorFileList.isEmpty()) {
					error(getString("widgets.fileupload.partialError"));
				}
				List<FileApiFile> toRemove = Lists.newArrayList();
				for (FileApiFile file : errorFileList) {
					ShowcaseFile showcaseFile = showcaseFileService.getById(Long.valueOf(file.getIdentifier()));
					for (FileApiFile fileApiFile : fileListModel.getObject()) {
						if (fileApiFile.getIdentifier() != null && fileApiFile.getIdentifier().equals(fileApiFile.getIdentifier())) {
							toRemove.add(fileApiFile);
						}
					}
					try {
						showcaseFileService.delete(showcaseFile);
					} catch (Exception e) {
						LOGGER.error("Erreur de nettoyage des fichiers", e);
					}
				}
				fileListModel.getObject().removeAll(toRemove);
				FeedbackUtils.refreshFeedback(target, getPage());
				target.add(fileListContainer);
			}

			@Override
			protected void onFileUploadFails(AjaxRequestTarget target, String errorMessage) {
				error(getString(errorMessage));
				for (FileApiFile file : fileListModel.getObject()) {
					ShowcaseFile showcaseFile = showcaseFileService.getById(Long.valueOf(file.getIdentifier()));
					try {
						showcaseFileService.delete(showcaseFile);
					} catch (Exception e) {
						LOGGER.error("Erreur de nettoyage des fichiers", e);
					}
				}
				fileListModel.getObject().removeAll(fileListModel.getObject());
				FeedbackUtils.refreshFeedback(target, getPage());
				target.add(fileListContainer);
			}
		});
		
		form.add(fileUpload);
		fileListContainer.add(fileList);
		add(progress);
		add(form);
		add(fileListContainer);
	}

}
