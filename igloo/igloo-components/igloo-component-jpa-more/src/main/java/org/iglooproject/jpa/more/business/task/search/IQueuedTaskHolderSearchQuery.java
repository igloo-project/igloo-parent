package org.iglooproject.jpa.more.business.task.search;

import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IQueuedTaskHolderSearchQuery
    extends IHibernateSearchSearchQuery<
        QueuedTaskHolder, QueuedTaskHolderSort, QueuedTaskHolderSearchQueryData> {}
