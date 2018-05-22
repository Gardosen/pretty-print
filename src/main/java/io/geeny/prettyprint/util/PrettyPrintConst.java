package io.geeny.prettyprint.util;

/**
 * Constant for common use
 * @author Marco Bierbach and Klemen Samsa
 * @version 1.0
 */

public class PrettyPrintConst {

    public static final String DEFAULT_LOG_FILE_NAME = "info";

    public enum TEXT_COLOR_TYPE {
        NORMAL,
        COLOR,
        BACKGROUND,
        BOTH
    }

    public enum PRINT_TYPE {
        SILENT,
        CONSOLE,
        FILE,
        ALL
    }

    public enum LOG_FILE_TYPE {
        DEFAULT,
        CURRENT,
        CUSTOM
    }

    public enum COLOR {

        ANSI_RESET("\u001B[0m"),
        ANSI_BLACK("\u001B[30m"),
        ANSI_RED("\u001B[31m"),
        ANSI_GREEN("\u001B[32m"),
        ANSI_YELLOW("\u001B[33m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_PURPLE("\u001B[35m"),
        ANSI_CYAN("\u001B[36m"),
        ANSI_WHITE("\u001B[37m");

        private String code;

        COLOR(String _code){
            code = _code;
        }

        public String getCode(){
            return code;
        }

    }

    /**
     * Method to get the COLOR enum object by the string value
     * @param _colorCode string representing the color ascii code we look for
     * @return enum COLOR representing the found color based on the color code
     */
    public static COLOR getColorByCode(String _colorCode){
        for(COLOR color : COLOR.values()){
            if(color.getCode().equals(_colorCode)){
                return color;
            }
        }
        return null;
    }

    public enum BACKGROUND {

        ANSI_DEFAULT_BACKGROUND("\u001B[49m"),
       ANSI_BLACK_BACKGROUND("\u001B[40m"),
       ANSI_RED_BACKGROUND("\u001B[41m"),
       ANSI_GREEN_BACKGROUND("\u001B[42m"),
       ANSI_YELLOW_BACKGROUND("\u001B[43m"),
       ANSI_BLUE_BACKGROUND("\u001B[44m"),
       ANSI_PURPLE_BACKGROUND("\u001B[45m"),
       ANSI_CYAN_BACKGROUND("\u001B[46m"),
       ANSI_WHITE_BACKGROUND("\u001B[47m");

        private String code;

        BACKGROUND(String _code){
            code = _code;
        }

        public String getCode(){
            return code;
        }
    }

    public static BACKGROUND getBackgroundByCode(String _code){
        for(BACKGROUND background : BACKGROUND.values()){
            if(background.getCode().equals(_code)){
                return background;
            }
        }
        return null;
    }

    public static String SEPERATOR = System.getProperty("line.separator");

}
