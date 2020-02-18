package com.duke.visitormanagement.util

import android.content.Context
import android.content.SharedPreferences

object SharedPrefrencesUtils {

    private const val SHARED_PREFERENCES = "VisitorManagement"
    private var sPreferences: SharedPreferences? = null
    private const val IS_USER_LOGIN   = "IS_USER_LOGIN"
    private const val USER_ID         = "USER_ID"
    private const val USER_NAME       = "USER_NAME"
    private const val USER_EMAIL      = "USER_EMAIL"
    private const val USER_PHONE      = "USER_PHONE"
    private const val USER_TYPE    = "USER_TYPE"

    fun init(context: Context?) {
        sPreferences = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun isUserLogin(): Boolean? {
        return sPreferences?.getBoolean(IS_USER_LOGIN, false)
    }

    fun getUserId(): String? {
        return sPreferences?.getString(USER_ID, "")
    }

    fun getUserName(): String? {
        return sPreferences?.getString(USER_NAME, "")
    }

    fun getUserEmail(): String? {
        return sPreferences?.getString(USER_EMAIL, "")
    }

    fun getUserNumber(): String? {
        return sPreferences?.getString(USER_PHONE, "")
    }

    fun setUserLogin(status: Boolean,id: String?,name: String?,email: String?,number: String?,userType: String?) {
        sPreferences?.edit()
            ?.putBoolean(IS_USER_LOGIN, status)
            ?.putString(USER_ID, id)
            ?.putString(USER_NAME, name)
            ?.putString(USER_EMAIL, email)
            ?.putString(USER_PHONE, number)
            ?.putString(USER_TYPE, userType)
            ?.apply()
    }

    fun clearUser() {
        sPreferences?.edit()
            ?.clear()
            ?.apply()
    }
}