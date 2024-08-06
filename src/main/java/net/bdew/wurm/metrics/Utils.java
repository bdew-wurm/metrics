package net.bdew.wurm.metrics;

public class Utils {
    public static String escapeLabelValue(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                    res.append("\\\\");
                    break;
                case '\"':
                    res.append("\\\"");
                    break;
                case '\n':
                    res.append("\\n");
                    break;
                default:
                    res.append(c);
            }
        }
        return res.toString();
    }
}
