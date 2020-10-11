package com.afwsamples.testdpc;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyAlarmHandler extends IntentService {

    private static final String TAG = "AlarmHandler";

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    public MyAlarmHandler() {
        super("MyAlarmHandler");
    }

    public static final String ACTION_NONIDLE_REPEAT_EXACT = "ACTION_NONIDLE_REPEAT_EXACT";
    public static final String ACTION_NONIDLE_REPEAT_INEXACT = "ACTION_NONIDLE_REPEAT_INEXACT";
    public static final String ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY = "ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY";
    public static final String ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY = "ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY";
    public static final String ACTION_IDLE_REPEAT_EXACT = "ACTION_IDLE_REPEAT_EXACT";
    public static final String ACTION_IDLE_REPEAT_INEXACT = "ACTION_IDLE_REPEAT_INEXACT";

    public static PendingIntent getAlarmPendingIntent(Context context, String action, int interval) {
        switch(action){
            case ACTION_NONIDLE_REPEAT_EXACT: {
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_NONIDLE_REPEAT_EXACT);
                return PendingIntent.getService(context,
                        0, intent, 0);
            }

            case ACTION_NONIDLE_REPEAT_INEXACT:{
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_NONIDLE_REPEAT_INEXACT);
                return PendingIntent.getService(context,
                        1, intent, 0);
            }
            case ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY:{
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY);
                intent.putExtra("Interval", interval);
                return PendingIntent.getService(context,
                        2, intent, 0);
            }
            case ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY:{
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY);
                intent.putExtra("Interval", interval);
                return PendingIntent.getService(context,
                        3, intent, 0);
            }
            case ACTION_IDLE_REPEAT_EXACT: {
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_IDLE_REPEAT_EXACT);
                intent.putExtra("Interval", interval);
                return PendingIntent.getService(context,
                        4, intent, 0);
            }

            case ACTION_IDLE_REPEAT_INEXACT:{
                Intent intent = new Intent(context, MyAlarmHandler.class);
                intent.setAction(MyAlarmHandler.ACTION_IDLE_REPEAT_INEXACT);
                intent.putExtra("Interval", interval);
                return PendingIntent.getService(context,
                        5, intent, 0);
            }


        }
        return null;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_NONIDLE_REPEAT_EXACT: {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_NONIDLE_REPEAT_EXACT " + theTime);

                }
                break;
                case ACTION_NONIDLE_REPEAT_INEXACT: {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_NONIDLE_REPEAT_INEXACT " + theTime);

                }
                break;
                case ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY:{
                    int Interval= intent.getIntExtra("Interval", 1500);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY " + theTime + " Interval " +Interval);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+Interval, MyAlarmHandler.getAlarmPendingIntent(this, ACTION_NONIDLE_REPEAT_EXACT_HIGH_FREQUENCY,Interval));

                }
                break;
                case ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY:{
                    int Interval= intent.getIntExtra("Interval", 1500);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY " + theTime + " Interval " +Interval);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+Interval, MyAlarmHandler.getAlarmPendingIntent(this, ACTION_NONIDLE_REPEAT_INEXACT_HIGH_FREQUENCY,Interval));
                }
                break;

                case ACTION_IDLE_REPEAT_EXACT:{

                    int Interval= intent.getIntExtra("Interval", 1500);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_IDLE_REPEAT_EXACT " + theTime + " Interval " +Interval);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+Interval, MyAlarmHandler.getAlarmPendingIntent(this, ACTION_IDLE_REPEAT_EXACT,Interval));
                    // issue an reboot
                    Log.d(TAG, "TestDPC Sending an Reboot");

                    mDevicePolicyManager = (DevicePolicyManager) this.getSystemService(
                            Context.DEVICE_POLICY_SERVICE);
                    mAdminComponentName = DeviceAdminReceiver.getComponentName(this);
                    mDevicePolicyManager.reboot(mAdminComponentName);
                    //bindMXMFServices("/sdcard/Config.xml");


                }
                break;

                case ACTION_IDLE_REPEAT_INEXACT:{
                    int Interval= intent.getIntExtra("Interval", 1500);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String theTime = df.format(Calendar.getInstance().getTime());
                    Log.i("Wake Lock Test (Serv)", "Alarm Fired: ACTION_IDLE_REPEAT_INEXACT " + theTime + " Interval " +Interval);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+Interval, MyAlarmHandler.getAlarmPendingIntent(this, ACTION_IDLE_REPEAT_INEXACT,Interval));


                }
                break;
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
