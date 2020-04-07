package com.llistochek.advanceddiary

import android.content.Context
import androidx.core.content.edit
import com.llistochek.advanceddiary.api.LoginResponse
import com.llistochek.advanceddiary.api.StudentInfo

object Prefs {
    private const val PREFS_NAME = "preferences"
    private const val IS_USER_LOGGED_IN_NAME = "is_user_logged_in"
    fun isUserLoggedIn(ctx: Context): Boolean {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(IS_USER_LOGGED_IN_NAME, false)
    }
    fun setUserLoggedIn(ctx: Context) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(IS_USER_LOGGED_IN_NAME, true).apply()
    }

    private const val DEVICE_ID = "device_id"
    fun setDeviceId(ctx: Context, id: String) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString(DEVICE_ID, id).apply()
    }
    fun getDeviceId(ctx: Context): String {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(DEVICE_ID, null) ?: throw IllegalArgumentException("device_id doesn't exists")
    }

    private const val STUDENT_INFO = "student_info"
    private const val ID = "id"
    fun setStudentInfo(ctx: Context, studentInfo: LoginResponse) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString(STUDENT_INFO, studentInfo.children!![0][1] + studentInfo.children[0][2])
            putInt(ID, studentInfo.id!!)
        }
    }
    fun getStudentInfo(ctx: Context): StudentInfo {
        val prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return  StudentInfo(prefs.getString(STUDENT_INFO, null)!!, prefs.getInt(ID, -1))
    }
}