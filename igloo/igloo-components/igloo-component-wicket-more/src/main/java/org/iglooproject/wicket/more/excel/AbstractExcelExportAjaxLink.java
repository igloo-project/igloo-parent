package org.iglooproject.wicket.more.excel;

import static org.iglooproject.spring.property.SpringPropertyIds.TMP_EXPORT_EXCEL_PATH;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.api.download.FileDeferredDownloadBehavior;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.modal.WorkInProgressPopup;
import org.iglooproject.wicket.more.export.spreadsheet.SpreadsheetUtils;
import org.iglooproject.wicket.more.export.util.ExportFileUtils;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExcelExportAjaxLink extends AjaxLink<Void> {
	
	private static final long serialVersionUID = 4532792944573585105L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExcelExportAjaxLink.class);
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final WorkInProgressPopup loadingPopup;
	
	private final FileDeferredDownloadBehavior ajaxDownload;
	
	private final IModel<File> tempFileModel = new Model<>();
	
	private final IModel<MediaType> mediaTypeModel = new Model<>();
	
	public AbstractExcelExportAjaxLink(String id, WorkInProgressPopup loadingPopup, String fileNamePrefix) {
		this(id, loadingPopup, Model.of(fileNamePrefix));
	}
	
	public AbstractExcelExportAjaxLink(String id, WorkInProgressPopup loadingPopup, IModel<String> fileNamePrefixModel) {
		super(id);
		this.loadingPopup = loadingPopup;
		this.ajaxDownload = new FileDeferredDownloadBehavior(tempFileModel, ExportFileUtils.getFileNameMediaTypeModel(fileNamePrefixModel, mediaTypeModel));
		
		add(ajaxDownload);
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		loadingPopup.updateAjaxAttributes(attributes);
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		boolean hasError = false;
		File tmp = null;
		try {
			Workbook workbook = generateWorkbook();
			if (workbook.getNumberOfSheets() == 0) {
				onEmptyExport(workbook);
				hasError = true;
			} else {
				mediaTypeModel.setObject(SpreadsheetUtils.getMediaType(workbook));
				
				tmp = File.createTempFile("export-", "", propertyService.get(TMP_EXPORT_EXCEL_PATH));
				tempFileModel.setObject(tmp);
				FileOutputStream output = new FileOutputStream(tmp);
				workbook.write(output);
				output.close();
			}
		} catch (Exception e) {
			LOGGER.error("Erreur en générant un fichier Excel.", e);
			
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
	
	protected void onEmptyExport(Workbook workbook) {
		error(getString("common.error.export.empty"));
	}
	
	protected abstract Workbook generateWorkbook() throws ServiceException;
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			tempFileModel,
			mediaTypeModel
		);
	}
}
