package io.geeny.prettyprint.model;

import io.geeny.prettyprint.util.PrettyPrintConst;

/**
 * Model Object for a text element for pretty print
 * @version 1.0
 * @author Marco Bierbach and Klemen Samsa
 */
public class TextElement {

    private PrettyPrintConst.TEXT_COLOR_TYPE textType;
    private String text;
    private PrettyPrintConst.COLOR color;
    private PrettyPrintConst.BACKGROUND background;

    /**
     * Constructor for an element which has no color at all.
     * @param _text string
     */
    public TextElement(String _text) {
        text = _text;
        textType = PrettyPrintConst.TEXT_COLOR_TYPE.NORMAL;
    }

    /**
     * Constructor for an element which has colored text
     * @param _text string, text to print
     * @param _color enum COLOR color to use for the text
     */
    public TextElement(String _text, PrettyPrintConst.COLOR _color){
        text = _text;
        color = _color;
        textType = PrettyPrintConst.TEXT_COLOR_TYPE.COLOR;
    }

    /**
     * Constructor for an element which has colored background
     * @param _text string, text to print
     * @param _background enum BACKGROUND background color to use
     */
    public TextElement(String _text, PrettyPrintConst.BACKGROUND _background){
        text = _text;
        background = _background;
        textType = PrettyPrintConst.TEXT_COLOR_TYPE.BACKGROUND;
    }

    /**
     * Constructor for an element which has colored background and text
     * @param _text string, text to print
     * @param _color enum COLOR color to use for the text
     * @param _background enum BACKGROUND background color to use
     */
    public TextElement(String _text, PrettyPrintConst.COLOR _color, PrettyPrintConst.BACKGROUND _background){
        text = _text;
        color = _color;
        background = _background;
        textType = PrettyPrintConst.TEXT_COLOR_TYPE.BOTH;
    }

    /**
     * Getter method for the text of the queued element
     * @return string text
     */
    public String getText() {
        return text;
    }

    /**
     * Getter method for the color of the text of the queued element
     * @return enum COLOR representing the color of the text
     */
    public PrettyPrintConst.COLOR getColor() {
        return color;
    }

    /**
     * Getter method for the background color of the queued element
     * @return enum BACKGROUND representing the background color of the text
     */
    public PrettyPrintConst.BACKGROUND getBackground() {
        return background;
    }

    /**
     * Getter method for the textType of the queueed element
     * @return enum TEXT_COLOR_TYPE representing the color type the text has
     */
    public PrettyPrintConst.TEXT_COLOR_TYPE getTextType() {
        return textType;
    }
}
