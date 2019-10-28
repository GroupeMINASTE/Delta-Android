package fr.zabricraft.delta.views;

public class HomeAdapter {

    // Loader object
    private final AlgorithmLoader loader;

    // Constructor
    public HomeAdapter(AlgorithmLoader loader) {
        this.loader = loader;
    }

    // Loader interface
    public interface AlgorithmLoader {
        void load(int algorithm);
    }

}
