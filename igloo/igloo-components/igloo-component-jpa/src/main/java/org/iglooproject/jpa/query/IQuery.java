package org.iglooproject.jpa.query;

import java.util.List;

public interface IQuery<T> {

  List<T> fullList();

  List<T> list(long offset, long limit);

  long count();
}
