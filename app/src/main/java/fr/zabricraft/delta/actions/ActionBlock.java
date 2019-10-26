package fr.zabricraft.delta.actions;

import java.util.List;

public interface ActionBlock extends Action {

    void append(List<Action> actions);

    void insert(Action action, int index);

    void delete(int index);

}
