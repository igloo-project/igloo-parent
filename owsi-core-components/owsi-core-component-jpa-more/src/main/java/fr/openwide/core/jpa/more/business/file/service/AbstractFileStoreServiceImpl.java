package fr.openwide.core.jpa.more.business.file.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;

import fr.openwide.core.jpa.more.business.file.model.IFileStore;
import fr.openwide.core.spring.util.SpringBeanUtils;

public class AbstractFileStoreServiceImpl {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private Map<String, IFileStore> fileStores = new HashMap<String, IFileStore>();
	
	protected void registerFileStore(IFileStore fileStore) {
		try {
			fileStore.check();
			SpringBeanUtils.autowireBean(applicationContext, fileStore);
			
			fileStores.put(fileStore.getKey(), fileStore);
		} catch (Exception e) {
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
}