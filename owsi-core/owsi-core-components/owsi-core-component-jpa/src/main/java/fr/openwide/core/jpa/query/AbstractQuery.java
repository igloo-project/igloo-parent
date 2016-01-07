package fr.openwide.core.jpa.query;

import java.util.List;

public class AbstractQuery<T, TQueryImpl> implements IQuery<T> {

	@Override
	public List<T> fullList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> list(long offset, long limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
