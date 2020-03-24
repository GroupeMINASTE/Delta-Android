package fr.zabricraft.delta.utils;

import android.content.Context;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.zabricraft.delta.quiz.Quiz;
import fr.zabricraft.delta.tokens.FunctionDeclaration;
import fr.zabricraft.delta.tokens.Token;

public class Process {

    public List<Pair<String, String>> inputs;
    public Map<String, Token> variables;
    public List<Object> outputs;
    public Semaphore semaphore = new Semaphore(0);
    public Quiz quiz;
    public Context context;

    public Process(List<Pair<String, String>> inputs, Context context) {
        this.inputs = inputs;
        this.variables = new HashMap<>();
        this.outputs = new ArrayList<>();
        this.context = context;
    }

    public void set(String identifier, Token value) {
        String trimmed = identifier.trim();
        Matcher f = Pattern.compile("([" + TokenParser.variables + "])\\( *([" + TokenParser.variables + "]) *\\)").matcher(trimmed);
        if (f.find()) {
            // Take it as a function
            variables.put(f.group(1), new FunctionDeclaration(f.group(2), value));
        } else {
            // Set it as a variable
            variables.put(trimmed, value.compute(variables, false));
        }
    }

    public void unset(String identifier) {
        String trimmed = identifier.trim();
        Matcher f = Pattern.compile("([" + TokenParser.variables + "])\\( *([" + TokenParser.variables + "]) *\\)").matcher(trimmed);
        if (f.find()) {
            // Take it as a function
            variables.remove(f.group(1));
        } else {
            // Unset it as a variable
            variables.remove(trimmed);
        }
    }

}
