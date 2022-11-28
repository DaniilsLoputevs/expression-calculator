package expression.calculator.v2;

import lombok.val;

public class ExpressionValidator {
    public static final String
            ERR_PREF = "Error: ",
            ERR_V = ERR_PREF + "validation - ";
    
    public String validate(String expr) {
        expr = checkOpenCloseBrackets(expr);
        expr = checkDivideZero(expr);
        return expr;
    }
    
    
    private String checkOpenCloseBrackets(String expr) {
        int openCount = 0, closeCount = 0;
        boolean unOpenBracketFind = false;
        char lastBracketChar = 0;
        for (val c : expr.toCharArray()) {
            if (c == '(') {
                openCount++;
                lastBracketChar = c;
            }
            if (c == ')') {
                closeCount++;
                if (closeCount > openCount) {
                    unOpenBracketFind = true;
                    break;
                }
                lastBracketChar = c;
            }
        }
        return (unOpenBracketFind || lastBracketChar == '(') ? ERR_V + "open-close bracket pair expected."
                : (openCount != closeCount) ? ERR_V + "found brackets (open=" + openCount + ", close=" + closeCount + ')'
                : expr;
    }
    
    private String checkDivideZero(String expr) {
        return (!expr.contains("/0")) ? expr : ERR_V + "impossible divide by zero";
    }
}
