
package org.onebeartoe.timer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.onebeartoe.multimedia.AudioPlayer;

import org.onebeartoe.multimedia.SunAudioClipPlayer;

/**
 * @Author Roberto Marquez
*/
public class DesktopTimer extends JFrame implements ActionListener 
{
	public static final long serialVersionUID = 78798L;

	static 
        private String alarmSound = "/Door-Buzzer-SoundBible.com-1567875395.wav";
	
        private AlarmTime alarmTime;
	
        private Calendar timeToGoOff;

	private JLabel timeLbl;
        
	private JButton [] adjustBtns;
        
	private String [] adjustBtnsText = {"mins +", "mins - ", "secs +", "secs -"};
        
	private JButton startBtn;
        
        private JButton stopButton;
        
        private JButton optionsBtn;       

        private GUIupdaterTask uiUpdateTask;
        
	public DesktopTimer() 
	{
		super("onebeartoe Timer");

		alarmTime = new AlarmTime();
                
		timeLbl = new JLabel("00:00", SwingConstants.CENTER);
		timeLbl.setFont( new Font("TimesRoman",Font.BOLD,65) );

		adjustBtns = new JButton[adjustBtnsText.length];
		JPanel adjustPanel = new JPanel( new GridLayout(adjustBtnsText.length,1) );
		for(int x=0; x<adjustBtnsText.length; x++) 
		{
			adjustBtns[x] = new JButton(adjustBtnsText[x]);
			adjustBtns[x].addActionListener(this);
			adjustPanel.add(adjustBtns[x]);
		}

		startBtn = new JButton("Start");
		startBtn.addActionListener( new StartButtonListener() );
                
		optionsBtn = new JButton("Options");
                
                stopButton = new JButton("Stop");
                stopButton.addActionListener( new StopButtonListener() );
                
		JPanel buttonPanel = new JPanel( new GridLayout(1,2) );
		buttonPanel.add(startBtn);
                buttonPanel.add(stopButton);

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(timeLbl , BorderLayout.CENTER);
		cp.add(adjustPanel, BorderLayout.EAST);
		cp.add(buttonPanel, BorderLayout.SOUTH);

		setSize(375, 250);
		setLocation( 365, 360 );
		setVisible(true);               
	}

	@Override
        public void actionPerformed(ActionEvent e) 
	{
		if ( e.getSource() == adjustBtns[0] ) 
		{
			alarmTime.incrMinutes();
			updateAlarmTimeLbl();
		}
		else if( e.getSource() == adjustBtns[1] ) 
		{
			alarmTime.decrMinutes();
			updateAlarmTimeLbl();
		}
		else if( e.getSource() == adjustBtns[2] ) 
                {
			alarmTime.incrSeconds();
			updateAlarmTimeLbl();
		}
		else if( e.getSource() == adjustBtns[3] ) 
                {
			alarmTime.decrSeconds();
			updateAlarmTimeLbl();
		}
	}
        
        private class StopButtonListener implements ActionListener
        {           
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                startBtn.setEnabled(true);
                stopButton.setEnabled(false);
                alarmTime = timeLeft();
                
                uiUpdateTask.cancel();
            }        
        }
        
        private class StartButtonListener implements ActionListener
        {           
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                int milliseconds = alarmTime.getSeconds() * 1000;
                milliseconds += alarmTime.getMinutes() * 60 * 1000;                                                                

                timeToGoOff = Calendar.getInstance();
                timeToGoOff.setTimeInMillis(timeToGoOff.getTime().getTime() + milliseconds);                

                // GUI updates in 1 milliseconds and every second thereafter
                Timer timer = new Timer();
                uiUpdateTask = new GUIupdaterTask();
                timer.schedule(uiUpdateTask, 1, 1000);                  
                
                startBtn.setEnabled(false);
                System.out.println("Timer will go off in " + (milliseconds/1000) + " seconds.");
                
                stopButton.setEnabled(true);
            }        
        }

	public static void main( String [] args ) 
        {
		DesktopTimer app = new DesktopTimer();
		app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );                
	}             

	private void updateAlarmTimeLbl() 
        {
		StringBuilder time = new StringBuilder();
		if( alarmTime.getMinutes() < 10)
                {
			time.append('0');
                }
                time.append(alarmTime.getMinutes());
		time.append(':');
		if( alarmTime.getSeconds() < 10)
                {
                    time.append('0');
                }
		time.append(alarmTime.getSeconds());

		timeLbl.setText( time.toString() );
	}

	private void updateAlarmTimeLbl(int mins, int secs) 
	{
		StringBuilder time = new StringBuilder("");
		if( mins < 10)
                {
			time.append('0');
                }
                time.append(mins);
		time.append(':');
		if( secs < 10)
                {
                    time.append('0');
                }
		time.append(secs);

		timeLbl.setText( time.toString() );
	}

	private class GUIupdaterTask extends TimerTask 
        {
            @Override
            public void run() 
            {
                AlarmTime at = timeLeft();
                int mins = at.getMinutes();
                int secs = at.getSeconds();
                updateAlarmTimeLbl(mins, secs);
                if( mins == 0 && secs == 0 ) 
                {
                        cancel();
                        startBtn.setEnabled(true);                                
                        URL soundUrl = getClass().getResource(alarmSound);
                        AudioPlayer player = new SunAudioClipPlayer(soundUrl);
                        player.play();
                        
                        // reset the timer for the next use
                        alarmTime = new AlarmTime();
                }
            }
	}
        
        private AlarmTime timeLeft()
        {
            Date rightNow = new Date();
            long millisecondsLeft = timeToGoOff.getTime().getTime() - rightNow.getTime();
            long secondsLeft = millisecondsLeft / 1000;
            int mins = (int) secondsLeft / 60;
            int secs = (int) secondsLeft % 60;
            AlarmTime at = new AlarmTime();
            at.setMinutes(mins);
            at.setSeconds(secs);
            
            return at;
        }
}
