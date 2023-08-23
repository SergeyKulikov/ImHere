package com.great_systems.imhere.dialogs.multiselect

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.great_systems.imhere.R

class MultiSelectDialogRecyclerViewAdapter (
    val context: Context,
    private var arrayList: ArrayList<String>,
    private var isCheckedList: SparseBooleanArray,
    private var onItemClickListener: OnItemClickListener
):
    RecyclerView.Adapter<MultiSelectDialogRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_check_box_list_dialog, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(mIsCheckedList: SparseBooleanArray) {
        isCheckedList = mIsCheckedList
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val checkbox_list_dialog: MaterialCheckBox by lazy { itemView.findViewById<MaterialCheckBox>(R.id.checkbox_list_dialog) }
        val btn_positive: Button by lazy { itemView.findViewById<Button>(R.id.btn_positive) }
        val btn_negative: Button by lazy { itemView.findViewById<Button>(R.id.btn_negative) }
        val btn_neutral: Button by lazy { itemView.findViewById<Button>(R.id.btn_neutral) }
        val cl_multi_select_dialog_root: ConstraintLayout by lazy { itemView.findViewById<ConstraintLayout>(R.id.cl_multi_select_dialog_root) }
        init {
            checkbox_list_dialog.setOnClickListener{
                onClick(it)
            }
        }
        fun bind(position: Int) {
            // use the sparse boolean array to check
            checkbox_list_dialog.isChecked = isCheckedList.get(position, false)
            checkbox_list_dialog.text = arrayList[position]
        }
        fun onClick(v: View?) {
            val adapterPosition = absoluteAdapterPosition
            if (!isCheckedList.get(adapterPosition, false)) {
                checkbox_list_dialog.isChecked = true
                isCheckedList.put(adapterPosition, true)
            } else {
                checkbox_list_dialog.isChecked = false
                isCheckedList.put(adapterPosition, false)
            }
            onItemClickListener.onItemClick(isCheckedList,adapterPosition)
        }
    }
    interface OnItemClickListener {
        fun onItemClick(isCheckedList: SparseBooleanArray, position: Int)
    }
}