
package org.onebeartoe.chronos.digital.clock.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

import org.apache.ecs.html.Select;
import org.onebeartoe.application.Colors;

import org.onebeartoe.io.TextFileReader;
import org.onebeartoe.io.TextFileWriter;
import org.onebeartoe.io.buffered.BufferedTextFileReader;
import org.onebeartoe.network.http.file.transfer.DynamicFileHttpHandler;

/**
 * @author Roberto Marquez
 */
public class WebContentHttpHandler extends DynamicFileHttpHandler
{
    private List<String> webContentClasspaths;
    
    private BufferedTextFileReader textFileReader;
    
    private TextFileWriter textFileWriter;
    
    private final String subpath = "/web-content/";
    
    
    
    public WebContentHttpHandler()
    {
        textFileReader = new BufferedTextFileReader();
        
        textFileWriter = new TextFileWriter();
        
        // this lambda inserts a drowpdown with the build-in list of JavaFX colors
        TextReplacement colorDropdownReplacer = (key, original) -> 
        {
            String replacementText = "The colors replacment text is NOT set.";
            try 
            {
                replacementText = colorsDropdown();
            } 
            catch (IllegalAccessException ex) 
            {
                logger.severe("An error occured retrieving colors: " + ex.getMessage() );
            }
            
            String mapping = original.replaceAll(key, replacementText);
            
            return mapping;
        };
                
        String key = "REPLACE_ME_TEXT";
        addTextReplacer(key, colorDropdownReplacer);
        
        buildClasspaths();
        
        extractWebContent();
    }
    
    private void buildClasspaths()
    {
        webContentClasspaths = new ArrayList();                
        
        webContentClasspaths.add(subpath + "index.html");
        webContentClasspaths.add(subpath + "digital-clock.js");
    }
    
    private String colorsDropdown() throws IllegalAccessException
    {
        Map<String, Color> colorMap = Colors.list();
        Set<String> keySet = colorMap.keySet();
        String [] colorArray = keySet.toArray( new String[0] );
        
        List<String> sortedColorList = Arrays.stream(colorArray)
                                        .sorted()
                                        .collect( Collectors.toList() );
        
        String [] sortedColorArray = sortedColorList.toArray( new String[0] );
        
        String listName = "builtInColors";
                
        // API - http://www.johnquinn.com/doc/ecs/index.html
        Select dropdown = new Select(listName, sortedColorArray);
        
        dropdown.setOnChange("colorChanged(this.value);");
        
        return dropdown.toString();
    }
    /**
     * This method extracts the file named in the classpath parameter, but does not create
     * directories that it may have been under, in the JAR.
     * @param classpath 
     */
    private void extract(String classpath)
    {
        try
        {
            String text = textFileReader.readTextFromClasspath(classpath);
            
            int start = classpath.lastIndexOf("/");
            start = start == -1 ? 0 : start; // slash was not found in the classpath parameter
            String filename = classpath.substring(start);
            
            String outpath = getRootPath() + filename;
            File outfile = new File(outpath);
            
            outfile.getParentFile().mkdirs();
            
            logger.log(Level.INFO, "extracting: " + classpath);
            
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
