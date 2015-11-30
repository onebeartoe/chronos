
package org.onebeartoe.chronos.digital.clock.network;

import org.onebeartoe.network.http.file.transfer.DynamicFileHttpHandler;

/**
 * @author Roberto Marquez
 */
public class WebContentHttpHandler extends DynamicFileHttpHandler
{
    @Override
    protected String getRootPath()
    {
        String userHome = System.getProperty("user.home");
        
        String resourcePath = userHome + "/.onebeartoe/chronos/digital-clock/";
        
        return resourcePath;
    }
}
