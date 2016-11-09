package com.emergya.descartes.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ';';

    public static void writeLine(Writer w, List<? extends Object> list)
            throws IOException {
        writeLine(w, list, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators)
            throws IOException {
        writeLine(w, values, separators, ' ');
    }

    // https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(Object value) {

        String result = value.toString();
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<? extends Object> list,
            char separators, char customQuote) throws IOException {

        boolean first = true;

        // default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (Object value : list) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value))
                        .append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());

    }

}
