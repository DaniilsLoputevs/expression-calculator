package expression.calculator.v2;

import org.junit.jupiter.api.Test;

import static expression.calculator.v2.ExpressionValidator.ERR_V;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionValidatorTest {
    private final ExpressionValidator validator = new ExpressionValidator();
    
    private void test(String expected, String expr) {
        assertEquals(expected, validator.validate(expr));
    }
    
    
    @Test public void valid1() { test(ERR_V + "found brackets (open=4, close=3)", "2+(((1+3)+(4+4)+2)+6+7*2"); }
    
    @Test public void valid2() { test(ERR_V + "open-close bracket pair expected.", ")2+2("); }
    
    @Test public void valid3() { test(ERR_V + "open-close bracket pair expected.", ")(2+2)("); }
    
    @Test public void valid4() { test(ERR_V + "open-close bracket pair expected.", ")1+(2+2)+(3"); }
    
}