package io.geeny.prettyprint.model;

import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Model Class for the LogFile which will be used to save information
 *
 * @author Marco Bierbach
 * @version 1.0
 */
public class LogFile {
    private String fileName;
    private Path file;

    /**
     * Default Constructor to define the standard info log file
     * @param _fileName string representing the filename for the log file
     */
    public LogFile(String _fileName) {
        fileName = _fileName;
        createFile();
    }

    /**
     * Method to create the log file
     * @throws IOException
     */
    private void createFile() {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_").format(new Date());

        fileName = timeStamp + getFileName() + ".log";
        String path = Optional.ofNullable(PrettyPrint.instance.getConfig().getToolPath()).orElse("");
        file = Paths.get(path + fileName);

        if (Files.exists(file) == false) {
            try {
                Files.createFile(file);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    /**
     * Getter method for the fileName
     * @return returns the file name of the log file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Getter method for the file
     * @return returns the path of the file for the logfile
     */
    public Path getFile() {
        return file;
    }

}
