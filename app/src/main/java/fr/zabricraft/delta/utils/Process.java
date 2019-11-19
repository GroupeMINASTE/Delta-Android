package fr.zabricraft.delta.utils;

import android.content.Context;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.zabricraft.delta.tokens.FunctionDeclaration;
import fr.zabricraft.delta.tokens.Token;

public class Process {

    public List<Pair<String, String>> inputs;
    public Map<String, Token> variables;
    public List<String> outputs;
    public Context context;

    public Process(List<Pair<String, String>> inputs, Context context) {
        this.inputs = inputs;
        this.variables = new HashMap<>();
        this.outputs = new ArrayList<>();
        this.context = context;
    }

    public void set(String identifier, Token value) {
        Matcher f = Pattern.compile("([a-zA-Z])\\((.+)\\)").matcher(identifier);
        if (f.find()) {
            // Take it as a function
            variables.put(f.group(1), new FunctionDeclaration(f.group(2), value));
        } else {
            // Set it as a variable
            variables.put(identifier, value.compute(variables, false));
        }
    }

}
