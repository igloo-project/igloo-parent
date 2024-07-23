package igloo.bootstrap.js.statement;

import igloo.bootstrap.woption.IWVisitable;
import java.io.Serializable;

public interface IJsStatement extends IWVisitable, Serializable {

  String render();
}
