/**
 * This application is a modification of a sample JavaFX program that comes with 
 * Netbeans IDE 8.0.
 * 
 * Further modification made by Roberto Marquez - http://www.onebeartoe.com
 */

/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.onebeartoe.chronos.digital.clock;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.onebeartoe.chronos.digital.clock.network.ChangeColorHttpHandler;
import org.onebeartoe.chronos.digital.clock.network.WebContentHttpHandler;

/**
 * A digital clock application that demonstrates JavaFX animation, images, and
 * effects.
 *
 * @see javafx.scene.effect.Glow
 * @see javafx.scene.shape.Polygon
 * @see javafx.scene.transform.Shear
 * @resource DigitalClock-background.png
 */
public class DigitalClock extends Application 
{
    private Group root;
    
    private Clock clock;
    
    private Color onColor;
    
    private static final String ON_COLOR_KEY = "onColor";
    
    private static final String HOUR_OFFSET = "hourOffset";
    
    private int hourOffset;
    
    private HttpServer server;
    
    private final int httpPort = 8080;

    private List<Circle> dots;
    
    @Override
    public void init()
    {        
        initializeParameters();

        initializeServer();
    }
    
    private void initializeParameters()
    {
        // command line parameters are passed in the form '--name=value'
        Parameters parameters = getParameters();
        Map<String, String> namedParams = parameters.getNamed();
        
        String onColorValue = namedParams.get(ON_COLOR_KEY);
        if(onColorValue == null)
        {
            onColor = Color.ORANGERED;
        }
        else
        {
            onColor = Color.valueOf(onColorValue);
        }

        String s = namedParams.get(HOUR_OFFSET);
        
        if(s == null)
        {
            hourOffset = 0;
        }
        else
        {
            hourOffset = Integer.valueOf(s);
        }
    }

    private void initializeServer() //throws IOException
    {
        InetSocketAddress anyhost = new InetSocketAddress(httpPort);
        try
        {
            server = HttpServer.create(anyhost, 0);
            
            HttpHandler webContentHttpHandler = new WebContentHttpHandler();
            HttpHandler changeColorHttpHandler = new ChangeColorHttpHandler(this);

            server.createContext("/", webContentHttpHandler);
            server.createContext("/color", changeColorHttpHandler);
            
            server.start();
        }
        catch (IOException ex)
        {
            Logger.getLogger(DigitalClock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * The main() method is ignored in correctly deployed JavaFX 
     * application. main() serves only as fallback in case the 
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    public void setOnColor(Color newColor)
    {
        clock.setOnColor(newColor);
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Digital Clock");
        root = new Group();
        Scene scene = new Scene(root, 880, 812);
        
        // background image
        InputStream instream = getClass().getResourceAsStream("DigitalClock-background.png");
        Image image = new Image(instream);
        ImageView background = new ImageView(image);
        background.getTransforms().add(new Scale(1.5f, 1.5f, 0, 0));
        
        // digital clock
        clock = new Clock(onColor, Color.rgb(50,50,50));
        clock.setLayoutX(45);
        clock.setLayoutY(186);
        clock.getTransforms().add(new Scale(0.83f, 0.83f, 0, 0));
        
        // add background and clock to sample
        root.getChildren().addAll(background, clock);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void startAnimation()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

        	double dx = 7; //Step on x or velocity
        	double dy = 3; //Step on y
                
                Clock ball = clock;

            @Override
            public void handle(ActionEvent t) {
            	//move the ball
            	ball.setLayoutX(ball.getLayoutX() + dx);
            	ball.setLayoutY(ball.getLayoutY() + dy);

                Bounds bounds = root.getBoundsInLocal();

                double ballWidth = ball.getBoundsInParent().getWidth();
                double ballHeight = ball.getBoundsInParent().getHeight();
                
                //If the ball reaches the left or right border make the step negative
                if(ball.getLayoutX() <= (bounds.getMinX() + ballWidth) 
                   ||
                   ball.getLayoutX() >= (bounds.getMaxX() - ballWidth) )
                {
                	dx = -dx;
                }

                //If the ball reaches the bottom or top border make the step negative
                if((ball.getLayoutY() >= (bounds.getMaxY() - ballHeight)) 
                    ||
                    ball.getLayoutY() <= (bounds.getMinY() + ballHeight))
                {

                	dy = -dy;

                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();        
    }
    
    public void startAnimationWave()
    {
        Path path = new Path();
        path.getElements().add(new MoveTo(20,20));
        path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(clock);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }
    
    @Override
    public void stop()
    {
        clock.shutDown();
        
        System.out.println("stopping server");
        
        server.stop(httpPort);
        
        System.out.println("server stopped");  

        Platform.exit();
    }

    /**
     * Clock made of 6 of the Digit classes for hours, minutes and seconds.
     */
    public class Clock extends Parent 
    {
        private Calendar calendar = Calendar.getInstance();
        
        private Digit[] digits;
        
        private Timeline delayTimeline, secondTimeline;

        public Clock(Color onColor, Color offColor) 
        {
            // create effect for on LEDs
            Glow onEffect = new Glow(1.7f);
            onEffect.setInput(new InnerShadow());
            // create effect for on dot LEDs
            Glow onDotEffect = new Glow(1.7f);
            onDotEffect.setInput(new InnerShadow(5,Color.BLACK));
            // create effect for off LEDs
            InnerShadow offEffect = new InnerShadow();
            
            Circle c1 = new Circle(80 + 54 + 20, 44, 6, onColor);
            Circle c2 = new Circle(80 + 54 + 17, 64, 6, onColor);
            Circle c3 = new Circle((80 * 3) + 54 + 20, 44, 6, onColor);
            Circle c4 = new Circle((80 * 3) + 54 + 17, 64, 6, onColor);
 
            dots = new ArrayList();
            dots.add(c1);
            dots.add(c2);
            dots.add(c3);
            dots.add(c4);            
            
            // create digits
            digits = new Digit[7];
            
            for (int i = 0; i < 6; i++) 
            {
                Digit digit = new Digit(onColor, offColor, 
                                        onEffect, offEffect,
                                        dots);
                digit.setLayoutX(i * 80 + ((i + 1) % 2) * 20);
                digits[i] = digit;
                getChildren().add(digit);
            }
            
            // create dots
            Group dotsGroup = new Group(c1,
                                        c2,
                                        c3,
                                        c4);
            
            dotsGroup.setEffect(onDotEffect);
            
            getChildren().add(dotsGroup);
            
            // update digits to current time
            refreshClocks();
            
            // start timer to update every second
            play();
        }

        public void play() 
        {
            // wait till start of next second then start a timeline to call refreshClocks() every second
            delayTimeline = new Timeline();
            delayTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), new EventHandler<ActionEvent>() 
                {
                    @Override public void handle(ActionEvent event) 
                    {
                        if (secondTimeline != null) {
                            secondTimeline.stop();
                        }
                        secondTimeline = new Timeline();
                        secondTimeline.setCycleCount(Timeline.INDEFINITE);
                        secondTimeline.getKeyFrames().add(
                                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() 
                                {
                                    @Override public void handle(ActionEvent event) 
                                    {
                                        refreshClocks();
                                    }
                                }));
                        secondTimeline.play();
                    }
                })
            );
            delayTimeline.play();
        }        
        
        private void refreshClocks() 
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
            
//            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int hours = calendar.get(Calendar.HOUR);
            
            hours += DigitalClock.this.hourOffset;
            
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);
            digits[0].showNumber(hours / 10);
            digits[1].showNumber(hours % 10);
            digits[2].showNumber(minutes / 10);
            digits[3].showNumber(minutes % 10);
            digits[4].showNumber(seconds / 10);
            digits[5].showNumber(seconds % 10);
        }

        public void setOnColor(Color newColor)
        {
            for(Digit d : digits)
            {
                if(d != null)
                {
                    d.setOnColor(newColor);
                }
            }
        }
        
        public void shutDown()
        {
            delayTimeline.stop();
            secondTimeline.stop();
        }
        
        public void stop()
        {
            delayTimeline.stop();
            
            if (secondTimeline != null) 
            {
                secondTimeline.stop();
            }
        }
    }
}
