package expression.calculator.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static expression.calculator.v2.ExpressionValidator.ERR_PREF;
import static expression.calculator.v2.MathScopeExpressionExecutor.MathOperator.*;

// "10-5+3*10/2"
// "10-5+3*5"
// "10-5+15"
// "5+15"
public class MathScopeExpressionExecutor {
    public static void main(String[] args) {
        val src = "2/2-5+3*10/4-2";
//        val src = "-4+7.5-2";
//        val src = "3.5-2";
        System.out.println("src = " + src);
        val executor = new MathScopeExpressionExecutor();
        executor.execute(new ScopeExpression("${expr_00}", src));
    }
    
    public BigDecimal execute(ScopeExpression expression) {
        val exprInfo = ExprExeInfo.of(expression.expression);
        
        // if expression doesn't contains operator(suddenly) return expression as number.
        if (exprInfo.getAllLevelsOperatorCount() == 0) return new BigDecimal(exprInfo.expressionCurrent);
        
        executeAllOperatorsOnLevelV2(exprInfo, ExecutionOperatorLevel.HIGH);
        executeAllOperatorsOnLevelV2(exprInfo, ExecutionOperatorLevel.MIDDLE);
        executeAllOperatorsOnLevelV2(exprInfo, ExecutionOperatorLevel.LOW);
        
        System.out.println("exprInfo.expressionOriginal = " + exprInfo.expressionOriginal);
        System.out.println("exprInfo.expressionCurrent  = " + exprInfo.expressionCurrent);
        return new BigDecimal(exprInfo.expressionCurrent);
    }
    
    private void executeAllOperatorsOnLevelV2(ExprExeInfo exprInfo, ExecutionOperatorLevel level) {
        while (level.operatorCounterGetter.apply(exprInfo) != 0) {
            val currExpr = exprInfo.expressionCurrent;
            
            val indexes = findOperatorIndexesForExpr(currExpr, level.operators);
            val prevNumStartIndex = (indexes.prevOperatorIndex == 0) ? 0 : indexes.prevOperatorIndex + 1;
            
            val leftNum = Numbers.parseNumber(currExpr, prevNumStartIndex, indexes.currOperatorIndex);
            val rightNum = Numbers.parseNumber(currExpr, indexes.currOperatorIndex + 1, indexes.nextOperatorIndex);
            val operator = MathOperator.of(currExpr.charAt(indexes.currOperatorIndex));
            val rsl = operator.apply(leftNum, rightNum);
            
            level.operatorCounterDecrementFunc.accept(exprInfo);
            // escape case: prev == 1 -- it's means prevOperator is Minus for Negative number.
            exprInfo.expressionCurrent =
                    currExpr.substring(0, prevNumStartIndex) + rsl + currExpr.substring(indexes.nextOperatorIndex);
            
            exprInfo.executionHistory.add(new ExpressionExecuteStep(
                    currExpr, exprInfo.expressionCurrent, operator, leftNum, rightNum, rsl));
            
            System.out.println("currExpr = " + currExpr);
            System.out.println("leftNum  = " + leftNum);
            System.out.println("rightNum = " + rightNum);
            System.out.println("exprInfo.expressionCurrent =  " + exprInfo.expressionCurrent);
            
        }
    }
    
    // todo tests for many cases
    private OperatorIndexes findOperatorIndexesForExpr(String expr, Set<MathScopeExpressionExecutor.MathOperator> operators) {
        int prev = 0;
        int curr = -1;
        int next = expr.length();
        for (int i = 0; i < expr.length(); i++) {
            val c = expr.charAt(i);
            
            val currCharOperator = MathScopeExpressionExecutor.MathOperator.of(c);
            if (currCharOperator == null) continue;
            
            char prevChar = (i == 0) ? 0 : expr.charAt(i - 1);
            // skip Minus operator char after other operator - case: negative number
            if (currCharOperator == MINUS
                    && (MathScopeExpressionExecutor.MathOperator.isOperatorChar(prevChar) || prevChar == 0))
                continue;
            
            if (curr == -1 && operators.contains(currCharOperator)) {
                curr = i;
                continue;
            }
            // case: searching for prevOperatorIndex
            if (curr == -1) {
                prev = i;
            } else {
                next = i;
                break;
            }
        }
        return new OperatorIndexes(prev, curr, next);
    }
    
    
    private static class Numbers {
        private static BigDecimal parseNumber(String expr, int startIndex, int endIndex) {
            return new BigDecimal(expr.substring(startIndex, endIndex));
        }

        // todo - for remove?
//        private static boolean isNumberChar(char c) {
//            return NUMBERS_CHARS.contains(c);
//        }

//        private static final Set<Character> NUMBERS_CHARS = "0123456789."
//                .chars().mapToObj(c -> (char) c)
//                .collect(Collectors.toUnmodifiableSet());
    }
    
    @Getter
    private static class ExprExeInfo {
        private final String expressionOriginal;
        private String expressionCurrent;
        private final List<ExpressionExecuteStep> executionHistory = new ArrayList<>();
        
        private int highestLevelOperatorsCount = 0;
        private int middleLevelOperatorsCount = 0;
        private int lowLevelOperatorsCount = 0;
        
        private ExprExeInfo(String expression) {
            this.expressionOriginal = expression;
        }
        
        public int decrementHighestLevelOperatorsCount() {return this.highestLevelOperatorsCount--;}
        
        public int decrementMiddleLevelOperatorsCount() {return this.middleLevelOperatorsCount--;}
        
        public int decrementLowLevelOperatorsCount() {return this.lowLevelOperatorsCount--;}
        
        public int getAllLevelsOperatorCount() { return middleLevelOperatorsCount + lowLevelOperatorsCount + highestLevelOperatorsCount;}
        
        public static ExprExeInfo of(String exp) {
            val rsl = new ExprExeInfo(exp);
            rsl.expressionCurrent = exp;
            
            boolean isPrevCharOperator = false;
            for (int i = 0; i < exp.length(); i++) {
                char c = exp.charAt(i);
                val operator = MathOperator.of(c);
                if (operator == null) {
                    isPrevCharOperator = false;
                    continue;
                }
                if (operator == EXPONENT || operator == PERCENT) rsl.highestLevelOperatorsCount++;
                if (operator == MULTIPLICATION || operator == DIVISION) rsl.middleLevelOperatorsCount++;
                if (operator == PLUS || (operator == MINUS && !isPrevCharOperator)) rsl.lowLevelOperatorsCount++;
                isPrevCharOperator = true;
            }
            return rsl;
        }
    }
    
    
    @RequiredArgsConstructor
    private enum ExecutionOperatorLevel {
        
        HIGH(Set.of(EXPONENT, PERCENT), ExprExeInfo::getHighestLevelOperatorsCount, ExprExeInfo::decrementHighestLevelOperatorsCount),
        MIDDLE(Set.of(MULTIPLICATION, DIVISION), ExprExeInfo::getMiddleLevelOperatorsCount, ExprExeInfo::decrementMiddleLevelOperatorsCount),
        LOW(Set.of(PLUS, MINUS), ExprExeInfo::getLowLevelOperatorsCount, ExprExeInfo::decrementLowLevelOperatorsCount);
        
        private final Set<MathOperator> operators;
        private final Function<ExprExeInfo, Integer> operatorCounterGetter;
        private final Consumer<ExprExeInfo> operatorCounterDecrementFunc;
    }
    
    /**
     * Operators priority: (1 - max priority)
     * 2 - ^  - exponent
     * 3 - /* - Multiplication/Division
     * 4 - +- - Addition/Subtraction
     */
    @RequiredArgsConstructor
    @Getter
    enum MathOperator {
        EXPONENT('^', MathOperator::exponent),
        PERCENT('%', MathOperator::percent),
        
        MULTIPLICATION('*', BigDecimal::multiply),
        DIVISION('/', BigDecimal::divide),
        
        PLUS('+', BigDecimal::add),
        MINUS('-', BigDecimal::subtract),
        ;
        
        
        private final Character operatorChar;
        private final BinaryOperator<BigDecimal> func;
        
        
        private static final Map<Character, MathOperator> operators =
                Arrays.stream(MathOperator.values())
                        .collect(Collectors.toMap(MathOperator::getOperatorChar, it -> it));
        
        private static final Set<Character> operatorChars = operators.keySet();
        
        private static final BigDecimal HUNDRED = new BigDecimal("100");
        
        
        public static BigDecimal percent(BigDecimal base, BigDecimal percent) {
            return base.divide(HUNDRED).multiply(percent);
        }
        
        public static BigDecimal exponent(BigDecimal base, BigDecimal power) {
            if (power.toString().contains("."))
                throw new ArithmeticException(ERR_PREF + "power must integer, not decimal. power = " + power);
            return base.pow(power.toBigInteger().intValue());
        }
        
        
        public static boolean isOperatorChar(char c) {
            return operatorChars.contains(c);
        }
        
        public static MathOperator of(char c) {
            return operators.get(c);
        }
        
        public BigDecimal apply(BigDecimal left, BigDecimal right) {
            return this.func.apply(left, right);
        }
    }
    
    @ToString public record ExpressionExecuteStep(
            String expressionBefore, String expressionAfter,
            MathOperator exeOperator,
            BigDecimal exeLeftNumber, BigDecimal exeRightNumber,
            BigDecimal exeResult) {}
    
    @ToString public record OperatorIndexes(int prevOperatorIndex, int currOperatorIndex, int nextOperatorIndex) {}
}
