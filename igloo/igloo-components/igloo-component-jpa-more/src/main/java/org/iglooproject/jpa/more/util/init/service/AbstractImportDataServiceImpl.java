package org.iglooproject.jpa.more.util.init.service;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.DATABASE_INITIALIZED;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.util.init.dao.IImportDataDao;
import org.iglooproject.jpa.more.util.init.util.GenericEntityConverter;
import org.iglooproject.jpa.more.util.init.util.WorkbookUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.ReflectionUtils;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.iglooproject.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class AbstractImportDataServiceImpl implements IImportDataService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportDataServiceImpl.class);
	
	protected static final List<String> REFERENCE_DATA_FILE_NAMES = ImmutableList.of("reference_data.xlsx", "reference_data.xls");
	
	protected static final List<String> BUSINESS_DATA_FILE_NAMES = ImmutableList.of("business_data.xlsx", "business_data.xls");
	
	private static final String ID_FIELD_NAME = "id";
	
	private static final String CREATION_DATE_FIELD_NAME = "creationDate";
	
	private static final String LAST_UPDATE_DATE_FIELD_NAME = "lastUpdateDate";
	
	private static final String PASSWORD_FIELD_NAME = "password";
	
	private static final String PASSWORD_HASH_FIELD_NAME = "passwordHash";
	
	private static final Map<Class<?>, List<Class<?>>> ADDITIONAL_CLASS_MAPPINGS = new HashMap<>();
	
	private static final Map<Class<?>, String> SHEET_NAME_MAPPING = new HashMap<>();
	
	@Autowired
	private IImportDataDao importDataDao;
	
	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public void importDirectory(String classpathFolder) throws ServiceException, SecurityServiceException, IOException, Exception {
		Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping = new HashMap<>();
		
		importBeforeReferenceData(classpathFolder, idsMapping);
		
		try (DataSource referenceData = getFirstInputStream(classpathFolder, REFERENCE_DATA_FILE_NAMES)) {
			if (referenceData != null) {
				LOGGER.info("Importing {}", referenceData.getFilename());
				Workbook referenceDataWorkbook = getWorkbook(referenceData);
				importReferenceData(idsMapping, referenceDataWorkbook);
				LOGGER.info("Import of {} complete", referenceData.getFilename());
			}
		}
		
		importAfterReferenceData(classpathFolder, idsMapping);
		
		importBeforeBusinessData(classpathFolder, idsMapping);

		try (DataSource businessData = getFirstInputStream(classpathFolder, BUSINESS_DATA_FILE_NAMES)) {
			if (businessData != null) {
				LOGGER.info("Importing {}", businessData.getFilename());
				Workbook businessItemWorkbook = getWorkbook(businessData);
				importMainBusinessItems(idsMapping, businessItemWorkbook);
				LOGGER.info("Import of {} complete", businessData.getFilename());
			}
		}
		
		importAfterBusinessData(classpathFolder, idsMapping);
		
		importFiles(classpathFolder, idsMapping);
		
		propertyService.set(DATABASE_INITIALIZED, true);
		
		LOGGER.info("Import complete");
	}
	
	protected DataSource getFirstInputStream(String classpathFolder, List<String> fileNames) {
		Objects.requireNonNull(classpathFolder);
		Objects.requireNonNull(fileNames);
		
		String classpathFolderAbsolute = classpathFolder.endsWith("/") ? classpathFolder : classpathFolder + "/";
		for (String fileName : fileNames) {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathFolderAbsolute + fileName);
			if (is != null) {
				return new DataSource(is, fileName);
			}
		}
		return null;
	}
	
	protected Workbook getWorkbook(DataSource dataSource) throws FileNotFoundException, IOException {
		Objects.requireNonNull(dataSource);
		
		String fileExtension = FilenameUtils.getExtension(dataSource.getFilename());
		
		if (MediaType.APPLICATION_MS_EXCEL.extension().equals(fileExtension)) {
			return new HSSFWorkbook(dataSource.getInputStream());
		} else if (MediaType.APPLICATION_OPENXML_EXCEL.extension().equals(fileExtension)) {
			return new XSSFWorkbook(dataSource.getInputStream());
		}
		
		throw new IllegalStateException();
	}
	
	protected void importBeforeReferenceData(String classpathFolder, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importAfterReferenceData(String classpathFolder, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importBeforeBusinessData(String classpathFolder, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importAfterBusinessData(String classpathFolder, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected abstract List<String> getReferenceDataPackagesToScan();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void importReferenceData(Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping, Workbook workbook) {
		for (String packageToScan : getReferenceDataPackagesToScan()) {
			Set<Class<? extends GenericEntity>> classes = Sets.newHashSet();
			classes.addAll(ReflectionUtils.findAssignableClasses(packageToScan, GenericReferenceData.class));
			Map<Integer, Class<? extends GenericEntity>> orderedClasses = Maps.newTreeMap();
			
			for (Class<? extends GenericEntity> genericListItemClass : classes) {
				Sheet sheet = workbook.getSheet(getSheetName(genericListItemClass));
				
				if (sheet != null) {
					orderedClasses.put(sheet.getWorkbook().getSheetIndex(sheet), genericListItemClass);
				}
			}
			
			for (Class<? extends GenericEntity> genericListItemClass : orderedClasses.values()) {
				doImportItem(idsMapping, workbook, genericListItemClass);
			}
		}
	}
	
	protected abstract void importMainBusinessItems(Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping, Workbook workbook);
	
	protected void importFiles(String classpathFolder, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping) 
			throws ServiceException, SecurityServiceException {
		// nothing, override if necessary
	}
	
	protected <E extends GenericEntity<Long, ?>> void doImportItem(Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping,
				Workbook workbook, Class<E> clazz) {
		Sheet sheet = workbook.getSheet(getSheetName(clazz));
		if (sheet != null) {
			GenericEntityConverter converter = new GenericEntityConverter(importDataDao, workbook,
					new HashMap<Class<?>, Class<?>>(0), idsMapping);
			GenericConversionService conversionService = getConversionService(converter);
			converter.setConversionService(conversionService);
			
			Map<String, GenericEntity<Long, ?>> idsMappingForClass = idsMapping.get(clazz.getName());
			if (idsMappingForClass == null) {
				idsMappingForClass = new HashMap<>();
				idsMapping.put(clazz.getName(), idsMappingForClass);
			}
			
			for (Class<?> referencedClass : getOtherReferencedClasses(clazz)) {
				if (!idsMapping.containsKey(referencedClass.getName())) {
					idsMapping.put(referencedClass.getName(), new HashMap<String, GenericEntity<Long, ?>>());
				}
			}
			
			for (Map<String, Object> line : WorkbookUtils.getSheetContent(sheet)) {
				E item = BeanUtils.instantiateClass(clazz);
				
				String importId = StringUtils.trimWhitespace(Objects.toString(line.get(ID_FIELD_NAME), null));
				line.remove(ID_FIELD_NAME);
				
				doFilterLine(clazz, line);
				
				BeanWrapper wrapper = SpringBeanUtils.getBeanWrapper(item);
				wrapper.setConversionService(conversionService);
				wrapper.setPropertyValues(new MutablePropertyValues(line), true);
				
				importDataDao.create(item);
				
				afterImportItem(item);
				
				idsMappingForClass.put(importId, item);
				
				for (Class<?> referencedClass : getOtherReferencedClasses(clazz)) {
					idsMapping.get(referencedClass.getName()).put(importId, item);
				}
			}
			
			LOGGER.info("Imported " + idsMappingForClass.size() + " objects for class: " + clazz.getSimpleName());
		} else {
			LOGGER.info("Nothing to do for class: " + clazz.getSimpleName());
		}
	}
	
	protected <E extends GenericEntity<Long, ?>> void doFilterLine(Class<E> clazz, Map<String, Object> line) {
		Date creationDate = new Date();
		if (!line.containsKey(CREATION_DATE_FIELD_NAME)) {
			line.put(CREATION_DATE_FIELD_NAME, creationDate);
		}
		if (!line.containsKey(LAST_UPDATE_DATE_FIELD_NAME)) {
			line.put(LAST_UPDATE_DATE_FIELD_NAME, creationDate);
		}
		
		if (line.containsKey(PASSWORD_FIELD_NAME)) {
			line.put(PASSWORD_HASH_FIELD_NAME, passwordEncoder.encode(line.get(PASSWORD_FIELD_NAME).toString()));
		}
	}
	
	protected void addAdditionalClassMapping(Class<?> sourceClass, Class<?> targetClass) {
		if (!ADDITIONAL_CLASS_MAPPINGS.containsKey(sourceClass)) {
			ADDITIONAL_CLASS_MAPPINGS.put(sourceClass, new ArrayList<Class<?>>());
		}
		ADDITIONAL_CLASS_MAPPINGS.get(sourceClass).add(targetClass);
	}
	
	protected List<Class<?>> getOtherReferencedClasses(Class<?> sourceClass) {
		if (ADDITIONAL_CLASS_MAPPINGS.containsKey(sourceClass)) {
			return ADDITIONAL_CLASS_MAPPINGS.get(sourceClass);
		} else {
			return new ArrayList<>(0);
		}
	}
	
	protected void setSheetNameMapping(Class<?> clazz, String sheetName) {
		SHEET_NAME_MAPPING.put(clazz, sheetName);
	}
	
	protected String getSheetName(Class<?> clazz) {
		if (SHEET_NAME_MAPPING.containsKey(clazz)) {
			return SHEET_NAME_MAPPING.get(clazz);
		} else {
			return clazz.getSimpleName();
		}
	}
	
	private GenericConversionService getConversionService(GenericConverter... converters) {
		GenericConversionService service = new GenericConversionService();
		
		for (GenericConverter converter : converters) {
			service.addConverter(converter);
		}
		customizeConversionService(service);
		
		DefaultConversionService.addDefaultConverters(service);
		
		return service;
	}

	protected abstract void customizeConversionService(GenericConversionService conversionService);

	protected <E extends GenericEntity<Long, ?>> void afterImportItem(E item) {
	}

	protected static class DataSource implements AutoCloseable {
		private final InputStream inputStream;
		private final String filename;
		
		public DataSource(InputStream inputStream, String filename) {
			this.inputStream = inputStream;
			this.filename = filename;
		}
		
		public InputStream getInputStream() {
			return inputStream;
		}
		
		public String getFilename() {
			return filename;
		}
		
		@Override
		public void close() throws Exception {
			inputStream.close();
		}
	}
}
