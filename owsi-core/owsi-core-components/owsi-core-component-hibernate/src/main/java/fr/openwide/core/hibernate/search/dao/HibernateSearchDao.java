package fr.openwide.core.hibernate.search.dao;

import java.util.List;

import org.apache.lucene.search.Query;

import fr.openwide.core.hibernate.exception.ServiceException;


public interface HibernateSearchDao {

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	void reindexAll() throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery)
			throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery)
			throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException;
	
}
