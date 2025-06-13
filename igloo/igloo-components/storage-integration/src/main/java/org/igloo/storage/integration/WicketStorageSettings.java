package org.igloo.storage.integration;

import org.apache.wicket.request.component.IRequestablePage;
import org.igloo.storage.model.atomic.FichierStatus;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;

public class WicketStorageSettings implements IWicketStorageSettings {

  private final String mountPath;
  private final String downloadMountPath;
  private final boolean supervisionPagesEnabled;

  /**
   * A custom {@link FichierStatus#UNAVAILABLE} error page. This page may use a custom status code
   * to disable Apache HTTPD ErrorDocument-like handling.
   */
  private final Class<? extends IRequestablePage> unavailablePageClass;

  /**
   * @param mountPath The path to use to mount resource; it is advised to include {@link
   *     CommonParameters#ID} parameter in your path.
   * @param downloadMountPath The path to use to download (Content-Disposition: Attachment)
   *     resource; it is advised to include {@link CommonParameters#ID} parameter in your path.
   * @param supervisionPagesEnabled Enable OK/KO wicket monitoring pages.
   * @param unavailablePageClass provide a custom unavailable error page.
   */
  public WicketStorageSettings(
      String mountPath,
      String downloadMountPath,
      boolean supervisionPagesEnabled,
      Class<? extends IRequestablePage> unavailablePageClass) {
    this.mountPath = mountPath;
    this.downloadMountPath = downloadMountPath;
    this.supervisionPagesEnabled = supervisionPagesEnabled;
    this.unavailablePageClass = unavailablePageClass;
  }

  public WicketStorageSettings(
      String mountPath, String downloadMountPath, boolean supervisionPagesEnabled) {
    this(mountPath, downloadMountPath, supervisionPagesEnabled, null);
  }

  @Override
  public String getMountPath() {
    return mountPath;
  }

  @Override
  public String getDownloadMountPath() {
    return downloadMountPath;
  }

  @Override
  public boolean isSupervisionPagesEnabled() {
    return supervisionPagesEnabled;
  }

  @Override
  public Class<? extends IRequestablePage> getUnavailablePageClass() {
    return unavailablePageClass;
  }
}
