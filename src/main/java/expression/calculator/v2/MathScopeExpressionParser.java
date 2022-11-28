package expression.calculator.v2;

import lombok.val;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/** "2+(3*4-5+(6+4)+(10-7*2))-5" */

// "2+(3*4-5+(6+4)+(10-7*2))-5"
// $root = 2+${exp_1}-5
// $exp1 = (3*4-5+${epx_2} + ${epx_3})
// $epx2 = (6+4)
// $epx3 = (10-7*2)
public class MathScopeExpressionParser {
    
    public static void main(String[] args) {
        val parser = new MathScopeExpressionParser();
        val rsl = parser.parse("2+(3*4-5+(6+4)+(10-7*2))-5");
        
        System.out.println();
        rsl.forEach((k, v) -> System.out.println(v.name + "  ---  " + v.expression));
        
        // 2+$exp1-5
        // *4-5+(6+4)+(10-7*2)
    }
    
    public LinkedHashMap<String, ScopeExpression> parse(String expression) {
        val rsl = new LinkedHashMap<String, ScopeExpression>();
        val stack = new ArrayList<ScopeExpression>();
        
        val rootExpContainer = new ScopeExpression("${exp_0}", expression);
        stack.add(rootExpContainer);
        rsl.put(rootExpContainer.name, rootExpContainer);
        
        /* if expression doesn't contains sub-expression(brackets) return root expression. */
        if (expression.indexOf('(') == -1) return rsl;
        
        for (int i = 0, expNamePostfix = 1; i != stack.size(); ) {
            val currentExpContainer = stack.get(i);
            val parseExp = currentExpContainer.expression;
            val openBracketIndex = parseExp.indexOf('(');
            val closeBracketIndex = indexOfBracketPair(parseExp, openBracketIndex);
            
            /* if this exp doesn't contains sub-exp move to next exp or break loop by increment index. */
            if (openBracketIndex == -1 && closeBracketIndex == -1) {
                i++;
                continue;
            }
            
            val subExpName = "${exp_" + expNamePostfix++ + '}';
            val subExpBody = parseExp.substring(openBracketIndex + 1, closeBracketIndex);
            val bracketSubExp = new ScopeExpression(subExpName, subExpBody);
            
            val currentExp = parseExp.substring(0, openBracketIndex) + subExpName + parseExp.substring(closeBracketIndex + 1);
            currentExpContainer.expression = currentExp;
            currentExpContainer.executionDependsOnExpressions.add(bracketSubExp);
            
            rsl.put(subExpName, bracketSubExp);
            stack.add(bracketSubExp);
            
            System.out.println("currentExp  =  " + currentExp);
            System.out.println("subExpName  =  " + subExpName);
            System.out.println("subExpBody  =  " + subExpBody);
            
            /* if current exp contains more bracket(s) - parse this exp again but new version. */
            if (currentExp.contains("(")) currentExpContainer.expression = currentExp;
            else i++;
        }
        
        return rsl;
    }
    public LinkedHashMap<String, ScopeExpression> parseV2(String expression) {
        val rsl = new LinkedHashMap<String, ScopeExpression>();
        val stack = new ArrayList<ScopeExpression>();
        
        val rootExpContainer = new ScopeExpression("${exp_0}", expression);
        stack.add(rootExpContainer);
        rsl.put(rootExpContainer.name, rootExpContainer);
        
        /* if expression doesn't contains sub-expression(brackets) return root expression. */
        if (expression.indexOf('(') == -1) return rsl;
        
        for (int i = 0, expNamePostfix = 1; i != stack.size(); ) {
            val currentExpContainer = stack.get(i);
            val parseExp = currentExpContainer.expression;
            val openBracketIndex = parseExp.indexOf('(');
            val closeBracketIndex = indexOfBracketPair(parseExp, openBracketIndex);
            
            /* if this exp doesn't contains sub-exp move to next exp or break loop by increment index. */
            if (openBracketIndex == -1 && closeBracketIndex == -1) {
                i++;
                continue;
            }
            
            val subExpName = "${exp_" + expNamePostfix++ + '}';
            val subExpBody = parseExp.substring(openBracketIndex + 1, closeBracketIndex);
            val bracketSubExp = new ScopeExpression(subExpName, subExpBody);
            
            val currentExp = parseExp.substring(0, openBracketIndex) + subExpName + parseExp.substring(closeBracketIndex + 1);
            currentExpContainer.expression = currentExp;
            currentExpContainer.executionDependsOnExpressions.add(bracketSubExp);
            
            rsl.put(subExpName, bracketSubExp);
            stack.add(bracketSubExp);
            
            System.out.println("currentExp  =  " + currentExp);
            System.out.println("subExpName  =  " + subExpName);
            System.out.println("subExpBody  =  " + subExpBody);
            
            /* if current exp contains more bracket(s) - parse this exp again but new version. */
            if (currentExp.contains("(")) currentExpContainer.expression = currentExp;
            else i++;
        }
        
        return rsl;
    }
    
    
    
    private int indexOfBracketPair(String source, int indexOpenBracketChar) {
        if (indexOpenBracketChar == -1) return -1;
        int level = 0;
        val chars = source.toCharArray();
        for (int i = indexOpenBracketChar + 1; i < chars.length; i++) {
            val c = chars[i];
            if (c == '(') level++;
            if (c == ')' && level == 0) return i;
            if (c == ')') level--;
        }
        return -1;
    }
    
}
