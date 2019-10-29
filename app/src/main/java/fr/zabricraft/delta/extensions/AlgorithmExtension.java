package fr.zabricraft.delta.extensions;

import java.util.Date;

import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.AlgorithmParser;

public class AlgorithmExtension {

    public static final Algorithm secondDegreeEquation = new AlgorithmParser(0, null, false, "2nd degree equations", new Date(),
            "input \"a\" default \"1\"\n" +
                    "input \"b\" default \"7\"\n" +
                    "input \"c\" default \"12\"\n" +
                    "if \"a != 0\" {\n" +
                    "    set \"Δ\" to \"b ^ 2 - 4ac\"\n" +
                    "    set \"α\" to \"(-b) / (2a)\"\n" +
                    "    set \"β\" to \"(-Δ) / (4a)\"\n" +
                    "    set_formatted \"f(x)\" to \"ax ^ 2 + bx + c\"\n" +
                    "    print \"f(x)\"\n" +
                    "    set_formatted \"f(x)\" to \"a(x - α) ^ 2 + β\"\n" +
                    "    print \"f(x)\"\n" +
                    "    print \"Δ\"\n" +
                    "    if \"Δ = 0\" {\n" +
                    "        set \"x_0\" to \"α\"\n" +
                    "        print \"x_0\"\n" +
                    "        set_formatted \"f(x)\" to \"a(x - x_0) ^ 2\"\n" +
                    "        print \"f(x)\"\n" +
                    "    } else {\n" +
                    "        set \"x_1\" to \"(-b - √(Δ)) / (2a)\"\n" +
                    "        set \"x_2\" to \"(-b + √(Δ)) / (2a)\"\n" +
                    "        print \"x_1\"\n" +
                    "        print \"x_2\"\n" +
                    "        set_formatted \"f(x)\" to \"a(x - x_1)(x - x_2)\"\n" +
                    "        print \"f(x)\"\n" +
                    "    }\n" +
                    "}").execute();

    public static final Algorithm[] defaults = new Algorithm[]{secondDegreeEquation};

}
