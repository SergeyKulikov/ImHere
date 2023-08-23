package com.great_systems.sysdb

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.great_systems.sysdb.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import java.lang.reflect.Type
import kotlin.coroutines.suspendCoroutine


object SystemRepository {
    /**
     * Одной функцие получаем все куски
     */
    suspend fun selectAllPrefData(context: Context): PreferenceData {
        var dataPreferenceData: PreferenceData = PreferenceData()
        CoroutineScope(Dispatchers.Main).launch {
            dataPreferenceData.companyEntity = getPrefData<CompanyEntity>(context, EPrefType.pftCompany)
            dataPreferenceData.userEntity = getPrefData<UserEntity>(context, EPrefType.pftUser)
        }.onJoin.let {
            return dataPreferenceData
        }
    }

    private suspend fun <T> getPrefData(context: Context, type: EPrefType): T? = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            val launcherEntity = LauncherEntity(context.applicationContext)
            var rezult: T? = null

            try {
                SystemDatabase.getDatabase(context.applicationContext).sessionDao().select(
                    launcherEntity.currentCompany,
                    launcherEntity.currentUser,
                    type
                ).let {
                    rezult = Gson().fromJson(it.prefData, getType(it))
                }

            } catch (ex: Exception) {
                // ------
            }
            continuation.resume(rezult)
        }
    }

    private fun <T> getType(obj: T): Type? {
        return object : TypeToken<T>() {}.type
    }
}