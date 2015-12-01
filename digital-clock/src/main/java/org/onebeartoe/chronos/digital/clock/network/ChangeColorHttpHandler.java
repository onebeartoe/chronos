
package org.onebeartoe.chronos.digital.clock.network;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import javafx.scene.paint.Color;
import org.onebeartoe.application.Colors;
import org.onebeartoe.chronos.digital.clock.DigitalClock;
import org.onebeartoe.network.TextHttpHandler;

/**
 * @author Roberto Marquez
 */
public class ChangeColorHttpHandler extends TextHttpHandler
{
    private DigitalClock app;
    
    public ChangeColorHttpHandler(DigitalClock app)
    {
        this.app = app;
    }
    
    @Override
    protected String getHttpText(HttpExchange exchange)
    {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        int i = path.lastIndexOf("/") + 1;
        String colorName = path.substring(i);

        Color color = Colors.valueOf(colorName);
        
        app.setOnColor(color);

        return "clock on color update received:" + colorName;
    }
}
