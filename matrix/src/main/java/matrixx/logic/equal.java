package matrixx.logic;

import java.util.ArrayList;
import java.util.List;

// (( ده مثال ازاي هتستدعي الفانكشن يتاعت التساوي ));
// =========================================================
//         double result = evaluateExpression(expression);
// =========================================================
public class equal {

    public static final double E = Math.E; // قيمة e

    public static double evaluateExpression(String expr) throws Exception {
        if (expr.trim().isEmpty()) {

            throw new Exception("Empty expression");
        }
        expr = expr.replace("e", String.valueOf(Math.E));

        return parseExpressionWithParentheses(expr);
    }

    // معالجة التعبيرات مع الأقواس
    public static double parseExpressionWithParentheses(String expr) throws Exception {
        // Check for implicit multiplication (e.g., "5(9)")
        for (int i = 0; i < expr.length() - 1; i++) {
            if (Character.isDigit(expr.charAt(i)) && expr.charAt(i + 1) == '(') {

                throw new Exception("Implicit multiplication detected. Use * explicitly, like '5*(9)'.");
            }
        }

        while (expr.contains("(")) {
            int openIndex = expr.lastIndexOf("(");
            int closeIndex = expr.indexOf(")", openIndex);
            if (closeIndex == -1) {

                throw new Exception("Mismatched parentheses");
            }

            // Extract and evaluate the expression inside the parentheses
            String innerExpr = expr.substring(openIndex + 1, closeIndex);
            double innerResult = parseAdditionSubtraction(innerExpr);

            // Replace the evaluated expression back in the original string
            expr = expr.substring(0, openIndex) + innerResult + expr.substring(closeIndex + 1);
        }
        return parseAdditionSubtraction(expr); // Start with the highest operator level after parentheses
    }

    // Parse addition and subtraction
    public static double parseAdditionSubtraction(String expr) throws Exception {
        List<String> terms = splitExpression(expr, "+", "-");
        double result = parseMultiplicationDivision(terms.get(0));
        for (int i = 1; i < terms.size(); i += 2) {
            String operator = terms.get(i);
            double nextValue = parseMultiplicationDivision(terms.get(i + 1));
            if (operator.equals("+")) {
                result += nextValue;
            } else if (operator.equals("-")) {
                result -= nextValue;
            }
        }

        return result;
    }

    // Parse multiplication, division, and modulus
    public static double parseMultiplicationDivision(String expr) throws Exception {
        List<String> factors = splitExpression(expr, "*", "/", "%");
        double result = parsePower(factors.get(0));
        for (int i = 1; i < factors.size(); i += 2) {
            String operator = factors.get(i);
            double nextValue = parsePower(factors.get(i + 1));
            if (operator.equals("*")) {
                result *= nextValue;
            } else if (operator.equals("/")) {
                if (nextValue == 0) {

                    throw new ArithmeticException("Division by zero");
                }
                result /= nextValue;
            } else if (operator.equals("%")) {
                result %= nextValue;
            }
        }

        return result;
    }

    // Parse power and factorial
    public static double parsePower(String expr) throws Exception {
        expr = expr.trim();

        // Check for n√ notation
        if (expr.contains("√")) {
            String[] parts = expr.split("√");
            if (parts.length != 2) {

                throw new Exception("Invalid nth-root expression format");
            }
            double n = parseFactorial(parts[0].isEmpty() ? "2" : parts[0]); // Default to square root if no n specified
            double base = parseFactorial(parts[1]);
            return nthRoot(base, n);
        }

        // Handle exponentiation
        List<String> parts = splitExpression(expr, "^");
        double base = parseFactorial(parts.get(0)); // First part is the base
        for (int i = 1; i < parts.size(); i += 2) {
            String operator = parts.get(i); // Get the operator
            String exponentExpr = parts.get(i + 1); // Get the exponent expression
            double exponent = evaluateExpression(exponentExpr); // Evaluate the exponent expression
            if (operator.equals("-")) { // Check for negative exponent
                exponent = -exponent; // Negate the exponent
            }
            base = Math.pow(base, exponent); // Calculate base raised to the exponent
        }

        return base;
    }

    public static double nthRoot(double base, double n) {
        if (base < 0 && n % 2 == 0) {

            throw new ArithmeticException("Even root of a negative number is undefined in the real number domain.");
        }

        return Math.pow(base, 1.0 / n);
    }

    // Factorial calculation
    public static double factorial(double n) {
        if (n < 0 || n != Math.floor(n)) { // Check for negative or non-integer values
            System.out.println("Factorial is only defined for non-negative integers.");
            return Double.NaN; // Return NaN for invalid input
        }

        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return result;
    }

    // Parse factorial with higher priority than addition/subtraction
    public static double parseFactorial(String expr) throws Exception {
        expr = expr.trim();
        if (expr.endsWith("!")) {
            double value = parseUnary(expr.substring(0, expr.length() - 1));
            return factorial(value);
        }

        return parseUnary(expr);
    }

    public static double parseUnary(String var0) throws Exception {
        var0 = var0.trim();

        if (var0.equals("=")) { // إضافة قيمة e عند الضغط على الزر "="
            return E;
        } else if (var0.startsWith("log(")) {
            double var1 = parseExpressionWithParentheses(var0.substring(4));
            return Math.log10(var1);
        } else if (var0.startsWith("ln (")) {
            double var1 = parseExpressionWithParentheses(var0.substring(4));
            return Math.log(var1);
        } else if (var0.startsWith("sin(")) {
            double var1 = parseExpressionWithParentheses(var0.substring(4));
            return Math.sin(Math.toRadians(var1));
        } else if (var0.startsWith("cos(")) {
            double var1 = parseExpressionWithParentheses(var0.substring(4));
            return Math.cos(Math.toRadians(var1));
        } else if (var0.startsWith("tan(")) {
            double var1 = parseExpressionWithParentheses(var0.substring(4));
            return Math.tan(Math.toRadians(var1));
        } else if (var0.startsWith("sin") || var0.startsWith("cos") || var0.startsWith("tan") || var0.startsWith("log") || var0.startsWith("ln ")) {
            // Handle cases without parentheses
            String functionName = var0.substring(0, 3); // "sin", "cos", "tan", "log" or "ln"
            String argument = var0.substring(3).trim();

            // Expecting a number immediately after the function name
            if (argument.isEmpty()) {

                throw new Exception("Invalid expression: missing argument for " + functionName);
            }

            // Try to parse the argument as a number
            double var1 = Double.parseDouble(argument);

            // Call the respective function
            switch (functionName) {
                case "sin":
                    return Math.sin(Math.toRadians(var1));
                case "cos":
                    return Math.cos(Math.toRadians(var1));
                case "tan":
                    return Math.tan(Math.toRadians(var1));
                case "log":
                    return Math.log10(var1); // Assuming log10 for base 10
                case "ln ":
                    return Math.log(var1); // Natural log
            }
        } else if (var0.startsWith("\u221a")) { // Square root
            double var1 = parsePrimary(var0.substring(1));
            if (var1 < 0.0) {

                throw new ArithmeticException("Square root of negative number");
            }

            return Math.sqrt(var1);
        } else {

            return parsePrimary(var0); // Handle as a regular number or expression
        }

        throw new Exception("Invalid expression: " + var0);
    }

    // Parse numbers and handle parentheses
    public static double parsePrimary(String var0) throws Exception {
        var0 = var0.trim(); // Trim spaces

        // Print the string being parsed for debugging
        System.out.println("Parsing primary: '" + var0 + "'");

        // Check if the string starts with '(' and ends with ')'
        if (var0.startsWith("(") && var0.endsWith(")")) {

            return evaluateExpression(var0.substring(1, var0.length() - 1)); // Recursive evaluation
        }

        // Attempt to parse the number
        try {
            return Double.parseDouble(var0); // Attempt to parse as a double
        } catch (NumberFormatException e) {

            throw new Exception("Invalid number format for: '" + var0 + "'"); // Enhanced error message
        }
    }

    public static List<String> splitExpression(String expr, String... operators) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (isOperator(c, operators) && current.length() > 0) {
                result.add(current.toString());
                result.add(Character.toString(c));
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            result.add(current.toString());
        }

        return result;
    }

    // Helper method to check if a character is an operator
    public static boolean isOperator(char c, String... operators) {
        for (String operator : operators) {
            if (operator.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    public static double sine(double angle) {
        double rad = angle * (Math.PI / 180); // change angle to radian
        double sin = 0;
        for (int i = 0; i < 10; i++) { // number of edges in series
            sin += (Math.pow(-1, i) * Math.pow(rad, 2 * i + 1)) / factorial(2 * i + 1);
        }
        return sin;
    }

    public static double cosine(double angle) {
        double rad = angle * (Math.PI / 180);
        double cos = 0;
        for (int i = 0; i < 10; i++) { // number of edges of series
            cos += (Math.pow(-1, i) * Math.pow(rad, 2 * i)) / factorial(2 * i);
        }
        return cos;
    }

    // tan function
    public static double tangent(double angle) {
        return sine(angle) / cosine(angle);
    }
}
