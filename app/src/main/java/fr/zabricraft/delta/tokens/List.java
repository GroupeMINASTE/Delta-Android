package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class List implements Token {

    private java.util.List<Token> values;

    public List(java.util.List<Token> values) {
        this.values = values;
    }

    public java.util.List<Token> getValues() {
        return values;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (Token value : values) {
            string.append(", ");
            string.append(value.toString());
        }
        if (string.length() > 0) {
            string.substring(2);
        }

        string.insert(0, "{");
        string.append("}");
        return string.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        //right = right.compute(inputs, format);

        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        // Unknown
        return this;
    }

    public Token inverse() {
        // Unknown
        return this;
    }

    public Double asDouble() {
        return null;
    }

}
