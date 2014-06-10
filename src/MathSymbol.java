/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-9
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 */
public interface MathSymbol {
    /**
     * 左括号
     */
    public final static char LEFT_BRACKET = '(';

    /**
     * 右括号
     */
    public final static char RIGHT_BRACKET = ')';

    /**
     * 中缀表达式中的空格，需要要忽略
     */
    public final static char BLANK = ' ';

    /**
     * 小数点符号
     */
    public final static char DECIMAL_POINT = '.';

    /**
     * 负号
     */
    public final static char NEGATIVE_SIGN = '-';

    /**
     * 正号
     */
    public final static char POSITIVE_SIGN = '+';

    /**
     * 后缀表达式的各段的分隔符
     */
    public final static char SEPARATOR = ' ';
}
