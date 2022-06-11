import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
                System.out.println("Thread " + Thread.currentThread().getId()
                        + " is running");
                method.invoke(mfp, arguments);
            } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static MorseFileProcessor instance;
    private final TreeMap<String, Method> commands;
    private final TreeMap<String, TreeSet<CharStatistics>> statistics = new TreeMap<>();

    private MorseFileProcessor() throws NoSuchMethodException {
        this.commands = new TreeMap<>();
        this.commands.put("code", MorseFileProcessor.class
                .getDeclaredMethod("encode", TreeMap.class));
        this.commands.put("decode", MorseFileProcessor.class
                .getDeclaredMethod("decode", TreeMap.class));
        this.commands.put("logstat", MorseFileProcessor.class
                .getDeclaredMethod("logStatistics", TreeMap.class));
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

    private void updateChar(String file_name, char c) {
        CharStatistics cs = new CharStatistics(c);
        if (!statistics.containsKey(file_name)) {
            System.out.println("No such statistic !");
            return;
        }
        TreeSet<CharStatistics> ts = statistics.get(file_name);
        if (ts.contains(cs)) {
            CharStatistics ceil = ts.ceiling(cs);
            // CharStatistics floor = statistics.floor(cs);
            ceil.increaseCount();
        } else {
            ts.add(cs);
        }
    }

    private void encode(TreeMap<String, String> arguments) {
        if (arguments.containsKey("file-name")
                && arguments.containsKey("code-table")) {
            this.encode(arguments.get("file-name"), arguments.get("code-table"));
        } else {
            System.out.println("Arguments doesn't have necessary `-f` or `-t` properties !");
        }
    }
    private void encode(String file_name, String table_name) {
        statistics.put(file_name, new TreeSet<>());
        String output_file = "encoded." + file_name;
        System.out.println("Encode of " + file_name + " invoked ! "
                + ((table_name==null)?"":("Using encoding table " + table_name + " !")));
        try {
            TableEncoder encoder = new TableEncoder(table_name);
            BufferedInputStream input = new BufferedInputStream(
                    new FileInputStream(file_name));
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(output_file));

            int tmp;
            while ((tmp=input.read()) != -1) {
                char c = (char) tmp;

                updateChar(file_name, c);

                String morse_string = encoder.encode(c);

                writer.write(morse_string);
                writer.write(" ");
            }

            writer.flush();

            input.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Encoder of " + file_name + " finished work !");
    }

    private void decode(TreeMap<String, String> arguments) {
        if (arguments.containsKey("file-name")
                && arguments.containsKey("code-table")) {
            this.decode(arguments.get("file-name"), arguments.get("code-table"));
        } else {
            System.out.println("Arguments doesn't have necessary `-f` or `-t` properties !");
        }
    }
    private void decode(String file_name, String table_name) {
        String output_file = "decoded." + file_name;
        System.out.println("Decode of " + file_name + " invoked ! "
                + ((table_name==null)?"":("Using decoding table " + table_name + " !")));
        try {
            TableDecoder decoder = new TableDecoder(table_name);
            System.out.println("Decoder created with table `" + table_name + '`');

            File file = new File(file_name);
            Scanner scanner = new Scanner(file);

            FileWriter out = new FileWriter(output_file);
            BufferedWriter writer =  new BufferedWriter(out);

            while(scanner.hasNext()) {
                String morse_string = scanner.next();
                char c = decoder.decode(morse_string);
                writer.write(c);
            }
            writer.flush();

            scanner.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Decoder of " + file_name + " finished work !");
    }

    private void logStatistics(TreeMap<String, String> arguments) {
        if (arguments.containsKey("file-name")) {
            this.logStatistics(arguments.get("file-name"));
        } else {
            System.out.println("Arguments doesn't have necessary `-f` property !");
        }
    }
    private void logStatistics(String file_name) {
        if(!statistics.containsKey(file_name)) {
            System.out.println("No such statistic !");
        } else {
            TreeSet<CharStatistics> ts = statistics.get(file_name);
            for (CharStatistics cs : ts ) {
                System.out.println(cs.getSymbol() + " : " + cs.getCount());
            }
        }
    }
}


