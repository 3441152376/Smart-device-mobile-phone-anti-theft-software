package cn.egg404.phone.utils

import android.content.Context
import cn.egg404.phone.MyApplication
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPref<T>(
    private val name: String,
    private val defValue: T,
    private val pref: String = "default"
) : ReadWriteProperty<Any?, T> {

    private val prefs by lazy {
        MyApplication.CONTEXT.getSharedPreferences(pref, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        findPreference(findProperName(property))

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putPreference(findProperName(property), value)

    private fun findProperName(property: KProperty<*>) = name.ifEmpty { property.name }

    private fun findPreference(key: String): T = when (defValue) {
        is Int -> prefs.getInt(key, defValue) as Any
        is Long -> prefs.getLong(key, defValue)
        is Float -> prefs.getFloat(key, defValue)
        is Boolean -> prefs.getBoolean(key, defValue)
        is String -> prefs.getString(key, defValue)
        else -> throw IllegalArgumentException("Unsupported type.")
    } as T

    private fun putPreference(key: String, value: T) {
        val edit = prefs.edit().apply {

            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is HashSet<*> -> putStringSet(key, value as HashSet<String>)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }

        edit.apply()

//        commit.yes { edit.commit() }.other { edit.apply() }

        /*if (!commit) {
            edit.commit()
        } else {
            edit.apply()
        }*/
    }

}


fun getType(raw: Class<*>, vararg args: Type) = object : ParameterizedType {
    override fun getRawType(): Type = raw
    override fun getActualTypeArguments(): Array<out Type> = args
    override fun getOwnerType(): Type? = null
}