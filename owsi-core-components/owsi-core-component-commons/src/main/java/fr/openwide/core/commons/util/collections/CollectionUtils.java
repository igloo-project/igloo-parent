package fr.openwide.core.commons.util.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class CollectionUtils {
	
	/**
	 * Replaces all elements in <code>dst</code> by those in <code>src</code>.
	 * <p>This method is view-proof : it will work as expected even if <code>src</code> is a view of <code>dst</code>,
	 * i.e. if <code>src</code>'s content depends on <code>dst</code>'s (see for example
	 * {@link Collections#unmodifiableCollection(Collection)}, {@link Sets#union(java.util.Set, java.util.Set)}, ...).
	 * <p>In the process, this method may copy the elements of <code>src</code> in a new collection,
	 * hence it is not advisable to use it on large collections or in frequently called methods. 
	 * @param dst The collection whose content is to be replaced. Not null.
	 * @param src The collection whose content is to be copied to <code>dst</code>, or null to simply clear <code>dst</code>.
	 */
	public static <T> void replaceAll(Collection<T> dst, Collection<? extends T> src) {
		if (src == null) {
			dst.clear();
		} else {
			Collection<T> elements = Lists.newArrayList(src);
			dst.clear();
			dst.addAll(elements);
		}
	}
	
	/**
	 * Same as {@link CollectionUtils#replaceAll(Collection, Collection)}, but for {@link Map Maps}
	 */
	public static <K, V> void replaceAll(Map<K, V> dst, Map<? extends K, ? extends V> src) {
		if (src == null) {
			dst.clear();
		} else {
			Map<K, V> elements = Maps.newLinkedHashMap(src);
			dst.clear();
			dst.putAll(elements);
		}
	}
	
	private CollectionUtils() { }

}
