package io.geeny.prettyprint.model;

import io.geeny.prettyprint.util.PrettyPrintConst;

import java.util.HashMap;
import java.util.Map;

/**
 * Config object file for pretty print 2.0
 * @author Marco Bierbach and Klemen Samsa
 * @version 1.0
 */
public class PrettyConfig {
    private int textLength;
    private int padding;
    private int width;
    private String toolPath = "";
    private PrettyPrintConst.PRINT_TYPE printType;
    private Map<PrettyPrintConst.LOG_FILE_TYPE, Log> logs = new HashMap<>();

    /**
     * Constructor for initializing the pretty print config
     * @param _textLength allowed length of the text
     * @param _padding padding to the left and right side
     * @param _printType what type is pretty print going to use
     */
    public PrettyConfig(int _textLength, int _padding, PrettyPrintConst.PRINT_TYPE _printType){
        textLength = _textLength;
        padding = _padding;
        width = textLength + (padding*2);
        printType = _printType;

        logs.put(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT, new Log(PrettyPrintConst.DEFAULT_LOG_FILE_NAME));
        logs.put(PrettyPrintConst.LOG_FILE_TYPE.CURRENT, getLogByType(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT));

        int i = 0;
    }

    /**
     * Getter method for the text length
     * @return text length as int
     */
    public int getTextLength() {
        return textLength;
    }

    /**
     * Getter method for the padding
     * @return padding as int
     */
    public int getPadding() {
        return padding;
    }

    /**
     * Getter method for the width
     * @return width as int
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter method for the tool patch
     * @return tool path as a string
     */
    public String getToolPath() {
        return toolPath;
    }

    /**
     * Setter method for the toolpath
     * @param toolPath string which represents the toolpath
     */
    public void setToolPath(String toolPath) {
        this.toolPath = toolPath;
    }

    /**
     * Getter method for the print type
     * @return enum PRINT_TYPE representing the current print type which is used by pretty print
     */
    public PrettyPrintConst.PRINT_TYPE getPrintType() {
        return printType;
    }

    /**
     * Getter method for the log objects
     * @return Map containing all Log object
     */
    public Map<PrettyPrintConst.LOG_FILE_TYPE, Log> getLogs() {
        return logs;
    }

    /**
     * Getter method for the logFile type by the enum
     * @param _logFileType enum representative which is supposed to be returned
     * @return log object which was found based on the parameter
     */
    public Log getLogByType(PrettyPrintConst.LOG_FILE_TYPE _logFileType){
        return logs.get(_logFileType);
    }

    /**
     * Getter method for the current log file type
     * @return enum definition which logfile is currently used
     */
    public PrettyPrintConst.LOG_FILE_TYPE getCurrentLogFileType() {
        if(getLogByType(PrettyPrintConst.LOG_FILE_TYPE.CURRENT).equals(getLogByType(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT))){
            return PrettyPrintConst.LOG_FILE_TYPE.DEFAULT;
        } else {
            return PrettyPrintConst.LOG_FILE_TYPE.CUSTOM;
        }
    }
}
