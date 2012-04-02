package fr.openwide.core.jpa.more.util.init.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.util.init.dao.IImportDataDao;
import fr.openwide.core.spring.util.SpringBeanUtils;
import fr.openwide.core.spring.util.StringUtils;


public class GenericEntityConverter implements GenericConverter {
	
	private ConversionService conversionService;
	
	private IImportDataDao importDataDao;
	
	private Workbook workbook;
	
	private Map<Class<?>, Class<?>> classMapping;
	
	private Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping;
	
	public GenericEntityConverter(IImportDataDao importDataDao, Workbook workbook,
			Map<Class<?>, Class<?>> classMapping, Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping) {
		super();
		
		this.importDataDao = importDataDao;
		this.workbook = workbook;
		this.classMapping = classMapping;
		this.idsMapping = idsMapping;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertibleTypes = new LinkedHashSet<ConvertiblePair>();
		
		convertibleTypes.add(new ConvertiblePair(String.class, GenericEntity.class));
		
		return Collections.unmodifiableSet(convertibleTypes);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Object item = null;
		
		if (StringUtils.hasText((String) source)) {
			item = convertFromDatabase(source, targetType);
			
			if (item == null) {
				item = convertFromWorkbook(source, targetType);
			}
		}
		return item;
	}
	
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	private Object convertFromDatabase(Object source, TypeDescriptor targetType) {
		Class<GenericEntity<Long, ?>> clazz = getTargetClass(targetType);
		
		Long importId = Long.parseLong((String) source);
		Long databaseId = getDatabaseId(clazz, importId);
		
		if (databaseId != null) {
			return importDataDao.getById(clazz, databaseId);
		}
		return null;
	}
	
	private Object convertFromWorkbook(Object source, TypeDescriptor targetType) {
		Class<GenericEntity<Long, ?>> clazz = getTargetClass(targetType);
		String importId = StringUtils.trimWhitespace((String) source);
		
		Sheet sheet = workbook.getSheet(clazz.getSimpleName());
		if (sheet != null) {
			Map<String, Object> itemData = getItemData(sheet, importId);
			if (itemData != null) {
				if (!idsMapping.containsKey(clazz.getName())) {
					idsMapping.put(clazz.getName(), new HashMap<Long, GenericEntity<Long, ?>>());
				}
				
				GenericEntity<Long, ?> item = BeanUtils.instantiateClass(clazz);
				
				BeanWrapper wrapper = SpringBeanUtils.getBeanWrapper(item);
				wrapper.setConversionService(conversionService);
				wrapper.setPropertyValues(new MutablePropertyValues(itemData), true);
				
				idsMapping.get(clazz.getName()).put(Long.parseLong(importId), item);
				
				return item;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<GenericEntity<Long, ?>> getTargetClass(TypeDescriptor targetType) {
		Class<GenericEntity<Long, ?>> clazz = (Class<GenericEntity<Long, ?>>) targetType.getType();
		
		if (classMapping.containsKey(clazz)) {
			clazz = (Class<GenericEntity<Long, ?>>) classMapping.get(clazz);
		}
		
		return clazz;
	}
	
	private Long getDatabaseId(Class<GenericEntity<Long, ?>> clazz, Long importId) {
		if (idsMapping.containsKey(clazz.getName()) && idsMapping.get(clazz.getName()).containsKey(importId)) {
			return idsMapping.get(clazz.getName()).get(importId).getId();
		} else {
			return null;
		}
	}
	
	private Map<String, Object> getItemData(Sheet sheet, String importId) {
		for (Map<String, Object> line : WorkbookUtils.getSheetContent(sheet)) {
			if (line.containsKey("id") && importId.equals(line.get("id"))) {
				line.remove("id");
				return line;
			}
		}
		return null;
	}
	

}
