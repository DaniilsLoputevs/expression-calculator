package expression.calculator;

public interface ExpressionCalculator {
    String accept(String mathExpression);
    
    default String acceptV2(String mathExpression) { return "";}
}
