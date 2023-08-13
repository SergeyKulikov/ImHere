package com.great_systems.imhere

import android.app.Application
import com.great_systems.imhere.system.room.SystemDatabase
import com.great_systems.imhere.system.room.entity.PreferenceData

open class App: Application() {
    companion object {
        lateinit var instance: App

        /** Для каждой отдельной фирмы мы создаем свою отдельную БД
         * Этим мы решаем проблему разделения данными между разными организакиями,
         * которые могут вести в одной программе.
         */

        @JvmStatic lateinit var system: SystemDatabase

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Launcher содержит только текущие данные, поэтому он один на всех
        // system.getLauncher()
    }

}