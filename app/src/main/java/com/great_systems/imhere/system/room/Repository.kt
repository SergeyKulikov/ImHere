package com.great_systems.imhere.system.room

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.great_systems.imhere.App
import com.great_systems.imhere.system.room.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import java.lang.reflect.Type
import kotlin.coroutines.suspendCoroutine

class Repository {


    companion object {

        /**
         * Одной функцие получаем все куски
         */
        suspend fun selectAllPrefData(): PreferenceData {
            var dataPreferenceData: PreferenceData = PreferenceData()
            CoroutineScope(Dispatchers.Main).launch {
                dataPreferenceData.companyEntity = getPrefData<CompanyEntity>(EPrefType.pftCompany)
                dataPreferenceData.userEntity = getPrefData<UserEntity>(EPrefType.pftUser)
            }.onJoin.let {
                return dataPreferenceData
            }
        }

        private suspend fun <T> getPrefData(type: EPrefType): T? = withContext(Dispatchers.IO) {
            return@withContext suspendCoroutine { continuation ->
                val launcherEntity = LauncherEntity(App.instance)
                var rezult: T? = null

                try {
                    SystemDatabase.getDatabase(App.instance).sessionDao().select(
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

        /*
        suspend fun getTableList(): TableList = withContext(Dispatchers.IO) {
            return@withContext suspendCoroutine { continuation ->
                // val launcherEntity = App.launcherEntity // LauncherEntity(App.getInstance())
                val rezult = TableList()

                try {
                    App.currentDB.formDao().select().let { list ->
                        rezult.table.clear()
                        rezult.table.addAll(list)
                    }
                } catch (ex: Exception) {
                    // ------
                }
                continuation.resume(rezult)
            }
        }

        suspend fun insertTable(table: TableStruct) = withContext(Dispatchers.IO) {
            return@withContext suspendCoroutine { continuation ->
                var rezult: Long = 0
                try {
                    rezult = App.currentDB.formDao().insert(table)!!
                } catch (ex: Exception) {
                    // ------
                }
                continuation.resume(rezult)
            }
        }

        suspend fun insertTableList(tableList: List<TableStruct>) =
            withContext(Dispatchers.IO) {
                return@withContext suspendCoroutine { continuation ->
                    var rezult: List<Long> = ArrayList()
                    try {
                        App.currentDB?.formDao()?.insert(tableList)
                    } catch (ex: Exception) {
                        // ------
                    }
                    continuation.resume(null)
                }
            }
      */


        private fun <T> getType(obj: T): Type? {
            return object : TypeToken<T>() {}.type
        }


    }

}