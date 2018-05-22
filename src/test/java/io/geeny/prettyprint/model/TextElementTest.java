package io.geeny.prettyprint.model;

import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by marcobierbach on 15.02.18.
 */
public class TextElementTest {

    @Test
    public void TestTextElement(){
        TextElement newElement = new TextElement("Test");
        assertEquals("Test", newElement.getText());
    }

    @Test
    public void TestTextElementWithBackgroundColor(){
        TextElement newElement = new TextElement("Test", PrettyPrintConst.BACKGROUND.ANSI_BLUE_BACKGROUND);
        assertEquals("Test", newElement.getText());
        assertEquals(PrettyPrintConst.BACKGROUND.ANSI_BLUE_BACKGROUND, newElement.getBackground());
    }

    @Test
    public void TestTextElementWithColorAndBackgroundColor(){
        TextElement newElement = new TextElement("Test", PrettyPrintConst.COLOR.ANSI_GREEN, PrettyPrintConst.BACKGROUND.ANSI_BLUE_BACKGROUND);
        assertEquals("Test", newElement.getText());
        assertEquals(PrettyPrintConst.COLOR.ANSI_GREEN, newElement.getColor());
        assertEquals(PrettyPrintConst.BACKGROUND.ANSI_BLUE_BACKGROUND, newElement.getBackground());
    }

}
