package org.iglooproject.wicket.more.export;

import java.io.File;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.wicket.modal.AbstractFileDownloadAjaxLink;
import org.iglooproject.wicket.modal.WorkInProgressPopup;
import org.iglooproject.wicket.more.export.util.ExportFileUtils;
import org.javatuples.LabelValue;

public abstract class SimpleFileDownloadAjaxLink extends AbstractFileDownloadAjaxLink {

	private static final long serialVersionUID = -1855491000516928812L;

	private final IModel<String> fileNameModel;

	public SimpleFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, String fileNamePrefix, MediaType mediaType) {
		this(id, loadingPopup, Model.of(fileNamePrefix), Model.of(mediaType));
	}

	public SimpleFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<String> fileNamePrefixModel, IModel<MediaType> mediaTypeModel) {
		this(id, loadingPopup, ExportFileUtils.getFileNameMediaTypeModel(fileNamePrefixModel, mediaTypeModel));
	}

	public SimpleFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<String> fileNameModel) {
		super(id, loadingPopup);
		this.fileNameModel = fileNameModel;
	}

	@Override
	protected LabelValue<String, File> generateFileInformation() {
		return LabelValue.with(
				fileNameModel.getObject(),
				generateFile()
		);
	}

	protected abstract File generateFile();

}
