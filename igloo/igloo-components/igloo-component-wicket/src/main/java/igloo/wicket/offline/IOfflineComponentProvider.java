package igloo.wicket.offline;

import org.iglooproject.functional.SerializableSupplier2;

/**
 * Wrap component supplier and component class information for an offline wicket rendering, It can
 * be useful to know component type before object is constructed by the supplier (needed to provide
 * bootstrap version context based on Bootstrap interface markers, as bootstrap version needs to be
 * added to RequestCycle before object is created).
 */
public interface IOfflineComponentProvider<T> {

  public T getComponent();

  public Class<T> getComponentClass();

  public static <T> IOfflineComponentProvider<T> fromSupplier(
      final SerializableSupplier2<T> supplier, Class<T> componentClass) {
    return new IOfflineComponentProvider<T>() {

      @Override
      public T getComponent() {
        return supplier.get();
      }

      @Override
      public Class<T> getComponentClass() {
        return componentClass;
      }
    };
  }
}
