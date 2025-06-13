package org.igloo.storage.integration;

import org.apache.wicket.request.component.IRequestablePage;

public interface IWicketStorageSettings {

  String getMountPath();

  String getDownloadMountPath();

  boolean isSupervisionPagesEnabled();

  Class<? extends IRequestablePage> getUnavailablePageClass();
}
