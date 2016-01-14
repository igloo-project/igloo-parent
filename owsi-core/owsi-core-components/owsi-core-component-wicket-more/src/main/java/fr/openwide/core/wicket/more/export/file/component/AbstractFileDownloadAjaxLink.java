package fr.openwide.core.wicket.more.export.file.component;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.commons.util.mime.MediaType;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.common.WorkInProgressPopup;
import fr.openwide.core.wicket.more.export.file.behavior.FileDeferredDownloadBehavior;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;

public abstract class AbstractFileDownloadAjaxLink extends AjaxLink<Void> {
	
	private static final long serialVersionUID = -9035539414848139111L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileDownloadAjaxLink.class);
	
	private final WorkInProgressPopup loadingPopup;
	
	private final FileDeferredDownloadBehavior ajaxDownload;
	
	private final IModel<File> tempFileModel = new Model<File>();
	
	private final IModel<MediaType> mediaTypeModel = new Model<MediaType>();
	
	public AbstractFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, String fileNamePrefix) {
		super(id);
		this.loadingPopup = loadingPopup;
		this.ajaxDownload = new FileDeferredDownloadBehavior(tempFileModel, mediaTypeModel, fileNamePrefix);
		
		add(ajaxDownload);
	}
	
	protected MediaType getMediaType() {
		return MediaType.APPLICATION_ZIP;
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		loadingPopup.updateAjaxAttributes(attributes);
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		boolean hasError = false;
		Pair<File, MediaType> pairTemp;
		File tmp = null;
		
		try {
			pairTemp = generateFile();
			
			if (pairTemp == null) {
				onEmptyExport();
				hasError = true;
			} else {
				tmp = pairTemp.getValue0();
				
				if (tmp == null) {
					onEmptyExport();
					hasError = true;
				} else {
					mediaTypeModel.setObject(pairTemp.getValue1());
					tempFileModel.setObject(tmp);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Erreur en générant un Fichier.", e);
			
			hasError = true;
			FileUtils.deleteQuietly(tmp);
			tempFileModel.setObject(null);
			
			error(getString("common.error.unexpected"));
		}
		
		FeedbackUtils.refreshFeedback(target, getPage());
		target.appendJavaScript(loadingPopup.closeStatement().render());
		
		if (!hasError) {
			ajaxDownload.initiate(target);
		}
	}
	
	protected void onEmptyExport() {
		error(getString("common.error.export.empty"));
	}
	
	protected abstract Pair<File, MediaType> generateFile() throws ServiceException;
	
	@Override
	protected void onDetach() {
		super.onDetach();
		tempFileModel.detach();
		mediaTypeModel.detach();
	}
}
