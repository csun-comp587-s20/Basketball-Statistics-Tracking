package basketball;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class CreateFileTest {
    @Rule 
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testInTempFolder() throws IOException {
        File tempFile = testFolder.newFile("file.txt");
        File tempFolder = testFolder.newFolder("folder");
        System.out.println("Test folder: " + testFolder.getRoot());
    }

    @Test 
    public void addInformationTest(){
        CreateFile addinfo1 = new addInformation();
        String output = "pts";
        assertEquals(output, addinfo1.addInformation("pts"));
    }

    @Test 
    public void addInformationTest2(){
        CreateFile addinfo1 = new addInformation();
        String output = "reb";
        assertEquals(output, addinfo1.addInformation("reb"));
    }

    @Test 
    public void addInformationTest3(){
        CreateFile addinfo1 = new addInformation();
        String output = "ast";
        assertEquals(output, addinfo1.addInformation("ast"));
    }

    @Test 
    public void closeFileTest(){
        CreateFile close_file = new closeFile();
        assertTrue(close_file.closeFile());
    }




    /*@Test
    public void addGameConfigTest(){
        CreateFile game_config = new addGameConfig();
        String string = " ";
    }*/

    

    

}

