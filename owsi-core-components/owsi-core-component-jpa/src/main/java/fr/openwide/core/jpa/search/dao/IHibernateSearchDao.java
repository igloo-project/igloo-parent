package fr.openwide.core.jpa.search.dao;

import java.util.List;

import org.apache.lucene.search.Query;

import fr.openwide.core.jpa.exception.ServiceException;


public interface IHibernateSearchDao {

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Integer limit, Integer offset) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset)
			throws ServiceException;

	void reindexAll() throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery)
			throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery,
			Integer limit, Integer offset) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery)
			throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset)
			throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery, Integer limit, Integer offset) throws ServiceException;
	
}
