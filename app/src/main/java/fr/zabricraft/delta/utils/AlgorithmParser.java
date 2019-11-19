package fr.zabricraft.delta.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.ActionBlock;
import fr.zabricraft.delta.actions.ElseAction;
import fr.zabricraft.delta.actions.ForAction;
import fr.zabricraft.delta.actions.IfAction;
import fr.zabricraft.delta.actions.InputAction;
import fr.zabricraft.delta.actions.PrintAction;
import fr.zabricraft.delta.actions.PrintTextAction;
import fr.zabricraft.delta.actions.RootAction;
import fr.zabricraft.delta.actions.SetAction;
import fr.zabricraft.delta.actions.WhileAction;
import fr.zabricraft.delta.tokens.Token;

public class AlgorithmParser {

    // Constants
    public static final String characters = "abcdefghijklmnopqrstuvwxyz_";

    // Parsing vars
    private String lines;
    private List<String> keywords;
    private List<String> tokens;
    private int i;

    // Algorithm vars
    private int local_id;
    private Integer remote_id;
    private boolean owner;
    private String name;
    private Date last_update;
    private List<Action> actions;

    // Initializer
    public AlgorithmParser(int local_id, Integer remote_id, boolean owner, String name, Date last_update, String lines) {
        this.lines = lines;
        this.keywords = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.i = 0;

        this.local_id = local_id;
        this.remote_id = remote_id;
        this.name = name;
        this.owner = owner;
        this.last_update = last_update;
        this.actions = new ArrayList<>();
    }

    // Parse an algorithm
    public Algorithm execute() {
        // For each character of the string
        while (i < lines.length()) {
            char current = lines.charAt(i);

            // Opening brace
            if (current == '{') {
                // Some vars
                i++;
                StringBuilder content = new StringBuilder();
                int count = 0;

                // Get text until its closing brace
                while (lines.charAt(i) != '}' || count != 0) {
                    // Add current
                    content.append(lines.charAt(i));

                    // Check for another opening brace
                    if (lines.charAt(i) == '{') {
                        count++;
                    }

                    // Closing
                    if (lines.charAt(i) == '}') {
                        count--;
                    }

                    // Increment i
                    i++;
                }

                // Parse block
                List<Action> block = new AlgorithmParser(0, null, false, "", null, content.toString()).execute().getRoot().getActions();

                // Create an action from the line
                Action action = createAction();
                if (action instanceof ActionBlock) {
                    // Add braces actions
                    ((ActionBlock) action).append(block);

                    // If it is an ElseAction
                    if (action instanceof ElseAction) {
                        // Check for IfAction
                        if (actions.size() > 0) {
                            Action first = actions.get(0);
                            if (first instanceof IfAction) {
                                // Set elseAction
                                ((IfAction) first).setElseAction(((ElseAction) action));
                            }
                        }
                    } else {
                        // Add it to the list
                        insertAction(action);
                    }
                }
            }

            // Characters
            else if (characters.indexOf(current) != -1) {
                StringBuilder keyword = new StringBuilder();

                // Get all the characters
                while (i < lines.length() && characters.indexOf(lines.charAt(i)) != -1) {
                    keyword.append(lines.charAt(i));
                    i++;
                }

                // Insert it into keywords
                keywords.add(0, keyword.toString());

                // Remove one, else current character is skipped
                i--;
            }

            // Tokens
            else if (current == '"') {
                StringBuilder token = new StringBuilder();
                i++;

                // Get all the characters
                while (i < lines.length() && lines.charAt(i) != '"') {
                    // Check for \
                    if (lines.charAt(i) == '\\' && i < lines.length() - 1) {
                        // Skip it and get next character
                        i++;
                    }

                    // Add character to string
                    token.append(lines.charAt(i));
                    i++;
                }

                // Insert into tokens
                tokens.add(0, token.toString());
            }

            // New line
            else if (current == '\n') {
                // Create an action from the line
                Action action = createAction();
                if (action != null) {
                    // Add it to the list
                    insertAction(action);
                }
            }

            // Increment i
            i++;
        }

        // If something left, add it
        while (!keywords.isEmpty()) {
            // Create an action from the line
            Action action = createAction();
            if (action != null) {
                // Add it to the list
                insertAction(action);
            }
        }

        // Reverse actions
        Collections.reverse(actions);

        // Create an algorithm with parsed data
        return new Algorithm(local_id, remote_id, owner, name, last_update, new RootAction(actions));
    }

    private void insertAction(Action action) {
        // Insert action into actions
        actions.add(0, action);
    }

    private Action createAction() {
        // Check if first
        if (keywords.size() > 0) {
            // Get first and remove it
            String first = keywords.remove(0);

            // Keyword list
            Keyword[] alone = {Keyword.If, Keyword.Else, Keyword.Print, Keyword.PrintText, Keyword.While};
            HashMap<Keyword, Keyword[]> grouped = new HashMap<>();
            grouped.put(Keyword.Default, new Keyword[]{Keyword.Input});
            grouped.put(Keyword.In, new Keyword[]{Keyword.For});
            grouped.put(Keyword.To, new Keyword[]{Keyword.Set, Keyword.SetFormatted});

            // Iterate values
            for (Keyword key : grouped.keySet()) {
                // If it's the keyword we want
                if (first.equals(key.rawValue)) {
                    // Get the next one
                    if (keywords.size() > 0) {
                        // Get it and remove it
                        String second = keywords.remove(0);

                        // Iterate values
                        for (Keyword value : grouped.get(key)) {
                            // Check if second is the correct value
                            if (second.equals(value.rawValue)) {
                                // Create action and return it
                                if (value == Keyword.Input && tokens.size() >= 2) {
                                    // Input "identifier" default "token"
                                    Token token = new TokenParser(tokens.remove(0)).execute();
                                    String identifier = tokens.remove(0);
                                    return new InputAction(identifier, token);
                                } else if (value == Keyword.For && tokens.size() >= 2) {
                                    // For "identifier" in "token"
                                    Token token = new TokenParser(tokens.remove(0)).execute();
                                    String identifier = tokens.remove(0);
                                    return new ForAction(identifier, token);
                                } else if ((value == Keyword.Set || value == Keyword.SetFormatted) && tokens.size() >= 2) {
                                    // Set "identifier" to "token"
                                    Token token = new TokenParser(tokens.remove(0)).execute();
                                    String identifier = tokens.remove(0);
                                    return new SetAction(identifier, token);
                                }
                            }
                        }
                    }
                }
            }

            // Iterate values
            for (Keyword value : alone) {
                // It it's the keyword we want
                if (first.equals(value.rawValue)) {
                    // Create action and return it
                    if (value == Keyword.If && tokens.size() >= 1) {
                        // If "condition"
                        Token condition = new TokenParser(tokens.remove(0)).execute();
                        return new IfAction(condition);
                    } else if (value == Keyword.Else) {
                        // Else
                        return new ElseAction();
                    } else if (value == Keyword.Print && tokens.size() >= 1) {
                        // Print variable "identifier"
                        String identifier = tokens.remove(0);
                        return new PrintAction(identifier);
                    } else if (value == Keyword.PrintText && tokens.size() >= 1) {
                        // Print text "text"
                        String text = tokens.remove(0);
                        return new PrintTextAction(text);
                    } else if (value == Keyword.While && tokens.size() >= 1) {
                        // While "condition"
                        Token condition = new TokenParser(tokens.remove(0)).execute();
                        return new WhileAction(condition);
                    }
                }
            }
        }

        // Nothing found
        return null;
    }

}
