package matrixx.logic;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEvaluator {

    // Evaluates the given expression string and returns the result.
    public static double evaluate(String expr) throws Exception {
        if (expr.trim().isEmpty()) {
            throw new Exception("Empty expression");
        }

        // Validate the expression for invalid characters
        validateExpression(expr);

        // Replace known constants with their values
        expr = expr.replace("e", String.valueOf(Math.E));
        expr = expr.replace("π", String.valueOf(Math.PI));

        // Preprocess the expression to handle sin, cos, etc.
        expr = preprocessExpression(expr);

        return parseExpressionWithParentheses(expr);
    }

    // Validates the expression to ensure only valid characters are present
    private static void validateExpression(String expr) throws Exception {
        // Regular expression that allows digits, operators, parentheses, and functions (sin, cos, tan, sqrt, etc.)
        String regex = "^[0-9\\+\\-\\*/^\\(\\)\\.!\\sin\\cos\\tan\\sqrt\\eπ]*$";

        // Check if the expression contains any invalid characters
        if (!expr.matches(regex)) {
            throw new Exception("Invalid characters in expression");
        }

        // Check for mismatched parentheses
        int openParen = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') {
                openParen++;
            }
            if (c == ')') {
                openParen--;
            }
            if (openParen < 0) {
                throw new Exception("Mismatched parentheses");
            }
        }

        if (openParen != 0) {
            throw new Exception("Mismatched parentheses");
        }
    }

    private static double parseExpressionWithParentheses(String expr) throws Exception {
        while (expr.contains("(")) {
            int openIndex = expr.lastIndexOf("(");
            int closeIndex = expr.indexOf(")", openIndex);
            if (closeIndex == -1) {
                throw new Exception("Mismatched parentheses");
            }

            String innerExpr = expr.substring(openIndex + 1, closeIndex);
            double innerResult = parseAdditionSubtraction(innerExpr);
            expr = expr.substring(0, openIndex) + innerResult + expr.substring(closeIndex + 1);
        }
        return parseAdditionSubtraction(expr);
    }

    private static double parseAdditionSubtraction(String expr) throws Exception {
        List<String> terms = splitExpression(expr, "+", "-");
        double result = parseMultiplicationDivision(terms.get(0));
        for (int i = 1; i < terms.size(); i += 2) {
            String operator = terms.get(i);
            double nextValue = parseMultiplicationDivision(terms.get(i + 1));
            result = operator.equals("+") ? result + nextValue : result - nextValue;
        }
        return result;
    }

    private static double parseMultiplicationDivision(String expr) throws Exception {
        List<String> factors = splitExpression(expr, "*", "/");
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
            }
        }
        return result;
    }

    private static double parsePower(String expr) throws Exception {
        List<String> parts = splitExpression(expr, "^");
        double base = parseFactorial(parts.get(0));
        for (int i = 1; i < parts.size(); i += 2) {
            double exponent = parseFactorial(parts.get(i + 1));
            base = Math.pow(base, exponent);
        }
        return base;
    }

    // Calculates the factorial of a number (only non-negative integers are allowed).
    private static double factorial(double n) {
        if (n < 0 || n != Math.floor(n)) {
            return Double.NaN; // Factorial is only for non-negative integers

                }double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private static double parseFactorial(String expr) throws Exception {
        expr = expr.trim();
        if (expr.endsWith("!")) {
            double value = parseUnary(expr.substring(0, expr.length() - 1));
            return factorial(value);
        }
        return parseUnary(expr);
    }

    private static double parseUnary(String expr) throws Exception {
        expr = expr.trim();
        if (expr.startsWith("sin(")) {
            return Math.sin(Math.toRadians(parseExpressionWithParentheses(expr.substring(4, expr.length() - 1)))); 
        }else if (expr.startsWith("cos(")) {
            return Math.cos(Math.toRadians(parseExpressionWithParentheses(expr.substring(4, expr.length() - 1)))); 
        }else if (expr.startsWith("tan(")) {
            return Math.tan(Math.toRadians(parseExpressionWithParentheses(expr.substring(4, expr.length() - 1)))); 
        }else if (expr.startsWith("sqrt(")) {
            return Math.sqrt(parseExpressionWithParentheses(expr.substring(5, expr.length() - 1)));
        }
        return parsePrimary(expr);
    }

    private static double parsePrimary(String expr) throws Exception {
        expr = expr.trim();
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return evaluate(expr.substring(1, expr.length() - 1));
        }
        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid number format: " + expr);
        }
    }

    // Preprocess the expression to handle sin, cos, etc.
    private static String preprocessExpression(String expr) {
        // Replace sin with Math.sin, cos with Math.cos, tan with Math.tan, etc.
        expr = expr.replaceAll("(?i)sin", "Math.sin");
        expr = expr.replaceAll("(?i)cos", "Math.cos");
        expr = expr.replaceAll("(?i)tan", "Math.tan");
        expr = expr.replaceAll("(?i)log", "Math.log");
        expr = expr.replaceAll("(?i)exp", "Math.exp");
        expr = expr.replaceAll("(?i)sqrt", "Math.sqrt");

        return expr;
    }

    private static List<String> splitExpression(String expr, String... operators) {
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

    private static boolean isOperator(char c, String... operators) {
        for (String operator : operators) {
            if (operator.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}
