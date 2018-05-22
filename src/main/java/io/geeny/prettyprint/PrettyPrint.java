package io.geeny.prettyprint;

import io.geeny.prettyprint.model.Log;
import io.geeny.prettyprint.model.LogFile;
import io.geeny.prettyprint.model.PrettyConfig;
import io.geeny.prettyprint.model.TextElement;
import io.geeny.prettyprint.util.PrettyPrintConst;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;


/**
 * Helper io.geeny.util to print lines based on the setup of how big the console output shall be
 * @author Marco Bierbach
 * @version 1.7
 */
public class PrettyPrint {

    public PrettyConfig config;
    public static PrettyPrint instance;

    /**
     * Getter method for the pretty print config
     * @return PrettyConfig object representing the config object which contains all configurations for the pretty print instance
     */
    public PrettyConfig getConfig(){
        return config;
    }

    /**
     * Constructor
     * @param _textLength int length of the text
     * @param _padding int padding to the borders
     * @param _printType enum PRINT_TYPE print type which defines what pretty print shall do with messages
     * @param _toolPath string path used by the tool who uses pretty print
     */
    public PrettyPrint(int _textLength, int _padding, PrettyPrintConst.PRINT_TYPE _printType, String _toolPath) {
        instance = this;
        config = new PrettyConfig(_textLength, _padding, _printType);
        setToolPath(_toolPath);
        createLogFolder();

        if(_printType.equals(PrettyPrintConst.PRINT_TYPE.ALL) || _printType.equals(PrettyPrintConst.PRINT_TYPE.FILE)) {
            createLogFile(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
        }
    }

    /**
     * Constructor for pretty print
     * @param _printType print type which defines what pretty print shall do with messages
     */
    public PrettyPrint(PrettyPrintConst.PRINT_TYPE _printType){
        this(80,2,_printType, null);
    }

    /**
     * Default Contructor for pretty print
     */
    public PrettyPrint(){
        this(PrettyPrintConst.PRINT_TYPE.ALL);
    }

    /**
     * Method to set the toolPath for prettyprint
     * @param _toolPath string representing the tool path the tool which uses pretty print is executed from
     */
    public void setToolPath(String _toolPath){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT) ||
            config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.CONSOLE))
            return;

        //the printType is either all or logfile
        config.setToolPath(_toolPath);
    }


    /**
     * Method to create a new Logfile, based on what pp get's as information
     * @return log object which was created/found
     */
    private Log createLogFile(PrettyPrintConst.LOG_FILE_TYPE _logFileType) {
        Log log = config.getLogs().get(_logFileType);
        log.setLogFile(new LogFile(log.getLogName()));
        parseText("Logfile " +log.getLogName() + " created", PrettyPrintConst.COLOR.ANSI_GREEN);

        return log;
    }

    /**
     * Method to toggle between the custom and default logfile
     */
    public void toggleLog(){
        Log currentLogFile = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);
        Log defaultLogFile = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
        Log customLogFile = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM);

        if(currentLogFile.getLogName().equals(defaultLogFile.getLogName())) {
            //switch to custom logfile

            //move the current back to default
            config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT, currentLogFile);

            //move the custom to current
            config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.CURRENT, customLogFile);
        } else {
            //switch to default

            //move the current back to custom
            config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM, currentLogFile);


            //move the default to current
            config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.CURRENT, defaultLogFile);
        }

    }

    /**
     * Method to switch the log directly to the desired one
     * @param _logFileType enum LOG_FILE_TYPE representing the log file type which shall be switched to
     */
    public void switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE _logFileType){
        config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.CURRENT,
                config.getLogByType(_logFileType));
    }

    /**
     * Create a new logfile for logfile writing
     * @param _fileName string representing the filename which shall be created for later use
     */
    public void useCustomLog(String _fileName){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT) ||
                config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.CONSOLE))
            return;

        Log custom = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM);
        if(custom == null || custom.getLogName().equals(_fileName) == false) {
            //there is no custom file yet,create one
            config.getLogs().put(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM, new Log(_fileName));

            custom = createLogFile(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM);
        }

        //let's check what the current file is
        Log current = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        if (custom.getLogName().equals(current.getLogName()) == false) {
            //the custom logfile exits but is not the current one, switch!
            toggleLog();
        }
    }

    /**
     * Method to create the logfolder if it does not exists yet
     */
    private void createLogFolder(){
        if(config.getToolPath() != null && config.getToolPath().isEmpty() == false) {
            Path file = Paths.get(config.getToolPath());
            if (Files.exists(file) == false) {
                try {
                    Files.createDirectories(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Method to write the content which has been collected, to the current.
     */
    public void writeToFile(){
        writeToFile(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);
    }

    /**
     * Method to write the content which has been collected to the file
     * @param _logFile enum LOG_FILE_TYPE representing the log file which shall be written to
     */
    public void writeToFile(PrettyPrintConst.LOG_FILE_TYPE _logFile){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.ALL) ||
                config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.FILE)) {
            //get the file which needs to be written now
            Log log = config.getLogByType(_logFile);
            LogFile file = log.getLogFile();

            try {
                //write the content to the file
                Files.write(file.getFile(), log.getContentToWrite(), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //after we wrote the stuff to the file, we have to clear it
            config.getLogByType(_logFile).getContentToWrite().clear();
        }
    }

    /**
     * Method to calculate the new length of the string based on the color codes which has been added
     * @param _text
     * @return
     */
    private int calculateSpace(String _text) {
        int ansiCodeLength = config.getWidth() - config.getPadding() - _text.length();
        if (_text.contains("\u001B[")){
            int count = StringUtils.countMatches(_text,"\u001B[");
            ansiCodeLength += count*5-1;
            if(count > 2) {
                //security drop as the system behaves weird if we use more then one color coding
                ansiCodeLength--;
            }
        }
        return (ansiCodeLength);
    }

    /**
     * Method to add specific formated text to the contentArray of a logfile
     * @param _textElement
     */
    private void prepareContentForWriteToFile(TextElement _textElement){
        //check if there is the
        Log logToWrite = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        //check if fileToWrite is not null
        if(logToWrite != null) {
            //split the textLength if it is to long
            List<String> textLinesForFile = prepareArray(_textElement.getText()); //we don't need coloring here, so just give it the text

            for(String line : textLinesForFile) {
                //format the textLength that it has the boxes same as the console output
                String preparedContent = prepareContentString(line);

                //add the line to the list for later writeout
                logToWrite.getContentToWrite().add(preparedContent);
            }
        }
    }

    /**
     * Method to build a string line of a text element for the console
     * @param _textElement
     * @return
     */
    private String buildTextLineForConsole(TextElement _textElement){
        String textForConsole = "";
        switch (_textElement.getTextType()) {
            case NORMAL:
                textForConsole = textForConsole.concat(_textElement.getText());
                break;
            case COLOR:
                textForConsole = textForConsole.concat(_textElement.getColor().getCode() + _textElement.getText() + PrettyPrintConst.COLOR.ANSI_RESET.getCode());
                break;
            case BACKGROUND:
                textForConsole = textForConsole.concat(_textElement.getBackground().getCode() + _textElement.getText() +
                        PrettyPrintConst.BACKGROUND.ANSI_DEFAULT_BACKGROUND.getCode());
                break;
            case BOTH:
                textForConsole = textForConsole.concat(_textElement.getColor().getCode() + _textElement.getBackground().getCode() + _textElement.getText() +
                        PrettyPrintConst.BACKGROUND.ANSI_DEFAULT_BACKGROUND.getCode() + PrettyPrintConst.COLOR.ANSI_RESET.getCode());
                break;
        }

        return textForConsole;
    }


    /**
     * Method to return a formatted string, based on the configuration and the text which is supposed to be added to this line
     * @param _content
     * @return
     */
    private String prepareContentString (String _content) {
        return String.format("%s%s%s%s%s", "|", StringUtils.repeat(" ", config.getPadding()), _content, StringUtils.repeat(" ",calculateSpace(_content)), "|");
    }

    /**
     * Method to split texts which are to long.
     * @param _text
     * @return
     */
    private List<String> prepareArray(String _text){
        List<String> textLines = new ArrayList<>();

        if(_text.length() >= config.getWidth() ){
            //this textLength is to long for the output, we have to writ it in 2 lines
            textLines = createArray(_text);
        } else {
            textLines.add(_text);
        }
        return textLines;
    }

    /**
     * Create a string array out of a textLength based on the allowed length
     * @param _text
     * @return
     */
    private List<String> createArray(String _text) {
        PrettyPrintConst.COLOR savedColor = null;
        List<String> textLines = new ArrayList<>();

        String stringToCut = _text;

        while(stringToCut.length()>0){
            //find the first space before the end of the area
            int indexSpaceCloseToText = (stringToCut.length()< config.getTextLength())? stringToCut.length() : findSpaceCloseToEnd(stringToCut);
            //extract the element for that index
            String newElement = stringToCut.substring(0, indexSpaceCloseToText);

            if(savedColor != null) {
                //savedColor is not null, so we have to add the color code to the new element infront
                newElement = savedColor.getCode().concat(newElement);
                savedColor = null;
            }

            //check if the element contains a color opening, but no ending
            PrettyPrintConst.COLOR foundColor = hasUnclosedColor(newElement);

            if (foundColor != null) {
                //a unclosed color has been found, presave it and close it for this line!
                newElement = newElement.concat(PrettyPrintConst.COLOR.ANSI_RESET.getCode());
                savedColor = foundColor;
            }


            //add the element to the array
            textLines.add(newElement);
            //incerement the index to address on the array
            //remove the saved element from the string to check
            stringToCut = stringToCut.substring(indexSpaceCloseToText, stringToCut.length());
        }

        return textLines;
    }

    private PrettyPrintConst.COLOR hasUnclosedColor(String _newElement){
        //check the string if there is a color opening
        int indexColorOpener = _newElement.lastIndexOf("\u001B[3");
        int indexColorCloser = _newElement.lastIndexOf(PrettyPrintConst.COLOR.ANSI_RESET.getCode());

        if(indexColorOpener > indexColorCloser) {
            //there is a color which is not closed, we have to find out which one
            String colorString = _newElement.substring(indexColorOpener, indexColorOpener+5);
            return PrettyPrintConst.getColorByCode(colorString);
        }
        return null;
    }

    /**
     * Method to check for the first space before the end of the textLength area
     * @param _text
     * @return
     */
    private int findSpaceCloseToEnd(String _text) {
        boolean spaceFound = false;
        int startPoint = config.getTextLength();
        while( spaceFound == false && startPoint > 0) {
            if(_text.substring(startPoint).startsWith(" ") || _text.substring(startPoint).startsWith("-")){
                return startPoint;
            } else {
                startPoint--;
            }
        }

        return config.getTextLength();
    }

    /**
     * Method to write the text info to the console
     * @param _textElement
     */
    private void writeToConsole(TextElement _textElement){
        List<String> textLines = prepareArray(buildTextLineForConsole(_textElement));

        for (String line : textLines) {
            System.out.println(prepareContentString(line));
        }
    }

    /**
     * Method to simply handle a text which shall be send to the console or the logfile
     * @param _textElement Element which contains information how to show the text element
     */
    private void parseText(TextElement _textElement) {
        switch(config.getPrintType()){
            case ALL:
                prepareContentForWriteToFile(_textElement);
                writeToConsole(_textElement);
                break;
            case FILE:
                prepareContentForWriteToFile(_textElement);
                break;
            case CONSOLE:
                writeToConsole(_textElement);
                break;
            case SILENT:
            default:
                break;
        }
    }

    /**
     * Method to simply handle a text which shall be send to the console or the logfile
     * @param _text String which represents the text
     */
    public void parseText(String _text){
        parseText(new TextElement(_text));
    }

    /**
     * Method to simply handle a text which shall be send to the console with a text color or the logfile
     * @param _text String which represents the text
     * @param _textColor enum COLOR representing the color of the text
     */
    public void parseText(String _text, PrettyPrintConst.COLOR _textColor){
        parseText(new TextElement(_text, _textColor));
    }

    /**
     * Method to simply handle a text which shall be send to the console with background color or the logfile
     * @param _text String which represents the text
     * @param _backgroundColor enum BACKGROUND representing the background color of the text
     */
    public void parseText(String _text, PrettyPrintConst.BACKGROUND _backgroundColor){
        parseText(new TextElement(_text, _backgroundColor));
    }

    /**
     * Method to simply handle a text which shall be send to the console with background and text color or the logfile
     * @param _text String which represents the text
     * @param _textColor enum COLOR representing the color of the text
     * @param _backgroundColor enum BACKGROUND representing the background color of the text
     */
    public void parseText(String _text, PrettyPrintConst.COLOR _textColor, PrettyPrintConst.BACKGROUND _backgroundColor){
        parseText(new TextElement(_text, _textColor, _backgroundColor));
    }

    /**
     * Method to simply handle a ui element which shall be send to the console or the logfile
     * this method is for printing the borders and everything else which should never have color coding
     * @param _textElement TextElement object representing a text to parse
     */
    public void parseUiElement(TextElement _textElement) {
        switch(config.getPrintType()){
            case ALL:
                prepareUiForWriteToFile(_textElement);
                System.out.println(_textElement.getText());
                break;
            case FILE:
                prepareUiForWriteToFile(_textElement);
                break;
            case CONSOLE:
                System.out.println(_textElement.getText());
                break;
            case SILENT:
            default:
                break;
        }
    }

    /**
     * Method to add specific formated text to the contentArray of a logfile
     * @param _textElement
     */
    private void prepareUiForWriteToFile(TextElement _textElement){
        //check if there is the
        Log logToWrite = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        //check if fileToWrite is not null
        if(logToWrite != null) {
                //add the line to the list for later writeout
                logToWrite.getContentToWrite().add(_textElement.getText());

        }
    }

    /**
     * Method to add a TextElement to the Queue
     * @param _textElement text as text element object
     */
    private void queueText(TextElement _textElement){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        Log fileToWrite = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        fileToWrite.getQueue().add(_textElement);
    }

    /**
     * Method to add a Text to the Queue
     * @param _text text as string object
     */
    public void queueText(String _text){
        queueText(new TextElement(_text));
    }

    /**
     * Method to add a Text to the Queue with a text color
     * @param _text text as string object
     * @param _textColor enum COLOR representing the color of the text
     */
    public void queueText(String _text, PrettyPrintConst.COLOR _textColor){
        queueText(new TextElement(_text, _textColor));
    }

    /**
     * Method to add a Text to the Queue with a background color
     * @param _text text as string object
     * @param _backgroundColor enum BACKGROUND representing the background color of the text
     */
    public void queueText(String _text, PrettyPrintConst.BACKGROUND _backgroundColor){
        queueText(new TextElement(_text, _backgroundColor));
    }

    /**
     * Method to add a Text to the Queue with a text and background color
     * @param _text string represneting the text
     * @param _textColor enum COLOR representing the color of the text
     * @param _backgroundColor enum BACKGROUND representing the background color of the text
     *
     */
    public void queueText(String _text, PrettyPrintConst.COLOR _textColor, PrettyPrintConst.BACKGROUND _backgroundColor){
        queueText(new TextElement(_text, _textColor, _backgroundColor));
    }

    /**
     * Method to clear the queue after it has been printed
     */
    private void clearQueue(){
        Log fileToWrite = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        fileToWrite.getQueue().clear();
    }

    /**
     * Method to print the queue, which has been filled before
     */
    public void parseQueue(){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        Log fileToWrite = config.getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT);

        String textForConsole = "";
        String textForFile = "";
        for (TextElement textPiece : fileToWrite.getQueue()) {

            //for the file, we don't need to check the color coding
            textForConsole = textForConsole.concat(buildTextLineForConsole(textPiece));
            textForFile = textForFile.concat(textPiece.getText());
        }

        //create a dummy textelement to forward it to parseText
        writeToConsole(new TextElement(textForConsole));

        //write to logfile
        if (config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.CONSOLE) == false) {
            prepareContentForWriteToFile(new TextElement(textForFile));
        }

        //clear the queue
        clearQueue();
    }

    /**
     * Method to parse the start of a box
     */
    public void parseBoxStart(){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        String line = "";
        line += "┌" + createFiller() + "┐";
        parseUiElement(new TextElement(line));
    }

    /**
     * Method for parse a seperator in the correct width
     */
    public void parseSeperator(){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        String line = "";
        line += "├" + createFiller() + "┤";
        parseUiElement(new TextElement(line));
    }

    /**
     * Method to parse a headline
     */
    public void parseHeadline(){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        String line = "";
        line += "├" + createFiller() + "┤";
        parseUiElement(new TextElement(line));
    }

    /**
     * Method to parse the end of a box
     */
    public void parseBoxEnd(){
        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT))
            return;

        String line = "";
        line += "└" + createFiller() + "┘";
        parseUiElement(new TextElement(line));
    }

    /**
     * Method to parse the filler lines
     */
    private String createFiller(){
        String line = "";
        for(int i = 0; i < config.getWidth(); i++ ){
            line += "-";
        }
        return line;
    }


    // OLD BULLSHIT STUFF WE HAVE TO GET RID OF BELLOW!
}
