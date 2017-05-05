package fr.openwide.core.wicket.more.util.binding;

import org.bindgen.java.util.ListBinding;

import fr.openwide.core.commons.util.mime.MediaTypeBinding;
import fr.openwide.core.infinispan.model.IAttributionBinding;
import fr.openwide.core.infinispan.model.ILockAttributionBinding;
import fr.openwide.core.infinispan.model.ILockBinding;
import fr.openwide.core.infinispan.model.INodeBinding;
import fr.openwide.core.infinispan.model.IRoleAttributionBinding;
import fr.openwide.core.infinispan.model.IRoleBinding;
import fr.openwide.core.jpa.more.infinispan.model.TaskQueueStatusBinding;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationBinding;
import fr.openwide.core.wicket.more.model.IBindableDataProviderBinding;

public final class CoreWicketMoreBindings {

	private static final EhCacheCacheInformationBinding EH_CACHE_CACHE_INFORMATION = new EhCacheCacheInformationBinding();

	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();

	private static final ListBinding<?> LIST = new ListBinding<Void>();

	private static final MediaTypeBinding MEDIA_TYPE = new MediaTypeBinding();

	private static final INodeBinding I_NODE = new INodeBinding();

	private static final ILockBinding I_LOCK = new ILockBinding();
	private static final ILockAttributionBinding I_LOCK_ATTRIBUTION = new ILockAttributionBinding();

	private static final IRoleBinding I_ROLE = new IRoleBinding();
	private static final IRoleAttributionBinding I_ROLE_ATTRIBUTION = new IRoleAttributionBinding();

	private static final IAttributionBinding I_ATTRIBUTION = new IAttributionBinding();

	private static final TaskQueueStatusBinding TASK_QUEUE_STATUS = new TaskQueueStatusBinding(); 

	public static EhCacheCacheInformationBinding ehCacheCacheInformation() {
		return EH_CACHE_CACHE_INFORMATION;
	}

	public static IBindableDataProviderBinding iBindableDataProvider() {
		return IBINDABLE_DATA_PROVIDER;
	}

	public static ListBinding<?> list() {
		return LIST;
	}

	public static MediaTypeBinding mediaType() {
		return MEDIA_TYPE;
	}

	public static INodeBinding iNode() {
		return I_NODE;
	}

	public static ILockBinding iLock() {
		return I_LOCK;
	}

	public static ILockAttributionBinding iLockAttribution() {
		return I_LOCK_ATTRIBUTION;
	}

	public static IRoleBinding iRole() {
		return I_ROLE;
	}

	public static IRoleAttributionBinding iRoleAttribution() {
		return I_ROLE_ATTRIBUTION;
	}

	public static IAttributionBinding iAttribution(){
		return I_ATTRIBUTION;
	}

	public static TaskQueueStatusBinding taskQueueStatus(){
		return TASK_QUEUE_STATUS;
	}

	private CoreWicketMoreBindings() {
	}

}
