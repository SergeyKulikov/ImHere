package com.great_systems.imhere.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MultiSelectCustomViewDialog(private val title: String?,
                                     private var adapter: RecyclerView.Adapter<*>,
                                     val buttonPushed: (Int) -> Unit)
    : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // вынести это наружу ????
        val view: View = layoutInflater.inflate(com.great_systems.imhere.R.layout.recycled_view_layout, null)
        val rv: RecyclerView = view.findViewById(com.great_systems.imhere.R.id.recycler_view)


        val mLayoutManager = LinearLayoutManager(context)
        rv.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context,
            mLayoutManager.orientation
        )
        rv.addItemDecoration(dividerItemDecoration)
        rv.adapter = adapter



        Log.d("ADAPTER", (adapter as MultiSelectContactDialogRecyclerViewAdapter).itemCount.toString())
        adapter.notifyDataSetChanged()

        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            // Set the dialog title
            builder.setTitle(title)
                /*
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
                */
                .setView(view)
                // Set the action buttons
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog

                        buttonPushed(DialogInterface.BUTTON_POSITIVE)
                    })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        buttonPushed(DialogInterface.BUTTON_NEGATIVE)
                    })

            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }
}