package com.ztf.alarmapp

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("aaa", "收到")
        MainActivity.activity.wakeUpAndUnlock()
        openDing(MainActivity.mPkgName, context)
    }

    fun openDing(packageName: String, context: Context?) {
        val packageManager = context?.packageManager
//        var pi = packageManager?.getPackageInfo("com.alibaba.android.rimet", 0)
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageName)
        val apps = packageManager?.queryIntentActivities(resolveIntent, 0)
        val resolveInfo = apps?.iterator()?.next()
        if (resolveInfo != null) {
            val className = resolveInfo.activityInfo.name
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val cn = ComponentName(packageName, className)
            intent.component = cn
            context.startActivity(intent)
        }
    }
}
