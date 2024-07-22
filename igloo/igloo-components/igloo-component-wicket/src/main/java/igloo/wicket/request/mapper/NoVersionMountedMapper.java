/*
 * Copyright 2010 by TalkingTrends (Amsterdam, The Netherlands)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://opensahara.com/licenses/apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package igloo.wicket.request.mapper;

import org.apache.wicket.core.request.handler.ListenerRequestHandler;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.iglooproject.functional.Supplier2;

/** Provides a mount strategy that drops the version number from stateful page urls. */
public class NoVersionMountedMapper extends MountedMapper {

  public NoVersionMountedMapper(
      String mountPath,
      Class<? extends IRequestablePage> pageClass,
      IPageParametersEncoder pageParametersEncoder) {
    super(mountPath, pageClass, pageParametersEncoder);
  }

  public NoVersionMountedMapper(String mountPath, Class<? extends IRequestablePage> pageClass) {
    super(mountPath, pageClass);
  }

  public NoVersionMountedMapper(
      String mountPath,
      Supplier2<Class<? extends IRequestablePage>> pageClassProvider,
      IPageParametersEncoder pageParametersEncoder) {
    super(mountPath, pageClassProvider, pageParametersEncoder);
  }

  public NoVersionMountedMapper(
      String mountPath, Supplier2<Class<? extends IRequestablePage>> pageClassProvider) {
    super(mountPath, pageClassProvider);
  }

  @Override
  protected void encodePageComponentInfo(Url url, PageComponentInfo info) {
    // do nothing so that component info does not get rendered in url
  }

  @Override
  public Url mapHandler(IRequestHandler requestHandler) {
    if (requestHandler instanceof ListenerRequestHandler) {
      return null;
    } else {
      return super.mapHandler(requestHandler);
    }
  }
}
