package fr.openwide.core.basicapp.core.util.search;

import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.bindgen.binding.AbstractBinding;
import org.hibernate.search.Environment;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.spring.util.StringUtils;

public final class BasicApplicationHibernateSearchUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationHibernateSearchUtils.class);
	
	private static final Function<AbstractBinding<?, String>, String> BINDING_TO_PATH_FUNCTION = new BindingToPathFunction();
	
	private BasicApplicationHibernateSearchUtils() { }
	
	public static void mustIfNotNull(BooleanJunction<?> junction, Query ... queries) {
		for (Query query : queries) {
			if (query != null) {
				junction.must(query);
			}
		}
	}
	
	public static <T> Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, T> binding, T value) {
		return matchIfGiven(builder, binding.getPath(), value);
	}
	
	public static <T> Query matchIfGiven(QueryBuilder builder, String fieldPath, T value) {
		if (value != null) {
			return builder.keyword()
					.onField(fieldPath)
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	public static Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchIfGiven(builder, binding.getPath(), terms);
	}
	
	public static Query matchIfGiven(QueryBuilder builder, String fieldPath, String terms) {
		return matchOneTermIfGiven(builder, fieldPath, terms);
	}
	
	public static Query matchOneTermIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchOneTermIfGiven(builder, binding.getPath(), terms);
	}
	
	public static Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms) {
		if (StringUtils.hasText(terms)) {
			return builder.keyword()
					.onField(fieldPath)
					.matching(terms)
					.createQuery();
		}
		
		return null;
	}
	
	public static Query matchAllTermsIfGiven(Analyzer analyzer, AbstractBinding<?, String> binding, String terms) {
		return matchAllTermsIfGiven(analyzer, binding.getPath(), terms);
	}
	
	public static Query matchAllTermsIfGiven(Analyzer analyzer, String fieldPath, String terms) {
		if (StringUtils.hasText(terms)) {
			QueryParser parser = new QueryParser(Version.LUCENE_36, fieldPath, analyzer);
			parser.setDefaultOperator(Operator.AND);
			try {
				return parser.parse(QueryParser.escape(terms));
			} catch (ParseException e) {
				LOGGER.error("Erreur lors du parsing d'une chaîne échapée (a priori impossible ?)", e);
			}
		}
		
		return null;
	}
	
	@SafeVarargs
	public static Query matchAllTermsMultifieldIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAllTermsMultifieldIfGiven(analyzer, terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	public static Query matchAllTermsMultifieldIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
		if (StringUtils.hasText(terms)) {
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Environment.DEFAULT_LUCENE_MATCH_VERSION,
					Iterables.toArray(fieldPaths, String.class),
					analyzer
			);
			parser.setDefaultOperator(Operator.AND);
			try {
				return parser.parse(QueryParser.escape(terms));
			} catch (ParseException e) {
				LOGGER.error("Erreur lors du parsing d'une chaîne échapée (a priori impossible ?)", e);
			}
		}
		
		return null;
	}
	
	public static <T> Query beIncludedIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<T>> binding, T value) {
		if (value != null) {
			return builder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	public static <T> Query matchOneIfGiven(QueryBuilder builder, AbstractBinding<?, T> binding, Collection<? extends T> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanJunction<?> subJunction = builder.bool();
			for (T possibleValue : possibleValues) {
				subJunction.should(builder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
	}
	
	public static <T> Query matchAllIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<T>> binding, Collection<? extends T> values) {
		if (values != null && !values.isEmpty()) {
			BooleanJunction<?> subJunction = builder.bool();
			for (T possibleValue : values) {
				subJunction.must(builder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
	}
	
	public static Query matchIfTrue(QueryBuilder builder, AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		if (mustMatch != null && mustMatch) {
			return builder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		return null;
	}
	
	private static class BindingToPathFunction implements Function<AbstractBinding<?, String>, String> {
		@Override
		public String apply(AbstractBinding<?, String> input) {
			if (input == null) {
				throw new IllegalStateException("Path may not be null.");
			}
			return input.getPath();
		}
	}

}
