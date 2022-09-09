package igloo.bootstrap.js.statement;

import java.io.Serializable;

import igloo.bootstrap.woption.IWVisitable;

public interface IJsStatement extends IWVisitable, Serializable {

	String render();

}
