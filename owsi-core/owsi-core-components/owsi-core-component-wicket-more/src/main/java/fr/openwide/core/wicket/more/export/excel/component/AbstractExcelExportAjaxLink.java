package fr.openwide.core.wicket.more.export.excel.component;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.commons.util.mime.MediaType;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.export.excel.behavior.ExcelExportDeferredDownloadBehavior;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;

public abstract class AbstractExcelExportAjaxLink extends AjaxLink<Void> {
	
	private static final long serialVersionUID = 4532792944573585105L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExcelExportAjaxLink.class);
	
	@SpringBean
	private CoreConfigurer configurer;
	
	private final ExcelExportWorkInProgressModalPopupPanel loadingPopup;
	
	private final ExcelExportDeferredDownloadBehavior ajaxDownload;
	
	private final IModel<File> tempFileModel = new Model<File>();
	
	private final IModel<MediaType> mediaTypeModel = new Model<MediaType>();
	
	public AbstractExcelExportAjaxLink(String id, ExcelExportWorkInProgressModalPopupPanel loadingPopup, String fileNamePrefix) {
		super(id);
		this.loadingPopup = loadingPopup;
		this.ajaxDownload = new ExcelExportDeferredDownloadBehavior(tempFileModel, mediaTypeModel, fileNamePrefix);
		
		add(ajaxDownload);
	}
	
	protected MediaType getMediaType(Workbook workbook) {
		return workbook instanceof XSSFWorkbook ? MediaType.APPLICATION_OPENXML_EXCEL : MediaType.APPLICATION_MS_EXCEL;
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
				
				tmp = File.createTempFile("export-", "", configurer.getTmpExportExcelDirectory());
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
		tempFileModel.detach();
		mediaTypeModel.detach();
	}
}
