package org.iglooproject.jpa.more.business.file.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;

import org.iglooproject.jpa.more.business.file.model.IFileStore;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.iglooproject.spring.util.StringUtils;

public class AbstractFileStoreServiceImpl {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private Map<String, IFileStore> fileStores = new HashMap<>();
	
	protected void registerFileStore(IFileStore fileStore) {
		try {
			fileStore.check();
			SpringBeanUtils.autowireBean(applicationContext, fileStore);
			
			fileStores.put(fileStore.getKey(), fileStore);
		} catch (RuntimeException e) {
			throw new ApplicationContextException("Unable to initialize the FileStore service: " + fileStore.getKey(), e);
		}
	}
	
	protected IFileStore getFileStore(String key) {
		if (fileStores.containsKey(key)) {
			return fileStores.get(key);
		} else {
			throw new IllegalArgumentException("Unable to find fileStore " + key);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	protected <T extends IFileStore> T getFileStore(Class<T> clazz, String key) {
		if (fileStores.containsKey(key) && clazz.isAssignableFrom(fileStores.get(key).getClass())) {
			return (T) fileStores.get(key);
		} else {
			throw new IllegalArgumentException("Unable to find fileStore " + key);
		}
	}
	
	protected String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}
	
	protected String getFileExtension(String fileName) {
		return StringUtils.lowerCase(FilenameUtils.getExtension(fileName));
	}
}