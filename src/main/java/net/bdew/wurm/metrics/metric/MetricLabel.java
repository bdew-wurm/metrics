package net.bdew.wurm.metrics.metric;

public class MetricLabel {
    public final String key;
    public final String value;
    private final String text;

    public static MetricLabel defaultLabel;

    public MetricLabel(String key, String value) {
        this.key = key;
        this.value = value;

        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                default:
                    sb.append(c);
            }
        }

        sb.append("\"");

        text = sb.toString();
    }

    public void write(StringBuilder sb) {
        sb.append(text);
    }
}
