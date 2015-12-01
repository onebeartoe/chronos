
package org.onebeartoe.chronos.digital.clock.network;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import javafx.scene.paint.Color;
import org.onebeartoe.application.Colors;
import org.onebeartoe.network.TextHttpHandler;
import static sun.text.normalizer.Utility.hex;

/**
 * @author Roberto Marquez
 */
public class ChangeColorHttpHandler extends TextHttpHandler
{
    @Override
    protected String getHttpText(HttpExchange exchange)
    {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        int i = path.lastIndexOf("/") + 1;
        String colorName = path.substring(i);

        Color color = Colors.valueOf(colorName);
        

        
        Pixel pixel = application.getPixel();
        pixel.stopExistingTimer();
        pixel.setScrollTextColor(color);
        pixel.scrollText();
        
        return "scrolling text color update received:" + hex;
    }
}
