package io.geeny;

import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test for testing PrettyPrint
 *
 * @author  Marco Bierbach
 * @version 1.0
 */
public class PrettyPrintTest {

    @After
    public void RemoveLogFiles(){
        File[] files = new File(System.getProperty("user.dir")).listFiles();
        for(File file : files) {
            if(file.getName().endsWith(".log"))
                file.delete();
            System.out.println("Found file:"+file.getName());
        }

        File logsFolder = new File(System.getProperty("user.dir")+"/testLogs");
        if(logsFolder.exists()) {
            File[] testFolderFiles = logsFolder.listFiles();
            for (File file : testFolderFiles)
                file.delete();
            logsFolder.delete();
        }
    }

    /**
     * Method to test the toolpath of pretty print
     */
    @Test
    public void TestWithToolPath(){
        PrettyPrint printer = new PrettyPrint(40,2, PrettyPrintConst.PRINT_TYPE.ALL, "testLogs/");
        CheckValues(printer, 2, 40, PrettyPrintConst.PRINT_TYPE.ALL);

    }

    /**
     * Method to test the default printer declaration
     */
    @Test
    public void TestDefaultPrinter() {
        PrettyPrint printer = new PrettyPrint();
        CheckValues(printer, 2, 80, PrettyPrintConst.PRINT_TYPE.ALL);
    }

    /**
     * Method to test the custom printer declaration
     */
    @Test
    public void TestCustomPrinter() {
        PrettyPrint printer = new PrettyPrint(40,10, PrettyPrintConst.PRINT_TYPE.CONSOLE, "/test/");
        CheckValues(printer, 10, 40, PrettyPrintConst.PRINT_TYPE.CONSOLE);
    }

    /**
     * Method to test the silent mode of pretty print
     */
    @Test
    public void TestSilentMode() {
        System.out.println("Starting to test the Silent mode");
        PrettyPrint printer = new PrettyPrint(PrettyPrintConst.PRINT_TYPE.SILENT);


        //test log system
        //testLogSystem(printer);

        //test the queue system in silent mode
        testQueue(printer);

        //test ui related stuff
        testBorders(printer);

        testWriteToFile(printer);
    }

    /**
     * Method to test the All Mode of pretty print
     */
    @Test
    public void TestAllMode(){
        System.out.println("Starting to test the All mode");
        PrettyPrint printer = new PrettyPrint(PrettyPrintConst.PRINT_TYPE.ALL);


        //test log system
        testLogSystem(printer);

        //test the queue system in silent mode
        testQueue(printer);

        //test ui related stuff
        testBorders(printer);

        testWriteToFile(printer);
    }

    /**
     * Method to test the File Mode of pretty print
     */
    @Test
    public void TestFileMode(){
        System.out.println("Starting to test the File mode");
        PrettyPrint printer = new PrettyPrint(PrettyPrintConst.PRINT_TYPE.FILE);


        //test log system
        testLogSystem(printer);

        //test the queue system in silent mode
        testQueue(printer);

        //test ui related stuff
        testBorders(printer);

        testWriteToFile(printer);
    }

    @Test
    public void TestConsoleMode(){
        System.out.println("Starting to test the Console mode");
        PrettyPrint printer = new PrettyPrint(PrettyPrintConst.PRINT_TYPE.CONSOLE);


        //test log system
        //testLogSystem(printer);

        //test the queue system in silent mode
        testQueue(printer);

        //test ui related stuff
        testBorders(printer);

        //testWriteToFile(printer);
    }

    /**
     * Method to test the log System and how it behaves
     * @param _printer
     */
    private void testLogSystem(PrettyPrint _printer){
        //use a custom log
        _printer.useCustomLog("test");

        Assert.assertEquals(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM, _printer.getConfig().getCurrentLogFileType());

        _printer.toggleLog();

        Assert.assertEquals(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT, _printer.getConfig().getCurrentLogFileType());

        _printer.switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM);

        Assert.assertEquals(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM, _printer.getConfig().getCurrentLogFileType());
    }

    /**
     * Method to test the parseText methods in all varieties
     * @param _printer
     */
    private void testParseText(PrettyPrint _printer) {
        _printer.parseText("this is a long test for a long text to get the text cut into two lines on the console and colored", PrettyPrintConst.COLOR.ANSI_GREEN);

        //test colored background
        _printer.parseText("this is a text with a background color", PrettyPrintConst.BACKGROUND.ANSI_BLUE_BACKGROUND);

        //test colored text and colored background
        _printer.parseText("this is a text with color and background color", PrettyPrintConst.COLOR.ANSI_BLUE, PrettyPrintConst.BACKGROUND.ANSI_PURPLE_BACKGROUND);

        _printer.parseText("thisisaverylongwordwhichhanospaceandwillbreakthespacewhichisavailableforwordsforshowingthemintheconsole");
    }

    /**
     * Method to test all queueText methods
     * @param _printer
     */
    private void testQueue(PrettyPrint _printer){
        _printer.queueText("this ");
        _printer.queueText("is ", PrettyPrintConst.COLOR.ANSI_BLUE);
        _printer.queueText( "a", PrettyPrintConst.BACKGROUND.ANSI_GREEN_BACKGROUND);
        _printer.queueText("test", PrettyPrintConst.COLOR.ANSI_BLUE, PrettyPrintConst.BACKGROUND.ANSI_RED_BACKGROUND);
        _printer.parseQueue();
    }

    /**
     * Method to test all border methods
     * @param _printer
     */
    private void testBorders(PrettyPrint _printer){
        _printer.parseHeadline();
        _printer.parseSeperator();
        _printer.parseBoxEnd();
        _printer.parseBoxStart();
        _printer.parseBoxEnd();
    }

    /**
     * Method to test the writeToFile methods
     * @param _printer
     */
    private void testWriteToFile(PrettyPrint _printer){
        _printer.writeToFile(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);

        _printer.writeToFile();

        _printer.parseText("this is a test for the file writer");
    }

    /**
     * Metho to check the configuration
     * @param _printer
     * @param _padding
     * @param _textLength
     * @param _type
     */
    private void CheckValues(PrettyPrint _printer, int _padding, int _textLength, PrettyPrintConst.PRINT_TYPE _type) {
        Assert.assertEquals(_padding, _printer.getConfig().getPadding());
        Assert.assertEquals(_textLength+(2*_padding), _printer.getConfig().getWidth());
        Assert.assertEquals(_type, _printer.getConfig().getPrintType());
        Assert.assertEquals(_textLength, _printer.getConfig().getTextLength());
    }




}
