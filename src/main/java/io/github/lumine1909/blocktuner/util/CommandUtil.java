package io.github.lumine1909.blocktuner.util;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil {

    public static List<String> getMatched(List<String> suggestions, String input) {
        if (input.isEmpty()) {
            return suggestions;
        }
        List<String> matched = new ArrayList<>(suggestions.size());
        for (String s : suggestions) {
            if (s.toLowerCase().startsWith(input.toLowerCase())) {
                matched.add(s);
            }
        }
        return matched;
    }

}
