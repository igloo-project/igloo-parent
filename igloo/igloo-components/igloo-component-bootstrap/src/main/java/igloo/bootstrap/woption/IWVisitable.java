package igloo.bootstrap.woption;

public interface IWVisitable {

	default void accept(IWVisitor visitor) {
		// implemention may propagation visitor call
	}

}