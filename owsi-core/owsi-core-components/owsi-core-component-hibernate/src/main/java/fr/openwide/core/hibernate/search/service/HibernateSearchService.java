package fr.openwide.core.hibernate.search.service;

import java.util.List;

import fr.openwide.core.hibernate.exception.ServiceException;

public interface HibernateSearchService {

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;
	
	void reindexAll() throws ServiceException;
	
}
