package fr.openwide.core.test.infinispan.util.action;

import org.infinispan.remoting.transport.Address;

import fr.openwide.core.infinispan.model.impl.SimpleAction;

public class QuestionAction extends SimpleAction<String> {

	private final String question;

	public QuestionAction(Address target, String question) {
		super(target);
		this.question = question;
	}

	@Override
	public String call() throws Exception {
		if ("What's your name ?".equals(question)) {
			return "John Doe";
		} else if ("How old are you ?".equals(question)) {
			return "35";
		} else {
			return "...";
		}
	}

	public static final QuestionAction ask(Address target, String question) {
		return new QuestionAction(target, question);
	}

}
