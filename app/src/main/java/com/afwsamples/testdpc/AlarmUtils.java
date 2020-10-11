package com.afwsamples.testdpc;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

public class AlarmUtils {

    public static final String ACTION_NONIDLE_REPEAT_EXACT = "ACTION_NONIDLE_REPEAT_EXACT";
    public static final String ACTION_NONIDLE_REPEAT_INEXACT = "ACTION_NONIDLE_REPEAT_INEXACT";
    public static final String ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY = "ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY";
    public static final String ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY = "ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY";
    public static final String ACTION_IDLE_REPEAT_EXACT = "ACTION_IDLE_REPEAT_EXACT";
    public static final String ACTION_IDLE_REPEAT_INEXACT = "ACTION_IDLE_REPEAT_INEXACT";

    private static final String TAG = "AlarmsUtils";
    private static AlarmUtils instance = null;

    public static AlarmUtils getInstance() {
        if (null == instance)
        {
            instance = new AlarmUtils();
            return instance;
        }
        return instance;
    }

    public  static void setAlarm(AlarmManager am, Context context, Boolean repeat, Boolean exact, Boolean idleAware, int interval)
    {

        int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

        if (idleAware) {
            // set the alarm which fires while device is in Doze mode
            if (repeat) {
                if (exact) {
                    Log.i(TAG, "Alarm Setting: ACTION_IDLE_REPEAT_EXACT " + interval);
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_IDLE_REPEAT_EXACT,interval));
                } else { // non exact alarm
                    Log.i(TAG, "Alarm Setting: ACTION_IDLE_REPEAT_INEXACT " + interval);
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_IDLE_REPEAT_INEXACT,interval));

                }
            } else { // not repeat alarm
                if (exact) {

                } else { // non exact alarm

                }
            }
        }else{// non idleaware alarms
            if (repeat) {
                if (interval >= 60000) {
                    if (exact) {
                        Log.i(TAG, "Alarm Setting: ACTION_NONIDLE_REPEAT_EXACT " + interval);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_NONIDLE_REPEAT_EXACT,interval));
                    } else { // non exact alarm
                        Log.i(TAG, "Alarm Setting: ACTION_NONIDLE_REPEAT_INEXACT " + interval);
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_NONIDLE_REPEAT_INEXACT,interval));
                    }
                }
                else{
                    if (exact) {
                        Log.i(TAG, "Alarm Setting: ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY " + interval);
                        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY,interval));
                    } else { // non exact alarm
                        Log.i(TAG, "Alarm Setting: ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY " + interval);
                        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ interval, MyAlarmHandler.getAlarmPendingIntent(context, ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY,interval));
                    }

                }
            } else { // not repeat alarm
                if (exact) {

                } else { // non exact alarm
                }
            }
        }
    }

}