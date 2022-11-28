package expression.calculator.v2;

import expression.calculator.ExpressionCalculator;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import static expression.calculator.v2.ExpressionValidator.ERR_PREF;

@AllArgsConstructor
@Setter
public class ExpressionCalculatorV2 implements ExpressionCalculator {
    private MathScopeExpressionParser parser;
    private MathScopeExpressionExecutor executor;
    private ExpressionValidator validator;
    
    private LinkedHashMap<String, LinkedHashMap<String, ScopeExpression>> history = new LinkedHashMap<>();
    
    
    public ExpressionCalculatorV2() {
        this.parser = new MathScopeExpressionParser();
        this.executor = new MathScopeExpressionExecutor();
        this.validator = new ExpressionValidator();
    }
    
    @Override public String accept(String mathExpression) {
        mathExpression = validator.validate(clearEmptySpaces(mathExpression));
        if (mathExpression.startsWith(ERR_PREF)) return mathExpression;
        
        val parsed = parser.parse(mathExpression);
        val expressions = new ArrayList<>(parsed.values());
        Collections.reverse(expressions);
        
        for (val expr : expressions) {
            expr.executionResult = executor.execute(expr);
            applyExprResultForAllDependsOn(expr, expressions);
        }
        
        history.put(mathExpression, parsed);
        return parsed.get("${exp_0}").executionResult.toString();
    }
    
    @Override public String acceptV2(String mathExpression) {
        mathExpression = validator.validate(clearEmptySpaces(mathExpression));
        if (mathExpression.startsWith(ERR_PREF)) return mathExpression;
    
        val parsed = parser.parseV2(mathExpression);
        return ExpressionCalculator.super.acceptV2(mathExpression);
    }
    
    /**
     *  todo - збс! с Блэк-Джеком, Шлюхами, Казино и Map<String, List<ScopeExpression>>
     */
    private void applyExprResultForAllDependsOn(ScopeExpression executedExpr, Iterable<ScopeExpression> expressions) {
        expressions.forEach(it -> it.applyExpressionResult(executedExpr));
    }

    
    private String clearEmptySpaces(String expression) {
        return expression.replace(" ", "");
    }
}
