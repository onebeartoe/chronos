
package org.onebeartoe.chronos.bouncy.clock;

import javafx.application.Platform;
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
    
    @Test//(expectedExceptions = ExceptionInInitializerError.class)
    public void start()
    {
        Stage stage = null;
//        Stage stage = new Stage();

// Maybe try these links to test JavaFX classes
//
//      https://github.com/arnobl/torgen/blob/master/jfx-testing-testFX-simple/src/main/java/torgen/controller/SimpleController.java
//      https://github.com/TestFX/TestFX
//      http://torgen-engineering.blogspot.com/2015/11/testing-javafx-applications-with-testfx.html
//      headless
//          https://stackoverflow.com/questions/27403410/headless-testing-with-javafx-and-testfx


Thread t = new Thread(() ->
{
        Platform.runLater(() ->
        {
            implementation.start(stage);
        });    
}
);

t.setDaemon(true);
t.start();

        
    }
}
