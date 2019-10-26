package fr.zabricraft.delta.utils;

import java.util.ArrayList;
import java.util.List;

public class Database {

    // Static instance
    public static final Database current = new Database();

    // Properties


    // Initialize
    private Database() {
        // TODO
    }

    // Get algorithms
    public List<Algorithm> getAlgorithms() {
        // Initialize an array
        List<Algorithm> list = new ArrayList<>();

        // TODO

        // Return found algorithms
        return list;
    }

    // Add an algorithm into database
    public Algorithm addAlgorithm(Algorithm algorithm) {
        // TODO

        // Return algorithm
        return algorithm;
    }

    // Update an algorithm
    public Algorithm updateAlgorithm(Algorithm algorithm) {
        // TODO

        // Return algorithm
        return algorithm;
    }

    // Delete an algorithm
    public void deleteAlgorith(Algorithm algorithm) {
        // TODO
    }

}
