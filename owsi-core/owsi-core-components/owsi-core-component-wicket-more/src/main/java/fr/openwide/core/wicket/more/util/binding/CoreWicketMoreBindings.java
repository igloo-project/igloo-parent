package fr.openwide.core.wicket.more.util.binding;

import org.bindgen.java.util.ListBinding;

import fr.openwide.core.commons.util.mime.MediaTypeBinding;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationBinding;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColorBinding;
import fr.openwide.core.wicket.more.model.IBindableDataProviderBinding;

public final class CoreWicketMoreBindings {

	private static final EhCacheCacheInformationBinding EH_CACHE_CACHE_INFORMATION = new EhCacheCacheInformationBinding();

	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();
	
	private static final ListBinding<?> LIST = new ListBinding<Void>();
	
	private static final MediaTypeBinding MEDIA_TYPE = new MediaTypeBinding();
	
	private static final IBootstrapColorBinding I_BOOTSTRAP_COLOR = new IBootstrapColorBinding();
	
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
	
	public static IBootstrapColorBinding iBootstrapColor() {
		return I_BOOTSTRAP_COLOR;
	}
	
	private CoreWicketMoreBindings() {
	}

}
