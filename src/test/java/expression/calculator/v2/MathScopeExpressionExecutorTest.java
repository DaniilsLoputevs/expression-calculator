package expression.calculator.v2;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathScopeExpressionExecutorTest {
    private final MathScopeExpressionExecutor executor = new MathScopeExpressionExecutor();
    
    
    @Test public void low1() { test("7", "5+2"); }
    @Test public void low2() { test("6", "5+2-1"); }
    @Test public void low3() { test("9", "5+2-1+3"); }
    
    @Test public void middle1() { test("50", "5*10"); }
    @Test public void middle2() { test("25", "5*10/2"); }
    @Test public void middle3() { test("75", "5*10/2*3"); }
    
    @Test public void high1() { test("16", "2^4"); }
    @Test public void high2() { test("8", "2^4%50"); }
    @Test public void high3() { test("64", "2^4%50^2"); }
    
    @Test public void mixNaturalOrder() { test("14", "100%2^3*6/3+8-10"); }
    @Test public void mixReversedOrder() { test("10", "10-9+3/6*3^2%200"); }
    @Test public void mixMixedOrder() { test("158.333333333333333333333333333333333", "8*5^2+50/5%300-45"); }
    
    
    /**
     * BigDecimal
     * 1 - equals -- compare by: Value + Scale
     * ->> 8 == 8.0000 ->> false
     * 2 - compareTo -- compare by: Value
     * ->> 8 == 8.0000 ->> true
     */
    private void test(String expected, String expr) {
        val exp = new BigDecimal(expected);
        val rsl = executor.execute(expr);
        if (exp.compareTo(rsl) == 0) return; // return success
        assertEquals(exp, rsl); // return fail & show actual nums
    }
}