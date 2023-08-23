package com.great_systems.imhere.dialogs

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.great_systems.imhere.AndroidFileUtils
import com.great_systems.imhere.R
import com.great_systems.imhere.entity.ContactItem
import java.io.*


class MultiSelectContactDialogRecyclerViewAdapterOld(
    val context: Context,
    private var arrayList: ArrayList<ContactItem>,
    private var isCheckedList: SparseBooleanArray,
    private var onItemClickListener: OnItemClickListener,
    val actualList: (ArrayList<ContactItem>) -> Unit
) :
    RecyclerView.Adapter<MultiSelectContactDialogRecyclerViewAdapterOld.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_check_box_list2_dialog, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<ContactItem>, mIsCheckedList: SparseBooleanArray) {
        arrayList = list
        isCheckedList = mIsCheckedList
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val checkbox_list_dialog: MaterialCheckBox by lazy {
            itemView.findViewById<MaterialCheckBox>(
                R.id.checkbox_list_dialog
            )
        }
        val text2: TextView by lazy { itemView.findViewById(R.id.tv_text2) }
        val image_item: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_photo) }

        init {
            checkbox_list_dialog.setOnClickListener {
                onClick(it)
            }
        }

        fun bind(position: Int) {
            // use the sparse boolean array to check
            checkbox_list_dialog.isChecked = isCheckedList.get(position, false)
            checkbox_list_dialog.text = arrayList[position].name
            text2.text = arrayList[position].phonesListString()
            // image_item.setImageDrawable(context.getDrawable(androidx.transition.R.drawable.abc_ic_menu_overflow_material))

            arrayList[position].photo_id?.let {
                if (it > 0) {
                    val bmp1 = AndroidFileUtils(context).loadContactPhotoThumbnail(arrayList[position].photo_uri.toString())
                    image_item.setImageBitmap(bmp1)
                }
            }
        }

        // https://stackoverflow.com/questions/2383580/how-do-i-load-a-contact-photo

        fun onClick(v: View?) {
            val adapterPosition = absoluteAdapterPosition
            if (!isCheckedList.get(adapterPosition, false)) {
                checkbox_list_dialog.isChecked = true
                isCheckedList.put(adapterPosition, true)
            } else {
                checkbox_list_dialog.isChecked = false
                isCheckedList.put(adapterPosition, false)
            }
            onItemClickListener.onItemClick(isCheckedList, adapterPosition)

            val newList = ArrayList<ContactItem>()

            for (idx in arrayList.indices) {
                if (isCheckedList[idx]) {
                    newList.add(arrayList[idx])
                }
            }
            actualList.invoke(newList)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(isCheckedList: SparseBooleanArray, position: Int)
    }
}