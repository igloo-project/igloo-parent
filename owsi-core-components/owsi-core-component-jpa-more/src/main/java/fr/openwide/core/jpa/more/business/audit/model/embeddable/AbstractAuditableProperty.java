package fr.openwide.core.jpa.more.business.audit.model.embeddable;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Resolution;

import com.google.common.base.Objects;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@MappedSuperclass
public abstract class AbstractAuditableProperty<T> implements Serializable {

	private static final long serialVersionUID = -5707236459217540376L;

	public static final String LAST_EDIT_DATE_SORT = "lastEditDateSort";

	@DateBridge(resolution = Resolution.MILLISECOND)
	@Temporal(TemporalType.TIMESTAMP)
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD)),
		@Field(name = LAST_EDIT_DATE_SORT, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private Date lastEditDate;

	public abstract T getValue();

	public abstract void setValue(T value);

	public final Date getLastEditDate() {
		return CloneUtils.clone(lastEditDate);
	}

	public final void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = CloneUtils.clone(lastEditDate);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("value", getValue())
				.append("lastEditDate", getLastEditDate())
				.build();
	}

	@Override
	public final boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (!(getClass().equals(object.getClass()))) {
			return false;
		}

		AbstractAuditableProperty<?> other = (AbstractAuditableProperty<?>) object;
		return Objects.equal(getValue(), other.getValue());
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(getValue());
	}

}
