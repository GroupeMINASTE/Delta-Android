package fr.zabricraft.delta.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public static final String variables = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZΑαΒβΓγΔδΕεΖζΗηΘθΙιΚκΛλΜμΝνΞξΟοΠπΣσςϹϲΤτΥυΦφΧχΨψΩω";
    public static final String variablesAndNumber = variables + "0123456789";
    public static final String productCoefficients = variablesAndNumber + ")";
    public static final String constants = "ieπ";
    public static final String input = variablesAndNumber + "_+-*/%^√,;(){}=<>! ";
    public static final String[] funcs = {"sin", "cos", "tan", "sqrt", "exp", "log", "ln", "random"};

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
                            if ((process != null && process.variables.containsKey(prevar.getName()) && process.variables.get(prevar.getName()) instanceof FunctionDeclaration) || Arrays.asList(TokenParser.funcs).contains(prevar.getName())) {
                                // Add a function operator
                                insertOperation("f");
                            } else {
                                // Add a multiplication operator
                                insertOperation("*");
                            }
                        } else {
                            // Add a multiplication operator
                            insertOperation("*");
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
                else if (isLong(String.valueOf(current))) {
                    long val = 0;
                    long powerOfTen = 0;

                    // Get other digits
                    while (i < tokens.length() && isLong(String.valueOf(tokens.charAt(i)))) {
                        val = (val * 10) + Long.parseLong(String.valueOf(tokens.charAt(i)));
                        i++;
                    }

                    // If we have a dot
                    if (i < tokens.length() - 1 && tokens.charAt(i) == '.') {
                        // Pass the dot
                        i++;

                        // And start getting numbers after the dot
                        while (i < tokens.length() && isLong(String.valueOf(tokens.charAt(i)))) {
                            val = (val * 10) + Long.parseLong(String.valueOf(tokens.charAt(i)));
                            i++;
                            powerOfTen++;
                        }
                    }

                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Add a multiplication operator
                        insertOperation("*");
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

                    // Check for a function name
                    StringBuilder function = new StringBuilder();
                    function.append(name.toString());
                    int j = i;
                    while (j < tokens.length() - 1 && variablesAndNumber.indexOf(tokens.charAt(j + 1)) != -1) {
                        // Add character to function name
                        function.append(tokens.charAt(j + 1));

                        // Increment j to continue
                        j++;
                    }

                    // Check if a function is recognized
                    if (Arrays.asList(funcs).contains(function.toString().toLowerCase())) {
                        // We have a function
                        name = function;

                        // Set i to j to skip function name
                        i = j;
                    } else {
                        // It's a classic variable, continue

                        // Check for an index
                        if (i < tokens.length() - 2 && tokens.charAt(i + 1) == '_') {
                            if (tokens.charAt(i + 2) == '(') {
                                // Get everything until closing brace
                                StringBuilder index_builder = new StringBuilder();
                                j = i + 2;
                                while (j < tokens.length() - 1 && input.indexOf(tokens.charAt(j + 1)) != -1 && tokens.charAt(j + 1) != ')') {
                                    // Add character to index
                                    index_builder.append(tokens.charAt(j + 1));

                                    // Increment j to continue
                                    j++;
                                }

                                // Increment i to skip brace
                                i = j + 1;

                                // Trim
                                String index = index_builder.toString().trim();

                                if (!index.isEmpty()) {
                                    // Add index to variable
                                    if (index.length() == 1) {
                                        name.append('_');
                                        name.append(index);
                                    } else {
                                        name.append("_(");
                                        name.append(index);
                                        name.append(')');
                                    }
                                }
                            } else if (input.indexOf(tokens.charAt(i + 2)) != -1) {
                                // Add index to variable
                                char index = tokens.charAt(i + 2);
                                name.append('_');
                                name.append(index);

                                // Increment i 2 times to skip index
                                i += 2;
                            }
                        }
                    }

                    // Check if we have a token before without operator
                    if (values.size() > 0 && productCoefficients.indexOf(previous) != -1) {
                        // Add a multiplication operator
                        insertOperation("*");
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
                        insertOperation("*");
                    }

                    // Insert the 2nd power
                    insertValue(new Number(2));

                    // Add current operation
                    insertOperation(String.valueOf(current));
                }

                // Operation
                else {
                    // Insert operation
                    insertOperation(String.valueOf(current));
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
        } catch (SyntaxException e) {
            // We have a syntax error, do nothing
        }

        // If the token is not valid
        return new SyntaxError();
    }

    private void insertValue(Token value) {
        // Insert the value in the list
        values.add(0, value);
    }

    private void insertOperation(String op) throws SyntaxException {
        // While first operation has same of greater precedence to current, apply to two first values
        while (!ops.isEmpty() && Operation.from(ops.get(0)) != null && Operation.from(op) != null && Operation.from(ops.get(0)).getPrecedence() >= Operation.from(op).getPrecedence()) {
            // Create a token
            Token value = createValue();
            if (value != null) {
                // Insert it into the list
                insertValue(value);
            }
        }

        // If subtraction with no number before
        if (op.equals("-") && (values.isEmpty() || (i > 0 && tokens.charAt(i - 1) == '('))) {
            insertValue(new Number(0));
        }

        // If next is "="
        if (i < tokens.length() - 1 && tokens.charAt(i + 1) == '=') {
            // Add it
            ops.add(0, op + "=");

            // Increment i
            i++;
        } else {
            // Add current operation
            ops.add(0, op);
        }
    }

    private Token createValue() throws SyntaxException {
        // Get tokens
        Token right = getFirstTokenAndRemove();
        Token left = getFirstTokenAndRemove();

        // Get operator and apply
        Operation op = getFirstOperationAndRemove();
        if (op != null) {
            if (op == Operation.root) {
                return op.join(right, left, ops, process != null ? process.variables : new HashMap<String, Token>());
            } else {
                return op.join(left, right, ops, process != null ? process.variables : new HashMap<String, Token>());
            }
        }

        // Nothing found
        return null;
    }

    private Token getFirstTokenAndRemove() throws SyntaxException {
        // Check if exists
        if (!values.isEmpty()) {
            return values.remove(0);
        }

        // Return a syntax error
        throw new SyntaxException();
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
    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
