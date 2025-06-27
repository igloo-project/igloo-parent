package org.iglooproject.jpa.querydsl.group;

import com.querydsl.core.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.GroupCollector;
import com.querydsl.core.group.GroupExpression;

class GroupExpressionResultTransformer<T, R> implements ResultTransformer<R> {

  private final GroupExpression<T, R> groupExpression;

  public GroupExpressionResultTransformer(GroupExpression<T, R> groupExpression) {
    super();
    this.groupExpression = groupExpression;
  }

  @Override
  public R transform(FetchableQuery<?, ?> query) {
    CloseableIterator<T> iter = query.select(groupExpression.getExpression()).iterate();
    GroupCollector<T, R> collector = groupExpression.createGroupCollector();
    try {
      while (iter.hasNext()) {
        T collectable = iter.next();
        collector.add(collectable);
      }
    } finally {
      iter.close();
    }
    return collector.get();
  }
}
