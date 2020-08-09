package fr.zabricraft.delta.tokens;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;
import fr.zabricraft.delta.utils.TokenParser;

public class Function extends Token {

    private String name;
    private Token parameter;

    public Function(String name, Token parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String toString() {
        return name + "(" + parameter.toString() + ")";
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        Token parameter = this.parameter.compute(inputs, mode);
        Token expression;
        String variable;

        // Check if an input corresponds to this variable
        if (Arrays.asList(TokenParser.funcs).contains(name)) {
            // Universal func

            // Sin

            // Cos

            // Tan

            // Sqrt
            if (name.equals("sqrt")) {
                expression = new Root(new Variable("x"), new Number(2));
                variable = "x";
            }

            // Exp
            else if (name.equals("exp")) {
                expression = new Power(new Variable("e"), new Variable("x"));
                variable = "x";
            }

            // Log

            // Ln

            // Random
            else if (name.equals("random") && parameter instanceof Number && ((Number) parameter).getValue() > 0) {
                return new Number(Math.abs(new Random().nextLong()) % ((Number) parameter).getValue());
            }

            // Cannot be simplified
            else {
                return this;
            }
        } else if (inputs.containsKey(name) && inputs.get(name) instanceof FunctionDeclaration) {
            // Custom func
            FunctionDeclaration value = (FunctionDeclaration) inputs.get(name);
            expression = value.getToken();
            variable = value.getVariable().trim();
        } else {
            // Unknown func
            return new CalculError();
        }

        // Get inputs and current parameter of function
        Map<String, Token> values = new HashMap<>(inputs);
        if (parameter instanceof Variable) {
            if (!((Variable) parameter).getName().equals(variable)) {
                values.put(variable, parameter);
            }
        } else {
            values.put(variable, parameter);
        }

        // Return computed expression
        return expression.compute(values, mode);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        return defaultApply(operation, right, inputs, mode);
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 2;
    }

    public Token opposite() {
        return new Product(this, new Number(-1));
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        Double paramDouble = parameter.asDouble();

        // Check if an input corresponds to this variable
        if (Arrays.asList(TokenParser.funcs).contains(name) && paramDouble != null) {
            // Universal func

            // Sin
            if (name.equals("sin")) {
                return Math.sin(paramDouble);
            }

            // Cos
            if (name.equals("cos")) {
                return Math.cos(paramDouble);
            }

            // Tan
            if (name.equals("tan")) {
                return Math.tan(paramDouble);
            }

            // Log
            if (name.equals("log")) {
                return Math.log10(paramDouble);
            }

            // Ln
            if (name.equals("ln")) {
                return Math.log(paramDouble);
            }
        }

        return null;
    }

}
