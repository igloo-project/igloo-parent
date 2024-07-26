package org.igloo.storage.integration;

import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;

public class WicketStorageSettings implements IWicketStorageSettings {

  private final String mountPath;
  private final String downloadMountPath;
  private final boolean supervisionPagesEnabled;

  /**
   * @param mountPath The path to use to mount resource; it is advised to include {@link
   *     CommonParameters#ID} parameter in your path.
   * @param downloadMountPath The path to use to download (Content-Disposition: Attachment)
   *     resource; it is advised to include {@link CommonParameters#ID} parameter in your path.
   */
  public WicketStorageSettings(
      String mountPath, String downloadMountPath, boolean supervisionPagesEnabled) {
    this.mountPath = mountPath;
    this.downloadMountPath = downloadMountPath;
    this.supervisionPagesEnabled = supervisionPagesEnabled;
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
}
