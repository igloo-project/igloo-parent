package org.iglooproject.wicket.more.util.model;

import com.google.common.base.Throwables;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.threadsafe.impl.AbstractThreadSafeLoadableDetachableModel;

/**
 * See {@link GenericEntityModel} or {@link AbstractThreadSafeLoadableDetachableModel} for examples
 * of use.
 */
public final class LoadableDetachableModelExtendedDebugInformation {

  private StackTraceElement[] latestAttachStackTraceForDebug;

  public LoadableDetachableModelExtendedDebugInformation() {
    super();
  }

  public void onAttach() {
    this.latestAttachStackTraceForDebug = Thread.currentThread().getStackTrace();
  }

  public void onDetach() {
    this.latestAttachStackTraceForDebug = null;
  }

  public Object getLatestAttachInformation() {
    if (latestAttachStackTraceForDebug != null) {
      Exception e = new Exception();
      e.setStackTrace(latestAttachStackTraceForDebug);
      return Throwables.getStackTraceAsString(e);
    } else {
      return null;
    }
  }
}
