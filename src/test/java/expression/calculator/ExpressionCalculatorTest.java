package expression.calculator;

import expression.calculator.v2.ExpressionCalculatorV2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Operators priority: (1 - max priority)
 * 1 - () - brackets(Parentheses)
 * 2 - ^  - exponentiation
 * 3 - /* - Multiplication/Division
 * 4 - +- - Addition/Subtraction
 *
 * todo - rew
 * todo - збс! с Блэк-Джеком, Шлюхами, Казино и Map<String, List<ScopeExpression>>
 *
 * todo - testcase - нету пары, есть Закрывающая, но нету Открывающей. ^^^^^^^^^^^^^^^^^^^^^
 * todo - testcase - делить на ноль нельзя - validation. ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * todo - testcase - делить на ноль нельзя - execution.
 * System.out.println(new BigDecimal("100").divide(new BigDecimal("0")));
 * >> java.lang.ArithmeticException: Division by zero
 *
 * todo - testcase - Executor.findOperatorIndexesForExpr
 **/
public class ExpressionCalculatorTest  {
    private final ExpressionCalculator calculator = new ExpressionCalculatorV2();
    
    private void test(String expected, String expr) {
        assertEquals(expected, calculator.accept(expr));
    }
    
    
    @Test public void fivePlusTwo() { test("7", "5+2"); }
    
    @Test public void fivePlusTwoPlusTwo() { test("9", "5+2+2"); }
    
    @Test public void fivePlusTwoMinusTwo() { test("5", "5+2-2"); }
    
    @Test public void fivePlusTwoMultipleFive() { test("17", "5+2*6"); }
    
    @Test public void fivePlusTwoMultipleSixDivideThree() { test("9", "5+2*6/3"); }
    
    @Test public void smart1() { test("38", "2+(3*4-5+(6+4)+(10-7*2))-5"); }
    
    @Test public void brackets1() {
        test("38", "2+(((3*4-5+6)+4)+(10-7*2))-5");
    }
    
    @Test public void brackets2() {
        test("56", "2+( ( (1+3)+(4+4)+2 ) + (6+7) )*2");
    }
    
    @Test
    public void bracketsMiss() { test("Error: validation - found brackets (open=4, close=3)", "2+( ( (1+3)+(4+4)+2 ) + 6+7*2"); }
    
    @Test public void bracketPairMiss1() { test("Error: validation - open-close bracket pair expected.", ")2+2("); }
    
    @Test public void bracketPairMiss2() { test("Error: validation - open-close bracket pair expected.", ")(2+2)("); }
    
    @Test
    public void bracketPairMiss3() { test("Error: validation - open-close bracket pair expected.", ")1+(2+2)+(3"); }
}