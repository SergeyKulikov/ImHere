package com.great_systems.imhere.room


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.great_systems.imhere.App
import com.great_systems.imhere.dialogs.SelectedItemList
import com.great_systems.imhere.entity.ContactItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object Repository {

    private val CONTACT_ID = ContactsContract.Contacts._ID
    private val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
    private val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
    private val PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
    private val PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID

    suspend fun selectContactsFromAndDevice(context: Context):
            SelectedItemList<ContactItem> = withContext(Dispatchers.IO) {

        val result: SelectedItemList<ContactItem> = SelectedItemList()

        // Получаем контакты из телефона. Они не отмечены.
        result.addAll(loadContactsFromDevice(context))

        // Получаем контакты из базы данных и отмечаем как выделенные
        AppDatabase.getDatabase(App.instance).app().selectContactList().let { listDB ->
            result.list.forEach { itemDevice ->
                listDB.forEach{ itemDB ->
                    if (itemDB.id == itemDevice.item.id) {
                        itemDevice.selected = true
                    }
                }
            }
        }

        return@withContext result
    }

    suspend fun selectAllContactsLD(): LiveData<List<ContactItem>> = withContext(Dispatchers.IO) {
        return@withContext AppDatabase.getDatabase(App.instance).app().selectContactListLD()
    }

    suspend fun selectContactsFromDB(): List<ContactItem> = withContext(Dispatchers.IO) {
        return@withContext AppDatabase.getDatabase(App.instance).app().selectContactList()
    }

    fun findContact(phoneContacts: MutableList<ContactItem>, item: ContactItem): Boolean {
        phoneContacts.forEach {
            if (it.id == item.id) {
                return true
            }
        }
        return false
    }



    @SuppressLint("Range")
    fun loadContactsFromDevice(context: Context): MutableList<ContactItem> {
        val contacts: MutableList<ContactItem> = ArrayList()

        val contentResolver: ContentResolver = context.contentResolver
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {

                // получаем каждый контакт
                val id = cursor.getLong(
                    cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
                )
                val contact = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                )
                val photo_id = cursor.getLong(
                    cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_FILE_ID)
                )
                val photo_uri = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                )

                // добавляем контакт в список
                contacts.add(ContactItem(id, contact, photo_id, photo_uri,
                    getNamePhoneDetails(contentResolver, id)))
            }
            cursor.close()
        }
        return contacts
    }

    @SuppressLint("Range")
    fun getNamePhoneDetails(contentResolver: ContentResolver, contactId: Long): MutableList<String> {
        val names: MutableList<String> = mutableListOf()
        val cr = contentResolver
        val cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            "${ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID} == ?",
            arrayOf(contactId.toString()),
            null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val idd = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                // val number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                names.add(number)
            }
        }
        return names
    }

    suspend fun saveContactsToDB(contacts: List<ContactItem>) = withContext(Dispatchers.IO) {
        AppDatabase.getDatabase(App.instance).app().insertContactList(contacts)
    }

    suspend fun clearContacts() = withContext(Dispatchers.IO) {
        AppDatabase.getDatabase(App.instance).app().removeAllContact()
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


}