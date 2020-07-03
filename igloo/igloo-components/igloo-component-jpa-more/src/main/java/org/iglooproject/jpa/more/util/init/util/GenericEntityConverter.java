package org.iglooproject.jpa.more.util.init.util;

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

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.util.init.dao.IImportDataDao;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.iglooproject.spring.util.StringUtils;


public class GenericEntityConverter implements GenericConverter {
	
	private ConversionService conversionService;
	
	private IImportDataDao importDataDao;
	
	private Workbook workbook;
	
	private Map<Class<?>, Class<?>> classMapping;
	
	private Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping;
	
	public GenericEntityConverter(IImportDataDao importDataDao, Workbook workbook,
			Map<Class<?>, Class<?>> classMapping, Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping) {
		super();
		
		this.importDataDao = importDataDao;
		this.workbook = workbook;
		this.classMapping = classMapping;
		this.idsMapping = idsMapping;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertibleTypes = new LinkedHashSet<>();
		
		convertibleTypes.add(new ConvertiblePair(String.class, GenericEntity.class));
		
		return Collections.unmodifiableSet(convertibleTypes);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Object item = null;

		String importId = StringUtils.trimWhitespace((String)source);
		if (StringUtils.hasText(importId)) {
			item = convertFromDatabase(importId, targetType);
			
			if (item == null) {
				item = convertFromWorkbook(importId, targetType);
			}
		}
		
		return item;
	}
	
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	private Object convertFromDatabase(String importId, TypeDescriptor targetType) {
		Class<GenericEntity<Long, ?>> clazz = getTargetClass(targetType);
		
		Long databaseId = getDatabaseId(clazz, importId);
		
		if (databaseId != null) {
			return importDataDao.getById(clazz, databaseId);
		}
		return null;
	}
	
	private Object convertFromWorkbook(String importId, TypeDescriptor targetType) {
		Class<GenericEntity<Long, ?>> clazz = getTargetClass(targetType);
		
		Sheet sheet = workbook.getSheet(clazz.getSimpleName());
		if (sheet != null) {
			Map<String, Object> itemData = getItemData(sheet, importId);
			if (itemData != null) {
				if (!idsMapping.containsKey(clazz.getName())) {
					idsMapping.put(clazz.getName(), new HashMap<String, GenericEntity<Long, ?>>());
				}
				
				GenericEntity<Long, ?> item = BeanUtils.instantiateClass(clazz);
				
				BeanWrapper wrapper = SpringBeanUtils.getBeanWrapper(item);
				wrapper.setConversionService(conversionService);
				wrapper.setPropertyValues(new MutablePropertyValues(itemData), true);
				
				idsMapping.get(clazz.getName()).put(importId, item);
				
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
	
	private Long getDatabaseId(Class<GenericEntity<Long, ?>> clazz, String importId) {
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
