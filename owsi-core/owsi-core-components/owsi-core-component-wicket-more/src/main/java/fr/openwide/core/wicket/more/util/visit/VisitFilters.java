package fr.openwide.core.wicket.more.util.visit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.util.visit.IVisitFilter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

public class VisitFilters {
	
	private VisitFilters() {
	}
	
	private static final class TypeSafePredicate<T> implements Predicate<Object>, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final Class<T> clazz;
		private final Predicate<? super T> delegate;
		
		public TypeSafePredicate(Class<T> clazz, Predicate<? super T> delegate) {
			super();
			this.clazz = clazz;
			this.delegate = delegate;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean apply(Object input) {
			return clazz.isInstance(input) && delegate.apply((T)input);
		}
	}
	
	private static final class PredicateVisitFilter implements IVisitFilter, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final Predicate<Object> objectVisitPredicate;
		private final Predicate<Object> compositeVisitPredicate;
		
		public PredicateVisitFilter(Predicate<Object> objectVisitPredicate, Predicate<Object> compositeVisitPredicate) {
			super();
			this.objectVisitPredicate = objectVisitPredicate;
			this.compositeVisitPredicate = compositeVisitPredicate;
		}

		@Override
		public final boolean visitObject(Object object) {
			return objectVisitPredicate.apply(object);
		}

		@Override
		public final boolean visitChildren(Object object) {
			return compositeVisitPredicate.apply(object);
		}
	}

	public static <T> IVisitFilter downToExcluding(Class<T> clazz, Predicate<? super T> predicate) {
		Predicate<Object> objectPredicate = Predicates.not(new TypeSafePredicate<>(clazz, predicate));
		return new PredicateVisitFilter(objectPredicate, objectPredicate);
	}
	
	public static <T> IVisitFilter including(Class<T> clazz, Predicate<? super T> predicate) {
		Predicate<Object> objectPredicate = new TypeSafePredicate<>(clazz, predicate);
		return new PredicateVisitFilter(objectPredicate, Predicates.alwaysTrue());
	}
	
	public static <T> IVisitFilter excluding(Class<T> clazz, Predicate<? super T> predicate) {
		Predicate<Object> objectPredicate = Predicates.not(new TypeSafePredicate<>(clazz, predicate));
		return new PredicateVisitFilter(objectPredicate, Predicates.alwaysTrue());
	}

	public static <T> IVisitFilter downToIncluding(Class<T> clazz, Predicate<? super T> predicate) {
		Predicate<Object> objectPredicate = Predicates.not(new TypeSafePredicate<>(clazz, predicate));
		return new PredicateVisitFilter(Predicates.alwaysTrue(), objectPredicate);
	}

	public static IVisitFilter downToExcluding(Class<?> clazz) {
		return downToExcluding(clazz, Predicates.alwaysTrue());
	}
	
	public static IVisitFilter including(Class<?> clazz) {
		return including(clazz, Predicates.alwaysTrue());
	}
	
	public static <T> IVisitFilter excluding(Class<?> clazz) {
		return excluding(clazz, Predicates.alwaysTrue());
	}

	public static IVisitFilter downToIncluding(Class<?> clazz) {
		return downToIncluding(clazz, Predicates.alwaysTrue());
	}
	
	public static IVisitFilter including(Class<?> first, Class<?> ... others) {
		List<IVisitFilter> filters = Lists.newArrayList();
		for (Class<?> clazz : Lists.asList(first, others)) {
			filters.add(including(clazz));
		}
		return any(filters);
	}

	public static IVisitFilter downToExcluding(Object object) {
		return downToExcluding(Object.class, Predicates.equalTo(object));
	}
	
	public static IVisitFilter excluding(Object object) {
		return excluding(Object.class, Predicates.equalTo(object));
	}

	public static IVisitFilter downToIncluding(Object object) {
		return downToIncluding(Object.class, Predicates.equalTo(object));
	}
	
	public static IVisitFilter every(IVisitFilter ... operands) {
		return new EveryVisitFilter(Arrays.asList(operands));
	}
	
	public static IVisitFilter every(Iterable<? extends IVisitFilter> operands) {
		return new EveryVisitFilter(operands);
	}
	
	private static final class EveryVisitFilter implements IVisitFilter, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final Iterable<? extends IVisitFilter> operands;

		public EveryVisitFilter(Iterable<? extends IVisitFilter> operands) {
			super();
			this.operands = operands;
		}

		@Override
		public boolean visitObject(Object object) {
			for (IVisitFilter filter : operands) {
				if (!filter.visitObject(object)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean visitChildren(Object object) {
			for (IVisitFilter filter : operands) {
				if (!filter.visitChildren(object)) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static IVisitFilter any(IVisitFilter ... operands) {
		return new AnyVisitFilter(Arrays.asList(operands));
	}
	
	public static IVisitFilter any(Iterable<? extends IVisitFilter> operands) {
		return new AnyVisitFilter(operands);
	}
	
	private static final class AnyVisitFilter implements IVisitFilter, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final Iterable<? extends IVisitFilter> operands;

		public AnyVisitFilter(Iterable<? extends IVisitFilter> operands) {
			super();
			this.operands = operands;
		}

		@Override
		public boolean visitObject(Object object) {
			for (IVisitFilter filter : operands) {
				if (filter.visitObject(object)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean visitChildren(Object object) {
			for (IVisitFilter filter : operands) {
				if (filter.visitChildren(object)) {
					return true;
				}
			}
			return false;
		}
	}

	public static IVisitFilter renderedComponents() {
		return RenderedComponentsVisitFilter.INSTANCE;
	}
	
	private enum RenderedComponentsVisitFilter implements IVisitFilter {
		INSTANCE;

		@Override
		public boolean visitObject(Object object) {
			return hasRenderedMarkup(object);
		}

		private boolean hasRenderedMarkup(Object object) {
			if (!(object instanceof Component)) {
				return false;
			}
			Component component = (Component) object;
			if (!component.hasBeenRendered()) {
				return false;
			}
			Component parent = component.getParent();
			return component.isVisibleInHierarchy()
					|| (parent == null || parent.isVisibleInHierarchy()) && component.getOutputMarkupPlaceholderTag();
		}

		@Override
		public boolean visitChildren(Object object) {
			return hasRenderedMarkup(object);
		}
		
	}
}
