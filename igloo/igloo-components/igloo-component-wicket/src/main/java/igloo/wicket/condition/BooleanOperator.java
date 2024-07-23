package igloo.wicket.condition;

public enum BooleanOperator {
  AND {
    @Override
    protected boolean isDecisiveOperand(boolean operand) {
      return operand == false;
    }

    @Override
    protected boolean getResultWhenDecisiveOperandMet() {
      return false;
    }
  },
  OR {
    @Override
    protected boolean isDecisiveOperand(boolean operand) {
      return operand == true;
    }

    @Override
    protected boolean getResultWhenDecisiveOperandMet() {
      return true;
    }
  },
  NOR {
    @Override
    protected boolean isDecisiveOperand(boolean operand) {
      return operand == true;
    }

    @Override
    protected boolean getResultWhenDecisiveOperandMet() {
      return false;
    }
  },
  NAND {
    @Override
    protected boolean isDecisiveOperand(boolean operand) {
      return operand == false;
    }

    @Override
    protected boolean getResultWhenDecisiveOperandMet() {
      return true;
    }
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
