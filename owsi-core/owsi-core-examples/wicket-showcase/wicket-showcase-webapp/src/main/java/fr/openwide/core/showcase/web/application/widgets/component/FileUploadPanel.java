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
			protected void onFileChange(AjaxRequestTarget target, List<FileApiFile> fileList) {
				for (FileApiFile fileApiFile : fileList) {
					ShowcaseFile file = new ShowcaseFile();
					file.setExtension(FilenameUtils.getExtension(fileApiFile.getName()));
					file.setName(fileApiFile.getName());
					try {
						showcaseFileService.create(file);
						fileApiFile.setIdentifier(file.getId().toString());
					} catch (Exception e) {
						LOGGER.error("Erreur d'enregistrement du fichier");
					}
				}
				fileListModel.getObject().addAll(fileList);
				target.add(fileListContainer);
			}

			@Override
			protected String getFileUploadUrl() {
				return FileUploadResource.linkDescriptor().fullUrl();
			}
		});
		
		form.add(fileUpload);
		fileListContainer.add(fileList);
		add(progress);
		add(form);
		add(fileListContainer);
	}

}
