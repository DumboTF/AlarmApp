package com.ztf.alarmapp

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        var mHour = 0
        var mMinute = 0
        var mPkgName = ""
        lateinit var activity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        activity = this
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        ok_wechat.setOnClickListener {
            nao("com.tencent.wework")
        }
        ok_ali.setOnClickListener {
            nao("com.alibaba.android.rimet")
        }
    }

    fun nao(pkgName: String) {
        val strHour = hour.text.toString()
        val strMinute = minute.text.toString()
        if (strHour.isNotEmpty())
            mHour = strHour.toInt()
        if (strMinute.isNotEmpty())
            mMinute = strMinute.toInt()
        mPkgName = pkgName
        val mIntent = Intent(this, AlarmService::class.java)
        stopService(mIntent)
        startService(mIntent)
        Toast.makeText(applicationContext, "每天 $mHour 时 $mMinute 分，闹它", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun wakeUpAndUnlock() {
        // 获取电源管理器对象
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val screenOn = pm.isScreenOn
        Log.d("WakeScreen0", "screenOn: $screenOn")
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright"
            )
            wl.acquire(10000) // 点亮屏幕
            wl.release() // 释放
        }
        // 屏幕解锁
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock("unLock")
        // 屏幕锁定
        //        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard() // 解锁
        unLockScreen()
    }

    private fun unLockScreen() {
        val win = getWindow()
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        win.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }
}
