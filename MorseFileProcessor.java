import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.ArrayList;

public class MorseFileProcessor {
    private static MorseFileProcessor instance;
    private HashMap<String, Method> commands;
    private ArrayList<String> args;

    private MorseFileProcessor() throws NoSuchMethodException {
        this.commands = new HashMap<>();
        this.commands.put("code", MorseFileProcessor.class.getDeclaredMethod("encode"));
        this.commands.put("decode", MorseFileProcessor.class.getDeclaredMethod("decode"));
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

    public void execute(String cmd, ArrayList<String> args) {
        this.args = args;
        try {
            this.commands.get(cmd).invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void encode() {
        System.out.println("encode invoked");
    }

    private static void decode() {
        System.out.println("decode invoked");
    }
}
