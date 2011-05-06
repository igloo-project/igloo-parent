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
	
	private Map<String, Map<Integer, GenericEntity<Integer, ?>>> idsMapping;
	
	public GenericEntityConverter(IImportDataDao importDataDao, Workbook workbook,
			Map<Class<?>, Class<?>> classMapping, Map<String, Map<Integer, GenericEntity<Integer, ?>>> idsMapping) {
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
		Class<GenericEntity<Integer, ?>> clazz = getTargetClass(targetType);
		
		Integer importId = Integer.valueOf((String) source);
		Integer databaseId = getDatabaseId(clazz, importId);
		
		if (databaseId != null) {
			return importDataDao.getById(clazz, databaseId);
		}
		return null;
	}
	
	private Object convertFromWorkbook(Object source, TypeDescriptor targetType) {
		Class<GenericEntity<Integer, ?>> clazz = getTargetClass(targetType);
		String importId = StringUtils.trimWhitespace((String) source);
		
		Sheet sheet = workbook.getSheet(clazz.getSimpleName());
		if (sheet != null) {
			Map<String, Object> itemData = getItemData(sheet, importId);
			if (itemData != null) {
				if (!idsMapping.containsKey(clazz.getName())) {
					idsMapping.put(clazz.getName(), new HashMap<Integer, GenericEntity<Integer, ?>>());
				}
				
				GenericEntity<Integer, ?> item = BeanUtils.instantiateClass(clazz);
				
				BeanWrapper wrapper = SpringBeanUtils.getBeanWrapper(item);
				wrapper.setConversionService(conversionService);
				wrapper.setPropertyValues(new MutablePropertyValues(itemData), true);
				
				idsMapping.get(clazz.getName()).put(Integer.parseInt(importId), item);
				
				return item;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<GenericEntity<Integer, ?>> getTargetClass(TypeDescriptor targetType) {
		Class<GenericEntity<Integer, ?>> clazz = (Class<GenericEntity<Integer, ?>>) targetType.getType();
		
		if (classMapping.containsKey(clazz)) {
			clazz = (Class<GenericEntity<Integer, ?>>) classMapping.get(clazz);
		}
		
		return clazz;
	}
	
	private Integer getDatabaseId(Class<GenericEntity<Integer, ?>> clazz, Integer importId) {
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
