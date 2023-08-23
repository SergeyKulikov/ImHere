package com.great_systems.imhere.ui.contacts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.great_systems.imhere.R
import com.great_systems.imhere.entity.ContactItem

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private val dataSet: MutableList<ContactItem> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(value: List<ContactItem>) {
        dataSet.clear()
        dataSet.addAll(value)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val phones: TextView

        init {
            // Define click listener for the ViewHolder's View
            name = view.findViewById(R.id.name)
            phones = view.findViewById(R.id.phone)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.contact_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.name.text = item.name
        viewHolder.phones.text = toList(item.phones)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun toList(values: MutableList<String>): String {
        val buffer = StringBuffer()
        for (i in values.indices) {
            buffer.append(if (i == 0) "" else ", ")
            buffer.append(values[i])
        }
        return buffer.toString()
    }

}