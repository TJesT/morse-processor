import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String cmd = scanner.next();
        ArrayList<String> arg = new ArrayList<>();
        while (scanner.hasNext()) {
            arg.add(scanner.next());
        }

        MorseFileProcessor mfp = MorseFileProcessor.getInstance();

        mfp.execute(cmd, arg);

        scanner.close();
    }
}
