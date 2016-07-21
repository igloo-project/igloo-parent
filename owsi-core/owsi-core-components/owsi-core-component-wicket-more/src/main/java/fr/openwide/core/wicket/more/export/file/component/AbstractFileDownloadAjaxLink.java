package fr.openwide.core.wicket.more.export.file.component;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.commons.util.mime.MediaType;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.common.WorkInProgressPopup;
import fr.openwide.core.wicket.more.export.file.behavior.FileDeferredDownloadBehavior;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.util.model.Detachables;

public abstract class AbstractFileDownloadAjaxLink extends AjaxLink<Void> {

	private static final long serialVersionUID = -7776428837770440939L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileDownloadAjaxLink.class);

	private final WorkInProgressPopup loadingPopup;

	private final FileDeferredDownloadBehavior ajaxDownload;

	private final IModel<File> tempFileModel = new Model<File>();

	public AbstractFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<String> fileNamePrefixModel, IModel<MediaType> mediaTypeModel) {
		super(id);
		this.loadingPopup = loadingPopup;
		this.ajaxDownload = FileDeferredDownloadBehavior.withMediaType(tempFileModel, fileNamePrefixModel, mediaTypeModel);
		
		add(ajaxDownload);
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		loadingPopup.updateAjaxAttributes(attributes);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		File tmp = null;
		try {
			tmp = generateFile();
			
			if (tmp == null) {
				throw new IllegalStateException("File is missing.");
			}
			
			tempFileModel.setObject(tmp);
			
			ajaxDownload.initiate(target);
			
			target.appendJavaScript(loadingPopup.closeStatement().render());
		} catch (Exception e) {
			LOGGER.error("Error while generating a file.", e);
			
			FileUtils.deleteQuietly(tmp);
			tempFileModel.setObject(null);
			
			error(getString("common.error.unexpected"));
		}
		FeedbackUtils.refreshFeedback(target, getPage());
	}

	protected abstract File generateFile() throws ServiceException;

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(tempFileModel);
	}

}
