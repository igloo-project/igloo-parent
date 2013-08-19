package fr.openwide.core.jpa.more.util.init.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Maps;

import de.schlichtherle.truezip.file.TFileInputStream;
import fr.openwide.core.commons.util.FileUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.parameter.service.IAbstractParameterService;
import fr.openwide.core.jpa.more.util.init.dao.IImportDataDao;
import fr.openwide.core.jpa.more.util.init.util.GenericEntityConverter;
import fr.openwide.core.jpa.more.util.init.util.WorkbookUtils;
import fr.openwide.core.spring.util.ReflectionUtils;
import fr.openwide.core.spring.util.SpringBeanUtils;

public abstract class AbstractImportDataServiceImpl implements IImportDataService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportDataServiceImpl.class);
	
	private static final String REFERENCE_DATA_FILE = "reference_data.xls";
	
	private static final String BUSINESS_DATA_FILE = "business_data.xls";
	
	private static final String ID_FIELD_NAME = "id";
	
	private static final String CREATION_DATE_FIELD_NAME = "creationDate";
	
	private static final String LAST_UPDATE_DATE_FIELD_NAME = "lastUpdateDate";
	
	private static final String PASSWORD_FIELD_NAME = "password";
	
	private static final String PASSWORD_HASH_FIELD_NAME = "passwordHash";
	
	private static final Map<Class<?>, List<Class<?>>> ADDITIONAL_CLASS_MAPPINGS = new HashMap<Class<?>, List<Class<?>>>();
	
	private static final Map<Class<?>, String> SHEET_NAME_MAPPING = new HashMap<Class<?>, String>();
	
	@Autowired
	private IImportDataDao importDataDao;
	
	@Autowired
	private IAbstractParameterService parameterService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired(required = false)
	private SaltSource saltSource;
	
	@Override
	public void importDirectory(File directory) throws ServiceException, SecurityServiceException, FileNotFoundException, IOException {
		Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping = new HashMap<String, Map<Long, GenericEntity<Long, ?>>>();
		
		Workbook genericListItemWorkbook = new HSSFWorkbook(new TFileInputStream(FileUtils.getFile(directory, REFERENCE_DATA_FILE)));
		importGenericListItems(idsMapping, genericListItemWorkbook);
		
		Workbook businessItemWorkbook = new HSSFWorkbook(new TFileInputStream(FileUtils.getFile(directory, BUSINESS_DATA_FILE)));
		importMainBusinessItems(idsMapping, businessItemWorkbook);
		
		importFiles(directory, idsMapping);
		
		parameterService.setDatabaseInitialized(true);
		
		LOGGER.info("Import complete");
	}
	
	protected abstract List<String> getGenericListItemPackagesToScan();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void importGenericListItems(Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping, Workbook workbook) {
		for (String packageToScan : getGenericListItemPackagesToScan()) {
			Set<Class<? extends GenericListItem>> classes = ReflectionUtils.findAssignableClasses(packageToScan, GenericListItem.class);
			Map<Integer, Class<? extends GenericListItem>> orderedClasses = Maps.newTreeMap();
			
			for (Class<? extends GenericListItem> genericListItemClass : classes) {
				Sheet sheet = workbook.getSheet(getSheetName(genericListItemClass));
				
				if (sheet != null) {
					orderedClasses.put(sheet.getWorkbook().getSheetIndex(sheet), genericListItemClass);
				}
			}
			
			for (Class<? extends GenericListItem> genericListItemClass : orderedClasses.values()) {
				doImportItem(idsMapping, workbook, genericListItemClass);
			}
		}
	}
	
	protected abstract void importMainBusinessItems(Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping, Workbook workbook);
	
	protected void importFiles(File directory, Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping) 
			throws ServiceException, SecurityServiceException {
		// nothing, override if necessary
	}
	
	protected <E extends GenericEntity<Long, ?>> void doImportItem(Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping,
				Workbook workbook, Class<E> clazz) {
		Sheet sheet = workbook.getSheet(getSheetName(clazz));
		if (sheet != null) {
			GenericEntityConverter converter = new GenericEntityConverter(importDataDao, workbook,
					new HashMap<Class<?>, Class<?>>(0), idsMapping);
			GenericConversionService conversionService = getConversionService(converter);
			converter.setConversionService(conversionService);
			
			Map<Long, GenericEntity<Long, ?>> idsMappingForClass = new HashMap<Long, GenericEntity<Long, ?>>();
			idsMapping.put(clazz.getName(), idsMappingForClass);
			
			for (Class<?> referencedClass : getOtherReferencedClasses(clazz)) {
				if (!idsMapping.containsKey(referencedClass.getName())) {
					idsMapping.put(referencedClass.getName(), new HashMap<Long, GenericEntity<Long, ?>>());
				}
			}
			
			for (Map<String, Object> line : WorkbookUtils.getSheetContent(sheet)) {
				E item = BeanUtils.instantiateClass(clazz);
				
				Long importId = Long.parseLong(line.get(ID_FIELD_NAME).toString());
				line.remove(ID_FIELD_NAME);
				
				doFilterLine(clazz, line);
				
				BeanWrapper wrapper = SpringBeanUtils.getBeanWrapper(item);
				wrapper.setConversionService(conversionService);
				wrapper.setPropertyValues(new MutablePropertyValues(line), true);
				
				importDataDao.create(item);
				
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
			return new ArrayList<Class<?>>(0);
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
}
