import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by arhont on 4/10/2014.
 */
public class TestInstructionHandler {
    private String relativePath;

    @Before
    public void setUp() throws Exception {
        relativePath=getClass().getResource("").getPath();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionIn() throws Exception {
        new InstructionsHandler(relativePath+"nonexistent",relativePath+"outBad.txt").handle();

    }

    @Test
    public void testGood() throws Exception {
        ArrayList<String> expectedLines=new ArrayList<>();
        expectedLines.add("[0]: true");
        expectedLines.add("[1]: true");
        expectedLines.add("[2]: true");
        expectedLines.add("[3]: true");
        expectedLines.add("[4]: 4");
        expectedLines.add("[5]: 5");
        checkOut(expectedLines,"testInGood.txt","outGood.txt");
    }

    @Test
    public void testBad() throws Exception {
        ArrayList<String> expectedLines=new ArrayList<>();
        expectedLines.add("[0]: no such method in the collection");
        expectedLines.add("[1]: no such method in the collection");
        expectedLines.add("[2]: underlying method threw an exception");
        checkOut(expectedLines,"testInBad.txt","outBad.txt");
    }

    public void checkOut(ArrayList<String> expectedLines, String inFilePath,String outFilePath) throws Exception{
        new InstructionsHandler(relativePath+inFilePath,relativePath+outFilePath).handle();
        ArrayList<String> actualLines=new ArrayList<>();
        try (BufferedReader br=new BufferedReader(new FileReader(relativePath+outFilePath))){
            String line;
            while ((line = br.readLine()) != null) {
                actualLines.add(line);
            }
        }
        Assert.assertEquals(expectedLines,actualLines);
    }
}
