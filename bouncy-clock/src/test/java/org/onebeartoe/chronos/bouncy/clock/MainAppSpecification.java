
package org.onebeartoe.chronos.bouncy.clock;

import javafx.stage.Stage;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class MainAppSpecification
{
    MainApp implementation;
    
    @BeforeTest
    public void setup()
    {
        implementation = new MainApp();
    }
    
    @Test(expectedExceptions = ExceptionInInitializerError.class)
    public void start()
    {
        Stage stage = null;
//        Stage stage = new Stage();
        
        implementation.start(stage);
    }
}
