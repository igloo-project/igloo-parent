package fr.openwide.core.jpa.more.business.file.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextException;

import fr.openwide.core.jpa.more.business.file.model.FileStore;

public class AbstractFileStoreServiceImpl {
	
	private Map<String, FileStore> fileStores = new HashMap<String, FileStore>();
	
	protected void registerFileStore(FileStore fileStore) {
		try {
			fileStore.check();
			
			fileStores.put(fileStore.getKey(), fileStore);
		} catch (Exception e) {
			throw new ApplicationContextException("Unable to initialize the FileStore service: " + fileStore.getKey(), e);
		}
	}
	
	protected FileStore getFileStore(String key) {
		if (fileStores.containsKey(key)) {
			return fileStores.get(key);
		} else {
			throw new IllegalArgumentException("Unable to find fileStore " + key);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	protected <T extends FileStore> T getFileStore(Class<T> clazz, String key) {
		if (fileStores.containsKey(key) && clazz.isAssignableFrom(fileStores.get(key).getClass())) {
			return (T) fileStores.get(key);
		} else {
			throw new IllegalArgumentException("Unable to find fileStore " + key);
		}
	}
}