
import java.util.Stack;
import java.util.Vector;

public class Calculator implements MathSymbol {
    /**
     * 计算中缀表达式
     *
     * @param expression
     * @return
     */
    public double eval(String expression) {
        String str = infix2Suffix(expression);
        //System.out.println(" Infix Expression: " + expression);
        //System.out.println("Suffix Expression: " + str);
        if (str == null) {
            throw new IllegalArgumentException("Expression is null!");
        }
        //// String[] strs = str.split("//s+");
        String[] strs = str.split(" ");
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < strs.length; i++) {
            if (!Operator.isOperator(strs[i])) {
                stack.push(strs[i]);
            } else {
                Operator op = Operator.getInstance(strs[i]);
                double right = Double.parseDouble(stack.pop());
                double left = Double.parseDouble(stack.pop());
                double result = op.eval(left, right);
                stack.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(stack.pop());
    }

    /**
     * 将中缀表达式转换为后缀表达式
     *
     * @param expression
     * @return
     */
    public String infix2Suffix(String expression) {
//        if (expression == null) {
//            return null;
//        }
//        char[] chs = expression.toCharArray();
//        Stack<Character> stack = new Stack<Character>();
//        StringBuilder sb = new StringBuilder(chs.length);
//        boolean appendSeparator = false;
//        boolean sign = true;
//        for (int i = 0; i < chs.length; i++) {
//            char c = chs[i];
//            if (c == BLANK) {
//                continue;
//            }
//            // Next line is used output stack information.
//            // System.out.printf("%-20s %s%n", stack, sb.toString());
//            if (appendSeparator) {
//                sb.append(SEPARATOR);
//                appendSeparator = false;
//            }
//            if (isSign(c) && sign) {
//                sb.append(c);
//                continue;
//            }
//            if (isNumber(c)) {
//                sign = false;
//                sb.append(c);
//                continue;
//            }
//            if (isLeftBracket(c)) {
//                stack.push(c);
//                continue;
//            }
//            if (isRightBracket(c)) {
//                sign = false;
//                while (stack.peek() != LEFT_BRACKET) {
//                    sb.append(SEPARATOR);
//                    sb.append(stack.pop());
//                }
//                stack.pop();
//                continue;
//            }
//            appendSeparator = true;
//            if (Operator.isOperator(c)) {
//                sign = true;
//                if (stack.isEmpty() || stack.peek() == LEFT_BRACKET) {
//                    stack.push(c);
//                    continue;
//                }
//                int precedence = Operator.getPrority(c);
//                while (!stack.isEmpty() && Operator.getPrority(stack.peek()) >= precedence) {
//                    sb.append(SEPARATOR);
//                    sb.append(stack.pop());
//                }
//                stack.push(c);
//            }
//        }
//        while (!stack.isEmpty()) {
//            sb.append(SEPARATOR);
//            sb.append(stack.pop());
//        }
//        return sb.toString();

        /////张宏博 改
        if (expression == null) {
            return null;
        }


        char[] chs = expression.toCharArray();
        Stack<Character> stack = new Stack<Character>();
        StringBuilder sb = new StringBuilder(chs.length);
        boolean appendSeparator = false;
        boolean sign = true;
        for (int i = 0; i < chs.length; i++) {
            char c = chs[i];
            if (c == BLANK) {
                continue;
            }
            // Next line is used output stack information.
            // System.out.printf("%-20s %s%n", stack, sb.toString());
            if (appendSeparator) {
                sb.append(SEPARATOR);
                appendSeparator = false;
            }
            if (isSign(c) && sign) {
                sb.append(c);
                continue;
            }
            if (isNumber(c)) {
                sign = false;
                sb.append(c);
                continue;
            }
            if (isLeftBracket(c)) {
                stack.push(c);
                continue;
            }
            if (isRightBracket(c)) {
                sign = false;
                while (stack.peek() != LEFT_BRACKET) {
                    sb.append(SEPARATOR);
                    sb.append(stack.pop());
                }
                stack.pop();
                continue;
            }
            appendSeparator = true;
            if (Operator.isOperator(c)) {
                sign = true;
                if (stack.isEmpty() || stack.peek() == LEFT_BRACKET) {
                    stack.push(c);
                    continue;
                }
                int precedence = Operator.getPrority(c);
                while (!stack.isEmpty() && Operator.getPrority(stack.peek()) >= precedence) {
                    sb.append(SEPARATOR);
                    sb.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            sb.append(SEPARATOR);
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    /**
     * 判断某个字符是否是正号或者负号
     *
     * @param c
     * @return
     */
    private boolean isSign(char c) {
        if (c == NEGATIVE_SIGN || c == POSITIVE_SIGN) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个字符是否为数字或者小数点
     *
     * @param c
     * @return
     */
    private boolean isNumber(char c) {
        if ((c >= '0' && c <= '9') || c == DECIMAL_POINT) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个字符是否为左括号
     *
     * @param c
     * @return
     */
    private boolean isLeftBracket(char c) {
        return c == LEFT_BRACKET;
    }

    /**
     * 判断某个字符是否为右括号
     *
     * @param c
     * @return
     */
    private boolean isRightBracket(char c) {
        return c == RIGHT_BRACKET;
    }
}
