import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class MorseFileProcessor {
    private static MorseFileProcessor instance;
    private final HashMap<String, Method> commands;
    private final HashMap<String, Object> arguments;

    private MorseFileProcessor() throws NoSuchMethodException {
        this.commands = new HashMap<>();
        this.commands.put("code", MorseFileProcessor.class.getDeclaredMethod("encode"));
        this.commands.put("decode", MorseFileProcessor.class.getDeclaredMethod("decode"));

        this.arguments = new HashMap<>();
        this.arguments.put("file-name", null);
    }

    public static MorseFileProcessor getInstance() {
        if(instance == null) {
            try {
                instance = new MorseFileProcessor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void execute(String cmd, List<String> args) {
        while(!args.isEmpty()) {
            String flag = args.get(0), argument = args.get(1);
            args.remove(0); args.remove(0);
            switch (flag) {
                case "-f": {
                    this.arguments.put("file-name", argument);
                    break;
                }
                default: {
                    //TODO: log invalid argument or throw exception
                    break;
                }
            }
        }
        try {
            this.commands.get(cmd).invoke(getInstance());

        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void encode() {
        Encoder encoder = new Encoder(this.arguments.get("file-name").toString());
        encoder.start();
    }

    private void decode() {
        Decoder decoder = new Decoder(this.arguments.get("file-name").toString());
        decoder.start();
    }
}

class Encoder extends Thread {
    String file_name;
    public Encoder(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                    "Encode of " + this.file_name
                    + " invoked ! Thread "
                    + Thread.currentThread().getId()
                    + " is running .");
        }
        catch (Exception e) {
            System.out.printf("Exception caught !");
        }
    }
}

class Decoder extends Thread {
    String file_name;
    public Decoder(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                    "Decode of " + this.file_name
                    + " invoked ! Thread "
                    + Thread.currentThread().getId()
                    + " is running .");
        }
        catch (Exception e) {
            System.out.printf("Exception caught !");
        }
    }
}
