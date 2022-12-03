package expression.calculator;

import expression.calculator.v2.MathExpressionCalculatorV2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Operators priority: (highest = first, lowest = last)
 * * Brackets(Parentheses)
 * * Percent & Exponent
 * * Multiplication & Division
 * * Addition & Subtraction
 *
 *
 * todo - testcase - нету пары, есть Закрывающая, но нету Открывающей. ^^^^^^^^^^^^^^^^^^^^^
 * todo - testcase - делить на ноль нельзя - validation. ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * todo - testcase - делить на ноль нельзя - execution.
 *       System.out.println(new BigDecimal("100").divide(new BigDecimal("0")));
 *       >> java.lang.ArithmeticException: Division by zero
 * todo - testcase - Executor.findOperatorIndexesForExpr
 *
 * todo - збс! с Блэк-Джеком, Шлюхами, Казино и Map<String, List<ScopeExpression>>
 * todo - option - MathContext(precision, roundingMode)
 * todo - operators levels - make List and easy extendable
 * todo - remake Operator level to ExprExeInfo
 *
 * todo - validate - expr start&end with Operator char
 *
 * todo - ??? Functions
 *          FunctionDSL
 *          list expression by comma - for function with many arguments
 *
 *  ExpressionExecuteResult {
 *      String originalExpression;
 *      List<...> executeHistory;
 *      String result;
 *      Exception error;
 *
 *      // execution part
 *      String currentExpression;
 *      List<...> operatorLevels;
 *
 *  }
 **/
public class ExpressionCalculatorTest {
    private final ExpressionCalculator calculator = new MathExpressionCalculatorV2();
    
    private void test(String expected, String expr) {
        assertEquals(expected, calculator.accept(expr));
    }
    
    
    @Test public void smart1() { test("10", "2+(3*4-5+(6+4)+(10-7*2))-5"); }
    
    @Test public void brackets1() {
        test("10", "2+(((3*4-5+6)+4)+(10-7*2))-5");
    }
    
    @Test public void brackets2() {
        test("56", "2+( ( (1+3)+(4+4)+2 ) + (6+7) )*2");
    }
  
}