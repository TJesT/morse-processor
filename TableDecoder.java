import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableDecoder implements Decoder {
    TreeMap<String, Character> decoding_table = new TreeMap<>();
    TableDecoder(String table_name) throws Exception {
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
            decoding_table.put(value, c);
        }

        scanner.close();
    }

    @Override
    public char decode(String s) throws Exception {
        if (!decoding_table.containsKey(s)) {
            throw new Exception();
        }
        return decoding_table.get(s);
    }
}
