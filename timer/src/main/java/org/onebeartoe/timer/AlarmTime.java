
package org.onebeartoe.timer;

/**
 * Filename AlarmTime.java
 * Written by Roberto H. Marquez
 * Created Sept. 18, 2003
*/
public class AlarmTime 
{

    private int MINS_MAX = 99; // The alarm can be set to go off this many minutes after beiong set.
    private int SECS_MAX = 60;  // not so duh!
//    private int SECS_MAX = 59;  // duh!

   private int secs;
   private int mins;

   public AlarmTime() 
   {
      setSeconds(0);
      setMinutes(0);
   }

   public AlarmTime(int seconds, int minutes) 
   {
      setSeconds(seconds);
      setMinutes(minutes);
   }

   public int getSeconds() 
   {
      return secs;
   }

   public int getMinutes() {
      return mins;
   }

   public void setSeconds(int s)	{
      secs = (s >= 0 && s < SECS_MAX) ? s : 0;
   }

   public void setMinutes(int m) 
   {
      mins = (m >= 0 && m < MINS_MAX) ? m : 0;
   }

   public void incrSeconds() 
   {
      secs++;
      if( secs > SECS_MAX) 
      {
         secs = 0;
         if( mins == MINS_MAX)
            mins = 0;
         else
            incrMinutes();
      }
   }

   public void incrMinutes() 
   {
      if( mins < MINS_MAX) 
      {
         mins++;
      }
   }

   public void decrSeconds() 
   {
      secs--;
      if( secs < 0) {
         secs = SECS_MAX;
         decrMinutes();
      }
   }

   public void decrMinutes() 
   {
      if( mins > 0) 
      {
         mins--;
      }
   }

}
