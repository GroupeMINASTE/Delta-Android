package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Vector extends Token {

    private List<Token> values;

    public Vector(List<Token> values) {
        this.values = values;
    }

    public List<Token> getValues() {
        return values;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (Token value : values) {
            if (string.length() != 0) {
                string.append(", ");
            }
            string.append(value.toString());
        }

        string.insert(0, "(");
        string.append(")");
        return string.toString();
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
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
        List<Token> values = new ArrayList<>();
        for (Token value : getValues()) {
            values.add(value.opposite());
        }
        return new Vector(values);
    }

    public Token inverse() {
        // Unknown
        return this;
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        return null;
    }

}
