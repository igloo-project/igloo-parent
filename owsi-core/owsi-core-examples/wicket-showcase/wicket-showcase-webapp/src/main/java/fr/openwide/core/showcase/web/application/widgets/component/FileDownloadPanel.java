package fr.openwide.core.showcase.web.application.widgets.component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schlichtherle.truezip.file.TFile;
import fr.openwide.core.commons.util.mime.MediaType;
import fr.openwide.core.commons.util.registry.TFileRegistry;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.fileupload.service.IShowcaseFileService;
import fr.openwide.core.showcase.core.util.spring.ShowcaseConfigurer;
import fr.openwide.core.wicket.more.common.WorkInProgressPopup;
import fr.openwide.core.wicket.more.export.file.component.AbstractFileDownloadAjaxLink;

public class FileDownloadPanel extends Panel {

	private static final long serialVersionUID = 4147662803478838954L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadPanel.class);

	@SpringBean
	private IShowcaseFileService showcaseFileService;
	
	@SpringBean
	private ShowcaseConfigurer configurer;

	private Map<TFile, Map<String, Long>> doublons = new HashMap<>();
	
	public FileDownloadPanel(String id) {
		super(id);
		
		WorkInProgressPopup loadingPopup = new WorkInProgressPopup("exportZipLoadingPopup", new ResourceModel("common.action.export.zip.loading"));
		
		add(
				loadingPopup,
				new AbstractFileDownloadAjaxLink("exportZip", loadingPopup, "export-showcase-") {
					private static final long serialVersionUID = 1L;
					@Override
					protected Pair<File, MediaType> generateFile() throws ServiceException {
						File file = generateTFile();
						MediaType type = MediaType.APPLICATION_ZIP;
						
						return Pair.with(file, type); 
					}
				}
		);
	}

	/*
	 * If you want to use TFileRegistry you must add {@link TFileZipFileFilter} in your web.xml file
	 */
	public TFile generateTFile() {
		TFile archiveFile = null;
		try {
			archiveFile = new TFile(configurer.getTmpDirectory() + "/mon_archive" + "." + RandomUtils.nextLong() + "." + MediaType.APPLICATION_ZIP.extension()).mkdir(true);
			
			// ------ dossier1 ------ //
			TFile dossier1 = TFileRegistry.create(archiveFile, "Dossier1").mkdir(true);
			
				// ---- dossier11 ----
			TFile dossier11 = TFileRegistry.create(dossier1, "Dossier11").mkdir(true);
			
			File file = File.createTempFile("file", ".txt");
			copyFileIfNotNull(file, dossier11);
			
				// ---- dossier12 ----
			TFile dossier12 = TFileRegistry.create(dossier1, "Dossier12").mkdir(true);
			
			// ---- dossier2 ----
			TFile dossier2 = TFileRegistry.create(archiveFile, "Dossier2").mkdir(true);
			
				// ---- dossier21 ----
			TFile dossier21 = TFileRegistry.create(dossier2, "Dossier21").mkdir(true);
			
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la construction d'un Zip.", e);
		}
		
		return archiveFile.toNonArchiveFile();
	}
	
	private void copyFileIfNotNull(File file, TFile destination) throws IOException {
		if (file != null) {
			TFile src = TFileRegistry.create(file);
			String fileName = file.getName();
			
			TFile doublon = TFileRegistry.create(destination, fileName);
			if (doublon.exists()) {
				if (!doublons.containsKey(destination)) {
					doublons.put(destination, new HashMap<String, Long>());
				}
				
				if (doublons.get(destination).containsKey(fileName)) {
					doublons.get(destination).put(fileName, doublons.get(destination).get(fileName)+1L);
				} else {
					doublons.get(destination).put(fileName, 1L);
				}
				
				fileName = fileName + "(" + doublons.get(destination).get(fileName) + ")";
			}
			
			TFile dst = TFileRegistry.create(destination);
			if (dst.isArchive() || dst.isDirectory()) {
				dst = TFileRegistry.create(dst, fileName);
			}
			
			src.cp_rp(dst);
		}
	}
}
