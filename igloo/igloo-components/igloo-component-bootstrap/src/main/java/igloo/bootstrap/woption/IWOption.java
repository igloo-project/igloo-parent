package igloo.bootstrap.woption;

import java.io.Serializable;

public interface IWOption<T> extends IWVisitable, Serializable {

  T option();
}
