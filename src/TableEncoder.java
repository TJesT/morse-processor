import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableEncoder implements Encoder {
    TreeMap<Character, String> encoding_table = new TreeMap<>();

    TableEncoder(String table_name) throws Exception {
        File file = new File(table_name);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> line_split = Stream.of(line.split(" "))
                    .map(String::new)
                    .collect(Collectors.toList());

            String symbol = line_split.get(0);
            String value = line_split.get(1);
            if (symbol.length() != 1) {
                throw new Exception();
            }

            char c = symbol.charAt(0);
            encoding_table.put(c, value);
        }

        scanner.close();
    }

    @Override
    public String encode(char c) {
        if (Character.isLowerCase(c)) {
            c = Character.toUpperCase(c);
        }
        if (!encoding_table.containsKey(c)) return "";

        return encoding_table.get(c);
    }
}
