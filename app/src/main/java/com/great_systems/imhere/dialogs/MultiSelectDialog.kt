package com.great_systems.imhere.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseBooleanArray
import androidx.appcompat.app.AlertDialog
import androidx.core.util.contains
import androidx.fragment.app.DialogFragment
import com.great_systems.imhere.R

class MultiSelectDialog(private val title: String?,
                        private val list: ArrayList<String>,
                        private var selectedItems: SparseBooleanArray,
                        val returnVal: (selectedItems: SparseBooleanArray) -> Unit)
    : DialogFragment(R.layout.dialog_multi_select) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val arr = list.toTypedArray()
        val checkedItems: BooleanArray = BooleanArray(arr.size)

        for (i in arr.indices) {
            if (selectedItems.contains(i)) {
                checkedItems[i] = selectedItems[i]
            } else {
                checkedItems[i] = false
            }
        }

        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            // Set the dialog title
            builder.setTitle(title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(arr, checkedItems,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which)
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(which)
                        }
                    })
                // Set the action buttons
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog

                    })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->

                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}