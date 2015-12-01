
package org.onebeartoe.chronos.digital.clock.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.io.TextFileReader;
import org.onebeartoe.io.TextFileWriter;
import org.onebeartoe.network.http.file.transfer.DynamicFileHttpHandler;

/**
 * @author Roberto Marquez
 */
public class WebContentHttpHandler extends DynamicFileHttpHandler
{
    private List<String> webContentClasspaths;
    
    private TextFileReader textFileReader;
    
    private TextFileWriter textFileWriter;
    
    private final String subpath = "/web-content/";
    
    public WebContentHttpHandler()
    {
        textFileReader = new TextFileReader();
        
        textFileWriter = new TextFileWriter();
        
        buildClasspaths();
        
        extractWebContent();
    }
    
    private void buildClasspaths()
    {
        webContentClasspaths = new ArrayList();                
        
        webContentClasspaths.add(subpath + "index.html");
    }
    
    private void extract(String classpath)
    {
        try
        {
            String text = textFileReader.readTextFromClasspath(classpath);
            
            String outpath = getRootPath() + classpath;
            File outfile = new File(outpath);
            
            outfile.getParentFile().mkdirs();
            
            textFileWriter.writeText(outfile, text);
        }
        catch (IOException ex)
        {
            String message = "could not extract file from classpath: " + classpath;
            
            logger.log(Level.SEVERE, message, ex);
        }
    }
    
    private void extractWebContent()
    {
        File outdir = new File( getRootPath() );
        outdir.mkdirs();
        
        if( ! outdir.exists() )
        {
            logger.log(Level.SEVERE, "could not create directory for Web content");
        }
        else
        {
            webContentClasspaths.stream()
                    .forEach( s -> extract(s) );
        }
    }
    
    @Override
    protected String getRootPath()
    {
        String userHome = System.getProperty("user.home");
        
        String resourcePath = userHome + "/.onebeartoe/chronos/digital-clock" + subpath;
        
        return resourcePath;
    }
}
