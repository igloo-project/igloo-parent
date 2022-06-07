package org.iglooproject.wicket.modal;

import java.io.File;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.util.Detachables;
import org.javatuples.LabelValue;

public class FileDownloadAjaxLink extends AbstractFileDownloadAjaxLink {

	private static final long serialVersionUID = -7776428837770440939L;

	private final IModel<LabelValue<String, File>> fileInformationModel;

	public FileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<LabelValue<String, File>> fileInformationModel) {
		super(id, loadingPopup);
		this.fileInformationModel = fileInformationModel;
	}

	/**
	 * This method is final. If you need to override it, extend AbstractFileDownloadLink or use
	 * SimpleFileDownloadAjaxLink instead.
	 */
	@Override
	protected final LabelValue<String, File> generateFileInformation() {
		return fileInformationModel.getObject();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(fileInformationModel);
	}

}
