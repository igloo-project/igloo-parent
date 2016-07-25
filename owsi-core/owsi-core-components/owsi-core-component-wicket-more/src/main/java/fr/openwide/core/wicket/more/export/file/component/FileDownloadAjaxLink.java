package fr.openwide.core.wicket.more.export.file.component;

import java.io.File;

import org.apache.wicket.model.IModel;
import org.javatuples.LabelValue;

import fr.openwide.core.wicket.more.common.WorkInProgressPopup;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class FileDownloadAjaxLink extends AbstractFileDownloadAjaxLink {

	private static final long serialVersionUID = -7776428837770440939L;

	private final IModel<LabelValue<String, File>> fileInformationModel;

	public FileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<LabelValue<String, File>> fileInformationModel) {
		super(id, loadingPopup);
		this.fileInformationModel = fileInformationModel;
	}

	@Override
	protected LabelValue<String, File> generateFileInformation() {
		return fileInformationModel.getObject();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(fileInformationModel);
	}

}
