package igloo.wicket.offline;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Key holding rendered page/component class in {@link RequestCycle#getMetaData(MetaDataKey)} when we perform offline rendering
 * with igloo <code>AbstractOfflinePanelRendererServiceImpl</code> (wicket-more).
 */
public class OfflineComponentClassMetadataKey extends MetaDataKey<Class<? extends Component>> {

	private static final long serialVersionUID = 9166675647023302325L;

	public static final OfflineComponentClassMetadataKey INSTANCE = new OfflineComponentClassMetadataKey();

}
