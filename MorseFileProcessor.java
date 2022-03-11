import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class MorseFileProcessor {
    private class Task extends Thread {
        Method method;
        TreeMap<String, String> arguments;

        Task(Method method, TreeMap<String, String> arguments) {
            this.method = method;
            this.arguments = arguments;
        }
        @Override
        public void run() {
            MorseFileProcessor mfp = MorseFileProcessor.getInstance();
            try {
                method.invoke(mfp, arguments.get("file-name"), arguments.get("code-table"));
                System.out.println("Thread " + Thread.currentThread().getId()
                        + " is running");
            } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static MorseFileProcessor instance;
    private final TreeMap<String, Method> commands;

    private MorseFileProcessor() throws NoSuchMethodException {
        this.commands = new TreeMap<>();
        this.commands.put("code", MorseFileProcessor.class
                .getDeclaredMethod("encode", String.class, String.class));
        this.commands.put("decode", MorseFileProcessor.class
                .getDeclaredMethod("decode", String.class, String.class));
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
        TreeMap<String, String> arguments = new TreeMap<>();
        arguments.put("file-name",  null);
        arguments.put("code-table", null);
        arguments.put("out-name",   null);

        while(!args.isEmpty()) {
            String flag = args.get(0), argument = args.get(1);
            args.remove(0); args.remove(0);
            switch (flag) {
                case "-f": {
                    arguments.put("file-name", argument);
                    break;
                }
                case "-t": {
                    arguments.put("code-table", argument);
                    break;
                }
                case "-o": {
                    arguments.put("out-name", argument);
                    break;
                }
                default: {
                    //TODO: log invalid argument or throw exception
                    break;
                }
            }
        }
        MorseFileProcessor.Task task = new MorseFileProcessor.Task(this.commands.get(cmd), arguments);
        task.start();
    }

    private void encode(String file_name, String table_name) {
        System.out.println("Encode of " + file_name + " invoked ! "
                + ((table_name==null)?"":("Using encoding table " + table_name + " !")));
        try {
            TableEncoder encoder = new TableEncoder(table_name);
            System.out.println("Encoder created with table `" + table_name + '`');
            File file = new File(file_name);
            Scanner scanner = new Scanner(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decode(String file_name, String table_name) {
        String out_file = "decoded." + file_name;
        System.out.println("Decode of " + file_name + " invoked ! "
                + ((table_name==null)?"":("Using decoding table " + table_name + " !")));
        try {
            TableDecoder decoder = new TableDecoder(table_name);
            System.out.println("Decoder created with table `" + table_name + '`');
            File file = new File(file_name);
            Scanner scanner = new Scanner(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


