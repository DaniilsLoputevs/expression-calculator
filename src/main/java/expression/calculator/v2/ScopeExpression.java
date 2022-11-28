package expression.calculator.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScopeExpression {
    String name;
    String expression; // (3*4-5+$epx2 + $epx3)
    List<ScopeExpression> executionDependsOnExpressions = new ArrayList<>(); // [$epx2, $epx3]
    BigDecimal executionResult;
    
    
    public ScopeExpression(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }
    
    public void applyExpressionResult(ScopeExpression expr) {
        executionDependsOnExpressions.remove(expr);
        this.expression = this.expression.replace(expr.name, expr.executionResult.toString());
    }
    
    @Override public String toString() {
        return expression + " --- " + name;
    }
}
