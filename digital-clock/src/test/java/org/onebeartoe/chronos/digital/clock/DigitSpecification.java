
package org.onebeartoe.chronos.digital.clock;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class DigitSpecification
{
    Digit implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        Circle c1 = new Circle(80 + 54 + 20, 44, 6, Color.ORANGERED);
 
        List<Circle> dots = new ArrayList();
        dots.add(c1);
        
        implementation = new Digit(null, null,
                                    null, null,
                                    dots);
    }       

    /**
     * Test of showNumber method, of class Digit.
     */
    @Test
    public void showNumber()
    {
        int currentNumber = 3;
        
        implementation.showNumber(currentNumber);
        
        int actual = implementation.currentNumber();
        
        int expected = 3;
        
        assertEquals(actual, expected);
    }
}
