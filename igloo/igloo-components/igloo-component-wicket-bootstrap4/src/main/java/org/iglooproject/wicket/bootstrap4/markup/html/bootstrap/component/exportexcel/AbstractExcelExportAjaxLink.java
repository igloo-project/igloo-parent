package org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component.exportexcel;

import static org.iglooproject.spring.property.SpringPropertyIds.TMP_EXPORT_EXCEL_PATH;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component.workinprogress.WorkInProgressPopup;
import org.iglooproject.wicket.more.export.file.behavior.FileDeferredDownloadBehavior;
import org.iglooproject.wicket.more.export.util.ExportFileUtils;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.util.model.Detachables;
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
	
	/**
	 * @return A media type for a Microsoft Excel file (xls or xlsx). Media type for ODF Spreadsheet (ods) is not valid.
	 */
	protected MediaType getMediaType(Workbook workbook) {
		if (workbook instanceof HSSFWorkbook) {
			return MediaType.APPLICATION_MS_EXCEL;
		} else if (workbook instanceof XSSFWorkbook && ((XSSFWorkbook) workbook).isMacroEnabled()) {
			return MediaType.APPLICATION_MS_EXCEL_MACRO;
		} else if (workbook instanceof XSSFWorkbook && !((XSSFWorkbook) workbook).isMacroEnabled()) {
			return MediaType.APPLICATION_OPENXML_EXCEL;
		} else if (workbook instanceof SXSSFWorkbook) {
			return MediaType.APPLICATION_OPENXML_EXCEL;
		} else {
			// Default
			return MediaType.APPLICATION_MS_EXCEL;
		}
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
				mediaTypeModel.setObject(getMediaType(workbook));
				
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
