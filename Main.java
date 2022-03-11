import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        MorseFileProcessor mfp = MorseFileProcessor.getInstance();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            List<String> arg = Stream.of(line.split(" "))
                    .map(String::new)
                    .collect(Collectors.toList());

            String cmd = arg.get(0);
            arg.remove(0);

            mfp.execute(cmd, arg);
        }

        scanner.close();
    }
}
