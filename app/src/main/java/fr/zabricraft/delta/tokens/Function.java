package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;
import fr.zabricraft.delta.utils.TokenParser;

public class Function implements Token {

    private String name;
    private Token parameter;

    public Function(String name, Token parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String toString() {
        return name + "(" + parameter.toString() + ")";
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
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
        Map<String, Token> values = inputs;
        if (parameter instanceof Variable) {
            if (!((Variable) parameter).getName().equals(variable)) {
                values.put(variable, parameter);
            }
        } else {
            values.put(variable, parameter);
        }

        // Return computed expression
        return expression.compute(values, format);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Sum
        if (operation == Operation.addition) {
            // Right is a sum
            if (right instanceof Sum) {
                java.util.List<Token> values = new ArrayList<>(((Sum) right).getValues());
                values.add(this);
                return new Sum(values);
            }

            return new Sum(this, right);
        }

        // Difference
        if (operation == Operation.subtraction) {
            return new Sum(this, right.opposite());
        }

        // Product
        if (operation == Operation.multiplication) {
            // Right is a product
            if (right instanceof Product) {
                List<Token> values = new ArrayList<>(((Product) right).getValues());
                values.add(this);
                return new Product(values);
            }

            return new Product(this, right);
        }

        // Fraction
        if (operation == Operation.division) {
            return new Fraction(this, right);
        }

        // Modulo
        if (operation == Operation.modulo) {
            // Return the modulo
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
            // Return the power
            return new Power(this, right);
        }

        // Root
        if (operation == Operation.root) {
            return new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
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
