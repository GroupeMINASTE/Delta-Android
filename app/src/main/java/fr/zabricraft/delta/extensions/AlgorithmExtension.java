package fr.zabricraft.delta.extensions;

import java.util.Date;

import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import fr.zabricraft.delta.utils.AlgorithmParser;

public class AlgorithmExtension {

    public static final Algorithm secondDegreeEquation = new AlgorithmParser(0, 1L, false, "2nd degree equations", new Date(0), new AlgorithmIcon("function", "river"), "print_text \"Updating from server...\"").execute();

    public static final Algorithm[] defaults = new Algorithm[]{secondDegreeEquation};

}
