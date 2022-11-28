package expression.calculator.v2;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

import static expression.calculator.v2.MathScopeExpressionExecutor.MathOperator.*;

public class Exp {
    
    static class DTO {
        @Getter @Setter private String name, id, uuid, huid;
    }
    

    public static void main(String[] args) {
//        val dto = new DTO();
        
        
        
        System.out.println(new BigDecimal("100").divide(new BigDecimal("0")));
        
        
        // i =         4 6  9
        var src = "10+1+3*10--4/4-2";
        var rsl = findOperatorIndexesForExpr(src, Set.of(MULTIPLICATION));
        System.out.println(rsl);
        //         4 6  9
        src = "10+1+3--4/4-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(MINUS));
        System.out.println(rsl);
        //         4  7  10
        src = "10+1+-3*-4/4-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(MULTIPLICATION));
        System.out.println(rsl);
        //         4  7  10
        src = "10+1+-3*-4/4-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(MULTIPLICATION, DIVISION));
        System.out.println(rsl);
        //    0 1 3
        src = "2/2-5+3*10/4-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(MULTIPLICATION, DIVISION));
        System.out.println(rsl);
        //   0   2   6
        src = "-4+7.5-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(PLUS, MINUS));
        System.out.println(rsl);
        //   0    3    4
        src = "3.5-2";
        rsl = findOperatorIndexesForExpr(src, Set.of(PLUS, MINUS));
        System.out.println(rsl);
    }
    
    private static OperatorIndexes findOperatorIndexesForExpr(String expr, Set<MathScopeExpressionExecutor.MathOperator> operators) {
        int prev = 0;
        int curr = -1;
        int next = expr.length() - 1;
        for (int i = 0; i < expr.length(); i++) {
            val c = expr.charAt(i);
            
            val currCharOperator = MathScopeExpressionExecutor.MathOperator.of(c);
            if (currCharOperator == null) continue;
            
            char prevChar = (i == 0) ? 0 : expr.charAt(i - 1);
            // skip Minus operator char after other operator - case: negative number
            if (currCharOperator == MINUS && (MathScopeExpressionExecutor.MathOperator.isOperatorChar(prevChar) || prevChar == 0) ) continue;
            
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
    
    
    @ToString
    @RequiredArgsConstructor
    static class OperatorIndexes {
        /** If not found, default = 0 */
        private final int prevOperatorIndex;
        private final int currOperatorIndex;
        /** If not found, default = last index for expression */
        private final int nextOperatorIndex;
    }
    
    static class MyMapKey {
        private int id;
        private String someData;
        private final int createId = staticCreateId++;
        
        private static int staticCreateId = 0;
        
    
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
//            MyMapKey myMapKey = (MyMapKey) o;
//            return id == myMapKey.id && createId == myMapKey.createId && Objects.equals(someData, myMapKey.someData);
            return this.createId == ((MyMapKey) o).createId;
        }
    
        @Override
        public int hashCode() {
            return createId;
        }
    }
}
