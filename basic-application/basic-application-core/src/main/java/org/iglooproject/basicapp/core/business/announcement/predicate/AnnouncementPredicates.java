package org.iglooproject.basicapp.core.business.announcement.predicate;

import java.util.Collection;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


public final class AnnouncementPredicates {

	public static SerializablePredicate2<Announcement> enabled() {
		return Predicates2.notNullAnd(
			Predicates2.compose(Predicates2.isTrue(), Bindings.announcement().enabled())
		);
	}

	public static SerializablePredicate2<Announcement> disabled() {
		return Predicates2.notNullAndNot(enabled());
	}

	public static SerializablePredicate2<Announcement> type(AnnouncementType type, AnnouncementType ... otherTypes) {
		return type(ImmutableList.copyOf(Lists.asList(type, otherTypes)));
	}

	public static SerializablePredicate2<Announcement> type(Collection<AnnouncementType> types) {
		return Predicates2.notNullAnd(
			Predicates2.compose(Predicates2.in(types), Bindings.announcement().type())
		);
	}

	private AnnouncementPredicates() {
	}

}
