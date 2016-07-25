package fr.openwide.core.wicket.more.export.file.component;

import java.io.File;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.javatuples.LabelValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.wicket.more.common.WorkInProgressPopup;
import fr.openwide.core.wicket.more.export.file.behavior.FileDeferredDownloadBehavior;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.util.model.Detachables;

public abstract class AbstractFileDownloadAjaxLink extends AjaxLink<Void> {

	private static final long serialVersionUID = 3043111209219249583L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileDownloadAjaxLink.class);

	private final WorkInProgressPopup loadingPopup;

	private final IModel<LabelValue<String, File>> tempFileInformationModel;

	private final FileDeferredDownloadBehavior ajaxDownload;

	public AbstractFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup) {
		super(id);
		this.loadingPopup = loadingPopup;
		this.tempFileInformationModel = new Model<LabelValue<String, File>>();
		
		this.ajaxDownload = new FileDeferredDownloadBehavior(tempFileInformationModel);
		
		add(ajaxDownload);
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		loadingPopup.updateAjaxAttributes(attributes);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		try {
			tempFileInformationModel.setObject(generateFileInformation());
			ajaxDownload.initiate(target);
			target.appendJavaScript(loadingPopup.closeStatement().render());
		} catch (Exception e) {
			tempFileInformationModel.setObject(null);
			LOGGER.error("Error while downloading a file.", e);
			error(getString("common.error.unexpected"));
		}
		FeedbackUtils.refreshFeedback(target, getPage());
	}

	protected abstract LabelValue<String, File> generateFileInformation();

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(tempFileInformationModel);
	}

}
