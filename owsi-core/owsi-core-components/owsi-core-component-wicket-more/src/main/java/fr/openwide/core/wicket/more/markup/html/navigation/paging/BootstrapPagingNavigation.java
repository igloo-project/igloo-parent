package fr.openwide.core.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;

public class BootstrapPagingNavigation extends PagingNavigation {

	private static final long serialVersionUID = -1227712391251278582L;

	public BootstrapPagingNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		super(id, pageable, labelProvider);
	}

	@Override
	protected void populateItem(LoopItem loopItem) {
		super.populateItem(loopItem);
		long pageIndex = getStartIndex() + loopItem.getIndex();
		if (pageable.getCurrentPage() == pageIndex) {
			loopItem.add(new ClassAttributeAppender("active"));
		}
	}
	
	public boolean isBeginning() {
		return getStartIndex() == 0;
	}
	
	public boolean isEnding() {
		return getStartIndex() + getViewSize() >= pageable.getPageCount();
	}
	
	public boolean lessThanViewSize() {
		return pageable.getPageCount() < getViewSize();
	}
	
	@Override
	protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, long pageIndex) {
		return new PagingNavigationLink<Void>(id, pageable, pageIndex).setAutoEnable(false);
	}
}
