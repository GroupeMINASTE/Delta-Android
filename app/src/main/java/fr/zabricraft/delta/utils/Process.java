package fr.zabricraft.delta.utils;

import android.content.Context;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.tokens.Token;

public class Process {

    public List<Pair<String, Token>> inputs;
    public Map<String, Token> variables;
    public List<String> outputs;
    public Context context;

    public Process(List<Pair<String, Token>> inputs, Context context) {
        this.inputs = inputs;
        this.variables = new HashMap<>();
        this.outputs = new ArrayList<>();
        this.context = context;
    }

}
