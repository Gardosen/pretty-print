package io.geeny.prettyprint.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the content which needs to be written to the console and/or the log file, handling the queue
 *
 * @author Marco Bierbach
 * @version 1.0
 */
public class Log {
    private String logName;
    private LogFile logFile;
    private List<TextElement> queue = new ArrayList<>();
    private List<String> contentToWrite = new ArrayList<>();

    public Log(String _logName){
        logName = _logName;
    }

    /**
     * Getter method for the logname
     * @return returns the name of the log object
     */
    public String getLogName(){
        return logName;
    }

    /**
     * Getter method for the logfile
     * @return returns the logfile
     */
    public LogFile getLogFile(){
        return logFile;
    }

    /**
     * Setter method for the logfile
     * @param _logFile logfile which needs to be saved
     */
    public void setLogFile(LogFile _logFile){
        logFile = _logFile;
    }

    /**
     * Getter method for the queue
     * @return returns the queue of text elements for printing it either to the console and/or logfile
     */
    public List<TextElement> getQueue() {
        return queue;
    }

    /**
     * Getter method for the content list
     * @return returns the content to write to a log file
     */
    public List<String> getContentToWrite() {
        return contentToWrite;
    }

}
