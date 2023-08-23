package com.great_systems.imhere.dialogs

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.util.SparseBooleanArray
import androidx.fragment.app.FragmentManager
import com.great_systems.imhere.entity.ContactItem


object SimpleDialog {

    fun multiselectContactDialog(
        context: Context,
        fragmentManager: FragmentManager,
        contactList: SelectedItemList<ContactItem>,
        onSelectedChange: (SelectedItemList<ContactItem>) -> Unit
    ) {
        val adapter = MultiSelectContactDialogRecyclerViewAdapter(context, contactList, object :
            MultiSelectContactDialogRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(item: SelectedItem<ContactItem>, position: Int) {
                Log.d("999", item.toString())
            }
        })

        MultiSelectCustomViewDialog(null, adapter) {
            if (it == DialogInterface.BUTTON_POSITIVE) {
                onSelectedChange(contactList)
            }
        }.show(fragmentManager, "selectContacts_Dialog")
    }


    fun selectContactsOld(
        context: Context,
        fragmentManager: FragmentManager,
        deviceContactsItems: MutableList<ContactItem>,
        selected: BooleanArray,
        onSelectedChange: (MutableList<ContactItem>) -> Unit
    ) {
        val newSelected: HashSet<Int> = hashSetOf()
        val listItems: MutableList<ContactItem> = mutableListOf()
        var newContacts: ArrayList<ContactItem> = arrayListOf()
        val sp = SparseBooleanArray()
        val arr: ArrayList<ContactItem> = arrayListOf()

        // Список deviceContactsItems должен быть отсортирован до передачи сюда
        deviceContactsItems.forEach {
            listItems.add(it)
        }
        listItems.toCollection(arr)
        newContacts.addAll(arr)

        // Заполняем список выбранного
        for (i in selected.indices) {
            if (selected[i]) {
                newSelected.add(i)
            }
            sp.put(i, selected[i])
        }

        val adapter = MultiSelectContactDialogRecyclerViewAdapterOld(context, arr, sp, object :
            MultiSelectContactDialogRecyclerViewAdapterOld.OnItemClickListener {
            override fun onItemClick(isCheckedList: SparseBooleanArray, position: Int) {
                Log.d("999", isCheckedList.size().toString())
            }
        }) {
            newContacts = it
        }

        MultiSelectCustomViewDialog(null, adapter) {
            if (it == DialogInterface.BUTTON_POSITIVE) {
                onSelectedChange(newContacts)
            }
        }.show(fragmentManager, "selectContacts_Dialog")
    }

}