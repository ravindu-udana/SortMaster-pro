import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParser {

    public static List<String> getHeaders(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null)
                return new ArrayList<>();
            return parseLine(line);
        }
    }

    // updated adding error handling and validation part
    public static double[] getColumnData(File file, String columnName) throws IOException {
        List<Double> data = new ArrayList<>();
        int colIndex = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read Headers
            String headerLine = br.readLine();
            if (headerLine == null)
                return new double[0];

            List<String> headers = parseLine(headerLine);
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).trim().equals(columnName)) {
                    colIndex = i;
                    break;
                }
            }

            if (colIndex == -1) {
                throw new IOException("Column not found: " + columnName);
            }

            // Read Data
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                List<String> values = parseLine(line);
                if (colIndex < values.size()) {
                    String valStr = values.get(colIndex).trim();
                    try {
                        double val = Double.parseDouble(valStr);
                        data.add(val);
                    } catch (NumberFormatException e) {
                        // Skip non-numeric or empty values in the column
                    }
                }
            }
        }
        // check 1 completed--

        // Convert List<Double> to double[]
        double[] result = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    public static List<String> getNumericHeaders(File file) throws IOException {
        List<String> numericHeaders = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read Headers
            String headerLine = br.readLine();
            if (headerLine == null)
                return numericHeaders;

            List<String> headers = parseLine(headerLine);
            if (headers.isEmpty())
                return numericHeaders;

            // Check first N rows to determine if columns are numeric
            int numRowsToCheck = 50;
            boolean[] isNumeric = new boolean[headers.size()];
            for (int i = 0; i < isNumeric.length; i++)
                isNumeric[i] = true; // Assume true initially

            int rowsChecked = 0;
            String line;
            while ((line = br.readLine()) != null && rowsChecked < numRowsToCheck) {
                if (line.trim().isEmpty())
                    continue;
                List<String> values = parseLine(line);

                for (int i = 0; i < headers.size(); i++) {
                    // If this column was already marked non-numeric, skip
                    if (!isNumeric[i])
                        continue;

                    if (i < values.size()) {
                        String val = values.get(i).trim();
                        // Allow empty strings to pass (might be missing data), but if non-empty, must
                        // be double
                        if (!val.isEmpty()) {
                            try {
                                Double.parseDouble(val);
                            } catch (NumberFormatException e) {
                                isNumeric[i] = false;
                            }
                        }
                    }
                }
                rowsChecked++;
            }

            // Collect headers that remained numeric
            for (int i = 0; i < headers.size(); i++) {
                if (isNumeric[i]) {
                    numericHeaders.add(headers.get(i));
                }
            }
        }
        return numericHeaders;
    }

    // Simple CSV Line splitter handling quotes
    private static List<String> parseLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result;
    }

}
