package org.iglooproject.test.commons.util.collections;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.function.BiFunction;

import org.assertj.core.api.Assertions;
import org.iglooproject.commons.util.collections.range.time.DateDiscreteDomain;
import org.iglooproject.commons.util.collections.range.time.PartitionDiscreteDomain;
import org.iglooproject.commons.util.collections.range.time.ZonedDateDiscreteDomain;
import org.junit.Test;

/**
 * <p>
 * <b>Date</b> is 31/01/2018 14:32:02; we check that start and end of period, and previous, and next are corrects.
 * Period can be calendar month or calendar week (start on monday).
 * </p>
 * 
 * <p>Built with <em>cal -3</em> command.</p>
 * <pre>
 *     January 2018          February 2018          March 2018     
 * Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa
 *     1  2  3  4  5  6               1  2  3               1  2  3 
 *  7  8  9 10 11 12 13   4  5  6  7  8  9 10   4  5  6  7  8  9 10 
 * 14 15 16 17 18 19 20  11 12 13 14 15 16 17  11 12 13 14 15 16 17 
 * 21 22 23 24 25 26 27  18 19 20 21 22 23 24  18 19 20 21 22 23 24 
 * 28 29 30 31           25 26 27 28           25 26 27 28 29 30 31
 * </pre>
 */
public class DateDiscreteDomainTest {
	
	
	private static final String DATE = "2018-01-31T14:32:02+01:00[Europe/Paris]";
	private static final String WEEK_PREVIOUS = "2018-01-22T00:00:00+01:00[Europe/Paris]";
	private static final String WEEK_START = "2018-01-29T00:00:00+01:00[Europe/Paris]";
	private static final String WEEK_END = "2018-02-05T00:00:00+01:00[Europe/Paris]";
	private static final String WEEK_NEXT = "2018-02-12T00:00:00+01:00[Europe/Paris]";
	private static final String MONTH_PREVIOUS = "2017-12-01T00:00:00+01:00[Europe/Paris]";
	private static final String MONTH_START = "2018-01-01T00:00:00+01:00[Europe/Paris]";
	private static final String MONTH_END = "2018-02-01T00:00:00+01:00[Europe/Paris]";
	private static final String MONTH_NEXT = "2018-03-01T00:00:00+01:00[Europe/Paris]";

	@Test
	public void dateWeekRange() {
		this.<LocalDateTime>range(DateDiscreteDomain.weeks(), LocalDateTime::parse,
				DATE,
				WEEK_PREVIOUS,
				WEEK_START,
				WEEK_END,
				WEEK_NEXT
		);
	}

	@Test
	public void zonedDateTimeWeekRange() {
		this.<ZonedDateTime>range(ZonedDateDiscreteDomain.weeks(), ZonedDateTime::parse,
				DATE,
				WEEK_PREVIOUS,
				WEEK_START,
				WEEK_END,
				WEEK_NEXT
		);
	}

	@Test
	public void dateMonthRange() {
		this.<LocalDateTime>range(DateDiscreteDomain.months(), LocalDateTime::parse,
				DATE,
				MONTH_PREVIOUS,
				MONTH_START,
				MONTH_END,
				MONTH_NEXT
		);
	}

	public void zonedDateTimeMonthRange() {
		this.<ZonedDateTime>range(ZonedDateDiscreteDomain.months(), ZonedDateTime::parse,
				DATE,
				MONTH_PREVIOUS,
				MONTH_START,
				MONTH_END,
				MONTH_NEXT
		);
	}

	public <T extends Temporal & Comparable<? super T>> void range(
			PartitionDiscreteDomain<T> domain, BiFunction<String, DateTimeFormatter, T> generator,
			String value, String previous, String start, String end, String next) {
		T date = generator.apply(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		T previousDate = generator.apply(previous, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		T startDate = generator.apply(start, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		T endDate = generator.apply(end, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		T nextDate = generator.apply(next, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		
		Assertions.assertThat(domain.previous(date)).isEqualTo(startDate);
		Assertions.assertThat(domain.next(date)).isEqualTo(endDate);
		
		Assertions.assertThat(domain.previous(startDate)).isEqualTo(previousDate);
		Assertions.assertThat(domain.alignPrevious(startDate)).isEqualTo(startDate);
		Assertions.assertThat(domain.next(startDate)).isEqualTo(endDate);
		Assertions.assertThat(domain.alignNext(startDate)).isEqualTo(startDate);
		
		Assertions.assertThat(domain.previous(endDate)).isEqualTo(startDate);
		Assertions.assertThat(domain.alignPrevious(endDate)).isEqualTo(endDate);
		Assertions.assertThat(domain.next(endDate)).isEqualTo(nextDate);
		Assertions.assertThat(domain.alignNext(endDate)).isEqualTo(endDate);
	}

	/**
	 * Check {@link DateDiscreteDomain} is serializable.
	 */
	@Test
	public void serialize() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(DateDiscreteDomain.weeks());
		DateDiscreteDomain domain = (DateDiscreteDomain) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
		this.<LocalDateTime>range(domain, LocalDateTime::parse,
				DATE,
				WEEK_PREVIOUS,
				WEEK_START,
				WEEK_END,
				WEEK_NEXT
		);
	}
}
