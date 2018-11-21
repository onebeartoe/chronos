
package org.onebeartoe.chronos.bouncy.clock;

import java.util.Date;
import static javafx.application.Application.launch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This bouncy code is partially inspired by this stackoverlfow answer:
 * 
 *      https://stackoverflow.com/questions/20022889/how-to-make-the-ball-bounce-off-the-walls-in-javafx
 * 
 * @author Roberto Marquez - https://github.com/onebeartoe
 */
public class MainApp extends Application 
{
    private static Label circle;
   
    public static Pane canvas;
    
    @Override
    public void start(final Stage primaryStage) 
    {
        Date now = new Date();
        String time = now.toString();
        circle = new Label(time);
        
        canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Time App");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        circle.relocate(100, 100);

        canvas.getChildren().addAll(circle);

        // Thi Timeline object moves the position of the label every 50 milliseconds.
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() 
        {

            double deltaX = 6;
            double deltaY = 3;

            @Override
            public void handle(final ActionEvent t) 
            {
                circle.setLayoutX(circle.getLayoutX() + deltaX);
                circle.setLayoutY(circle.getLayoutY() + deltaY);

                final Bounds bounds = canvas.getBoundsInLocal();
                
                final boolean atRightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getBoundsInLocal().getMaxX());
                final boolean atLeftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getBoundsInLocal().getMinX() );
                final boolean atBottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getBoundsInLocal().getMaxY() );
                final boolean atTopBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getBoundsInLocal().getHeight() );

                if (atRightBorder || atLeftBorder) 
                {
                    deltaX *= -1;
                }
                
                if (atBottomBorder || atTopBorder) 
                {
                    deltaY *= -1;
                }
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();        
        
        // this timeline object is responsible for updating the lable text with the current time every second.
        final Timeline timeTimeline = new Timeline( new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(final ActionEvent t) 
            {
                Date now = new Date();
                String currentTime = now.toString();
                circle.setText(currentTime);
            }
        }));
        timeTimeline.setCycleCount(Timeline.INDEFINITE);
        timeTimeline.play();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
