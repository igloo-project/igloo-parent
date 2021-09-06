package org.iglooproject.jpa.more.util.init.service;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.DATABASE_INITIALIZED;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.commons.io.FileUtils;
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
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import net.java.truevfs.access.TFileInputStream;

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
	public void importDirectory(File directory) throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping = new HashMap<>();
		
		importBeforeReferenceData(directory, idsMapping);
		
		File referenceDataFile = getFirstFile(directory, REFERENCE_DATA_FILE_NAMES);
		
		LOGGER.info("Importing {}", referenceDataFile.getName());
		Workbook referenceDataWorkbook = getWorkbook(referenceDataFile);
		importReferenceData(idsMapping, referenceDataWorkbook);
		LOGGER.info("Import of {} complete", referenceDataFile.getName());
		
		importAfterReferenceData(directory, idsMapping);
		
		importBeforeBusinessData(directory, idsMapping);
		
		File businessDataFile = getFirstFile(directory, BUSINESS_DATA_FILE_NAMES);
		
		LOGGER.info("Importing {}", businessDataFile.getName());
		Workbook businessItemWorkbook = getWorkbook(businessDataFile);
		importMainBusinessItems(idsMapping, businessItemWorkbook);
		LOGGER.info("Import of {} complete", businessDataFile.getName());
		
		importAfterBusinessData(directory, idsMapping);
		
		importFiles(directory, idsMapping);
		
		propertyService.set(DATABASE_INITIALIZED, true);
		
		LOGGER.info("Import complete");
	}
	
	protected File getFirstFile(File directory, List<String> fileNames) {
		Objects.requireNonNull(directory);
		Objects.requireNonNull(fileNames);
		
		return FileUtils.listFiles(directory, new NameFileFilter(fileNames))
			.stream()
			.sorted(Comparator.comparing(File::getName, Ordering.explicit(fileNames).nullsLast()))
			.findFirst()
			.orElseThrow(NoSuchElementException::new);
	}
	
	protected Workbook getWorkbook(File file) throws FileNotFoundException, IOException {
		Objects.requireNonNull(file);
		
		String fileExtension = FilenameUtils.getExtension(file.getPath());
		
		if (MediaType.APPLICATION_MS_EXCEL.extension().equals(fileExtension)) {
			return new HSSFWorkbook(new TFileInputStream(file));
		} else if (MediaType.APPLICATION_OPENXML_EXCEL.extension().equals(fileExtension)) {
			return new XSSFWorkbook(new TFileInputStream(file));
		}
		
		throw new IllegalStateException();
	}
	
	protected void importBeforeReferenceData(File directory, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importAfterReferenceData(File directory, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importBeforeBusinessData(File directory, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
			throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		// nothing, override if necessary
	}
	
	protected void importAfterBusinessData(File directory, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping)
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
	
	protected void importFiles(File directory, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping) 
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
		
		DefaultConversionService.addDefaultConverters(service);
		
		return service;
	}

	protected <E extends GenericEntity<Long, ?>> void afterImportItem(E item) {
	}
}
