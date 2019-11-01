package com.ztf.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import java.util.*

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val oneDay = 24 * 60 * 60 * 1000L
//        val oneDay = 10 * 1000L
        //得到日历实例，主要是为了下面的获取时间
        val mCalendar = Calendar.getInstance()
        //开机之后到现在的运行时间
        var firstTime = SystemClock.elapsedRealtime()
        //获取当前毫秒值
        val systemTime = System.currentTimeMillis()
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.timeInMillis = System.currentTimeMillis()
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.timeZone = TimeZone.getTimeZone("GMT+8")
        //设置在几点提醒 设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, MainActivity.mHour)
        //设置在几分提醒 设置的为25分
        mCalendar.set(Calendar.MINUTE, MainActivity.mMinute)
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0)
        mCalendar.set(Calendar.MILLISECOND, 0)
        //选择定时时间
        var selectTime = mCalendar.timeInMillis

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1)
            selectTime = mCalendar.timeInMillis
        }

        val time = selectTime-systemTime
        firstTime +=time

        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        val triggerr = 10000L
        val mIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0)
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, mCalendar.timeInMillis, pendingIntent)
        //第一个参数表示闹钟类型，第二个参数表示闹钟首次执行时间，第三个参数表示闹钟两次执行的间隔时间，第三个参数表示闹钟响应动作。
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, oneDay, pendingIntent)
        return super.onStartCommand(intent, flags, startId)
    }

}