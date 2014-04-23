package fr.openwide.core.jpa.more.business.sort;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.search.SortField;

import com.mysema.query.types.OrderSpecifier;

/**
 * @param <S> The sort field type (@see {@link SortField}, {@link OrderSpecifier}, etc.).
 * 			  This type must describe the field on which sort is applied and the way the sort is applied.
 */
public interface ISort<S> extends Serializable {
	
	List<S> getSortFields(SortOrder sortOrder);
	
	SortOrder getDefaultOrder();
	
	public enum SortOrder {
		ASC {
			@Override public SortOrder reverse() { return DESC; }
		},
		DESC {
			@Override public SortOrder reverse() { return ASC; }
		};
		
		public abstract SortOrder reverse();
		
		public SortOrder asDefaultFor(SortOrder sortOrder) {
			return sortOrder == null ? this : sortOrder;
		}
	}
}
