package fr.openwide.core.wicket.more.condition;

public enum BooleanOperator {
	/** AND(*) */
	WHEN_ALL_TRUE {
		@Override protected boolean isDecisiveOperand(boolean operand) { return operand == false; }
		@Override protected boolean getResultWhenDecisiveOperandMet() { return false; }
	},
	/** OR(*) */
	WHEN_ANY_TRUE {
		@Override protected boolean isDecisiveOperand(boolean operand) { return operand == true; }
		@Override protected boolean getResultWhenDecisiveOperandMet() { return true; }
	},
	/** NOT(OR(*)) */
	WHEN_ALL_FALSE {
		@Override protected boolean isDecisiveOperand(boolean operand) { return operand == true; }
		@Override protected boolean getResultWhenDecisiveOperandMet() { return false; }
	},
	/** NOT(AND(*)) */
	WHEN_ANY_FALSE {
		@Override protected boolean isDecisiveOperand(boolean operand) { return operand == false; }
		@Override protected boolean getResultWhenDecisiveOperandMet() { return true; }
	};
	
	protected abstract boolean isDecisiveOperand(boolean operand);
	protected abstract boolean getResultWhenDecisiveOperandMet();
	
	public boolean apply(Iterable<? extends Condition> conditions) {
		for (Condition condition : conditions) {
			if (isDecisiveOperand(condition.applies())) {
				return getResultWhenDecisiveOperandMet();
			}
		}
		return !getResultWhenDecisiveOperandMet();
	}
}