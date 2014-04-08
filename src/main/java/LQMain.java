import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by arhont on 4/8/2014.
 */
public class LQMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("-- <input file> <output file>");
        }
        BufferedReader br;
        BufferedWriter bw;
        try {
            br = new BufferedReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + " is not found");
            return;
        }
        try {
            bw = new BufferedWriter(new FileWriter(args[1]));
        } catch (IOException e) {
            System.out.println(args[1] + " is not found");
            return;
        }
        LinkedDeque<Integer> linkedDeque = new LinkedDeque<>();
        int ln = 0;
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) continue;
                String[] str = line.split(" ", 2);
                String methodName = str[0];
                if (str.length == 1) {
                    Method method = linkedDeque.getClass().getMethod(methodName);
                    bw.write((ln++) + ": " + method.invoke(linkedDeque).toString() + "\n");
                } else {
                    Method method = linkedDeque.getClass().getMethod(methodName, Object.class);
                    bw.write((ln++) + ": " + method.invoke(linkedDeque, Integer.parseInt(str[1])).toString() + "\n");
                }

            }
        } catch (IOException e) {
            System.out.println("io problems");
        } catch (NoSuchMethodException e) {
            System.out.println("No such method");
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println("Something happend");
        }
        try {
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
