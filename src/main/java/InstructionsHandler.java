import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by arhont on 4/8/2014.
 *
 * Reads instructions from file. Invoke appropriate methods of LinkedDeque object.
 * Writes results of invoking to the other file.
 */
public class InstructionsHandler {
    private BufferedReader br;
    private BufferedWriter bw;
    private LinkedDeque<String> linkedDeque;

    /**
     * @param inputFile  - path to the file which contains instructions
     * @param outputFile - path to the file in which results will be written
     */
    public InstructionsHandler(String inputFile, String outputFile) throws IOException {
        try {
            br = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.out.println(inputFile + " is not found");
            throw e;
        }
        try {
            bw = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            System.out.println(outputFile + " is not found");
            throw e;
        }
        linkedDeque = new LinkedDeque<>();
    }

    /**
     * Reads strings from inputFile and invoke methods of LinkedDeque object.
     * Results of each invoking is written into the outputFile.
     * Each line of the output file starts with [commandNumber]:
     *
     * if methods returns nothing then "nothing was returned" will be written
     */
    public void handle() {
        int ln = 0;
        String line;
        try {
            while ((line = br.readLine()) != null) {
                bw.write("[" + (ln++) + "]: " + executeInstruction(line) + "\n");
            }
        } catch (IOException e) {
            System.out.println("cannot write into the file");
        }
        shutDown();
    }

    private String executeInstruction(String instruction) {
        try {
            if (instruction.length() == 0) return null;
            String[] str = instruction.split(" ", 2);
            String methodName = str[0];
            Object res;
            if (str.length == 1) {
                Method method = linkedDeque.getClass().getMethod(methodName);
                res=method.invoke(linkedDeque);
            } else {
                Method method = linkedDeque.getClass().getMethod(methodName, Object.class);
                res=method.invoke(linkedDeque, str[1]).toString();
            }
            if (res==null){
                return "nothing was returned";
            } else {
                return res.toString();
            }
        } catch (NoSuchMethodException e) {
            return "no such method in the collection";
        } catch (InvocationTargetException |IllegalAccessException e) {
            return "underlying method threw an exception";
        }
    }

    /**
     * Cleaning:
     * <ul>
     *     <li>Close file handlers</li>
     * </ul>
     */
    public void shutDown(){
        try {
            if (br!=null) br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (bw!=null) bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
