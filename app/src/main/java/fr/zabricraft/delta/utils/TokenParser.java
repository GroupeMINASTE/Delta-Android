package fr.zabricraft.delta.utils;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.tokens.Fraction;
import fr.zabricraft.delta.tokens.FunctionDeclaration;
import fr.zabricraft.delta.tokens.Number;
import fr.zabricraft.delta.tokens.Power;
import fr.zabricraft.delta.tokens.SyntaxError;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.tokens.Variable;

public class TokenParser {

    // Constants
    public static final String variables = "abcdefghijklmnopqrstuvwxyzΑαΒβΓγΔδΕεΖζΗηΘθΙιΚκΛλΜμΝνΞξΟοΠπΣσςϹϲΤτΥυΦφΧχΨψΩω";
    public static final String functions = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String variablesAndNumber = variables + "0123456789";
    public static final String productCoefficients = variablesAndNumber + ")";
    public static final String constants = "i";
    public static final String input = variablesAndNumber + functions + "_+-*/%^√,;(){}=<>! ";

    // Parsing vars
    private String tokens;
    private List<String> ops;
    private int i;

    // Token vars
    private List<Token> values;

    // Environment vars
    private Process process;

    // Initializer
    public TokenParser(String tokens, Process process) {
        this.tokens = tokens;
        this.ops = new ArrayList<>();
        this.i = 0;

        this.values = new ArrayList<>();

        this.process = process;
    }

    public TokenParser(String tokens) {
        this.tokens = tokens;
        this.ops = new ArrayList<>();
        this.i = 0;

        this.values = new ArrayList<>();
    }

    // Parse an expression
    public Token execute() {
        // Remove whitespace
        tokens = tokens.replaceAll(" ", "");

        // Check if empty
        if (tokens.isEmpty()) {
            return new Number(0);
        }

        try {
            // For each character of the string
            while (i < tokens.length()) {
                char current = tokens.charAt(i);
                char previous = i > 0 ? tokens.charAt(i - 1) : ' ';

                // Opening brace
                if (current == '(') {
                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Check if last token is a function
                        if (values.get(0) instanceof Variable) {
                            Variable prevar = ((Variable) values.get(0));
                            if (process != null && process.variables.containsKey(prevar.getName()) && process.variables.get(prevar.getName()) instanceof FunctionDeclaration) {
                                // Add a function operator
                                ops.add(0, "f");
                            } else {
                                // Add a multiplication operator
                                ops.add(0, "*");
                            }
                        } else {
                            // Add a multiplication operator
                            ops.add(0, "*");
                        }
                    }

                    // Add it to operations
                    ops.add(0, String.valueOf(current));
                }

                // Other opening brace
                else if (current == '{') {
                    // Add it to operations
                    ops.add(0, String.valueOf(current));
                }

                // Number
                else if (isInt(String.valueOf(current))) {
                    int val = 0;
                    int powerOfTen = 0;

                    // Get other digits
                    while (i < tokens.length() && isInt(String.valueOf(tokens.charAt(i)))) {
                        val = (val * 10) + Integer.parseInt(String.valueOf(tokens.charAt(i)));
                        i++;
                    }

                    // If we have a dot
                    if (i < tokens.length() - 1 && tokens.charAt(i) == '.') {
                        // Pass the dot
                        i++;

                        // And start getting numbers after the dot
                        while (i < tokens.length() && isInt(String.valueOf(tokens.charAt(i)))) {
                            val = (val * 10) + Integer.parseInt(String.valueOf(tokens.charAt(i)));
                            i++;
                            powerOfTen++;
                        }
                    }

                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Add a multiplication operator
                        ops.add(0, "*");
                    }

                    // Insert into values
                    if (powerOfTen > 0) {
                        insertValue(new Fraction(new Number(val), new Power(new Number(10), new Number(powerOfTen))));
                    } else {
                        insertValue(new Number(val));
                    }

                    // Remove one, else current character is skipped
                    i--;
                }

                // Variable
                else if (variables.indexOf(current) != -1) {
                    // Check name
                    StringBuilder name = new StringBuilder();
                    name.append(current);

                    // Check for an index
                    if (i < tokens.length() - 2 && tokens.charAt(i + 1) == '_' && variablesAndNumber.indexOf(tokens.charAt(i + 2)) != -1) {
                        // Add index to variable
                        char index = tokens.charAt(i + 2);
                        name.append('_');
                        name.append(index);

                        // Increment i 2 times to skip index
                        i += 2;
                    }

                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Add a multiplication operator
                        ops.add(0, "*");
                    }

                    // Insert into values
                    insertValue(new Variable(name.toString()));
                }

                // Closing brace
                else if (current == ')') {
                    // Create the token
                    while (!ops.isEmpty() && !ops.get(0).equals("(")) {
                        // Create a token
                        Token value = createValue();
                        if (value != null) {
                            // Insert it into the list
                            insertValue(value);
                        }
                    }

                    // Remove opening brace
                    if (!ops.isEmpty()) {
                        ops.remove(0);
                    }
                }

                // Closing brace
                else if (current == '}') {
                    // Create the token
                    while (!ops.isEmpty() && !ops.get(0).equals("{")) {
                        // Create a token
                        Token value = createValue();
                        if (value != null) {
                            // Insert it into the list
                            insertValue(value);
                        }
                    }

                    // Remove opening brace
                    if (!ops.isEmpty()) {
                        ops.remove(0);
                    }
                }

                // Root
                else if (current == '√') {
                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Add a multiplication operator
                        ops.add(0, "*");
                    }

                    // Insert the 2nd power
                    insertValue(new Number(2));

                    // Add current operation
                    ops.add(0, String.valueOf(current));
                }

                // Operation
                else {
                    // While first operation has same of greater precedence to current, apply to two first values
                    while (!ops.isEmpty() && Operation.from(ops.get(0)) != null && Operation.from(String.valueOf(current)) != null && Operation.from(ops.get(0)).getPrecedence() >= Operation.from(String.valueOf(current)).getPrecedence()) {
                        // Create a token
                        Token value = createValue();
                        if (value != null) {
                            // Insert it into the list
                            insertValue(value);
                        }
                    }

                    // If subtraction with no number before
                    if (current == '-' && values.isEmpty()) {
                        insertValue(new Number(0));
                    }

                    // If next is "="
                    if (i < tokens.length() - 1 && tokens.charAt(i + 1) == '=') {
                        // Add it
                        ops.add(0, String.valueOf(current) + "=");

                        // Increment i
                        i++;
                    } else {
                        // Add current operation
                        ops.add(0, String.valueOf(current));
                    }
                }

                // Increment i
                i++;
            }

            // Entire expression parsed, apply remaining values
            while (!ops.isEmpty()) {
                // Create a token
                Token value = createValue();
                if (value != null) {
                    // Insert it into the list
                    insertValue(value);
                }
            }

            // Return token
            if (!values.isEmpty()) {
                return values.get(0);
            }
        } catch (SyntaxError e) {
            // We have a syntax error, do nothing
        }

        // If the token is not valid
        return new SyntaxError();
    }

    private void insertValue(Token value) {
        // Insert the value in the list
        values.add(0, value);
    }

    private Token createValue() throws SyntaxError {
        // Get tokens
        Token right = getFirstTokenAndRemove();
        Token left = getFirstTokenAndRemove();

        // Get operator and apply
        Operation op = getFirstOperationAndRemove();
        if (op != null) {
            if (op == Operation.root) {
                return op.join(right, left, ops);
            } else {
                return op.join(left, right, ops);
            }
        }

        // Nothing found
        return null;
    }

    private Token getFirstTokenAndRemove() throws SyntaxError {
        // Check if exists
        if (!values.isEmpty()) {
            return values.remove(0);
        }

        // Return a syntax error
        throw new SyntaxError();
    }

    private Operation getFirstOperationAndRemove() {
        // Check if first
        if (!ops.isEmpty()) {
            String first = ops.remove(0);

            // Iterate operations
            for (Operation value : Operation.values()) {
                // If it's the value we want
                if (first.equals(value.rawValue)) {
                    return value;
                }
            }
        }

        // Nothing found
        return null;
    }

    // Check if a number is an integer
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
