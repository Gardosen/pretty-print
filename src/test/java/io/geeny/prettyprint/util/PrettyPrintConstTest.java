package io.geeny.prettyprint.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by marcobierbach on 15.02.18.
 */
public class PrettyPrintConstTest {

    @Test
    public void TestColorEnum(){
        Assert.assertEquals(PrettyPrintConst.COLOR.ANSI_BLACK, PrettyPrintConst.getColorByCode("\u001B[30m"));
        Assert.assertEquals(null, PrettyPrintConst.getColorByCode("test"));
    }

    @Test
    public void TestBackgroundEnum(){
        Assert.assertEquals(PrettyPrintConst.BACKGROUND.ANSI_BLACK_BACKGROUND, PrettyPrintConst.getBackgroundByCode("\u001B[40m"));
        Assert.assertEquals(null, PrettyPrintConst.getBackgroundByCode("test"));
    }

    @Test
    public void TestPrettyPrintConst(){
        PrettyPrintConst ppconst = new PrettyPrintConst();
        assertNotEquals(null, ppconst);
    }

}
