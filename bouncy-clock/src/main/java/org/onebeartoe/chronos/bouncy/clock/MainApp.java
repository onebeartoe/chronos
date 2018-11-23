
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
import javafx.scene.text.Font;
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
    private static Label label;

    @Override
    public void start(final Stage primaryStage) 
    {
        Date now = new Date();
        String time = now.toString();
        label = new Label(time);
        label.relocate(100, 100);
        Font font = new Font(48);
        label.setFont(font);
        
        Pane canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Time App");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        canvas.getChildren().addAll(label);

        // Thi Timeline object moves the position of the label every 50 milliseconds.
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() 
        {
            double deltaX = 6;
            double deltaY = 3;

            @Override
            public void handle(final ActionEvent t) 
            {
                label.setLayoutX(label.getLayoutX() + deltaX);
                label.setLayoutY(label.getLayoutY() + deltaY);

                final Bounds bounds = canvas.getBoundsInLocal();
                
                final boolean atRightBorder = label.getLayoutX() >= (bounds.getMaxX() - label.getBoundsInLocal().getMaxX());
                final boolean atLeftBorder = label.getLayoutX() <= (bounds.getMinX() + label.getBoundsInLocal().getMinX() );
                final boolean atBottomBorder = label.getLayoutY() >= (bounds.getMaxY() - label.getBoundsInLocal().getMaxY() );
                final boolean atTopBorder = label.getLayoutY() <= (bounds.getMinY() + label.getBoundsInLocal().getHeight() );

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
                label.setText(currentTime);
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
