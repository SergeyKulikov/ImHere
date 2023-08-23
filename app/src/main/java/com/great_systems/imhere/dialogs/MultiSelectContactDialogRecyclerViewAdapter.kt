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


class MultiSelectContactDialogRecyclerViewAdapter(
    val context: Context,
    private var arrayList: SelectedItemList<ContactItem>,
    private var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MultiSelectContactDialogRecyclerViewAdapter.ViewHolder>() {

    var checkable: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_check_box_list2_dialog, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<SelectedItem<ContactItem>>, mIsCheckedList: SparseBooleanArray) {
        // arrayList = list
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
            if (checkable) {
                checkbox_list_dialog.setOnClickListener {
                    onClick(it)
                }
            } else {
                checkbox_list_dialog.setOnClickListener {
                    onItemClickListener.onItemClick(
                        arrayList.list[adapterPosition],
                        adapterPosition
                    )
                }
            }
        }

        fun bind(position: Int) {
            // use the sparse boolean array to check
            checkbox_list_dialog.isEnabled = checkable

            checkbox_list_dialog.isChecked = arrayList.list[position].selected
            checkbox_list_dialog.text = arrayList.list[position].item.name
            text2.text = arrayList.list[position].item.phonesListString()
            // image_item.setImageDrawable(context.getDrawable(androidx.transition.R.drawable.abc_ic_menu_overflow_material))

            arrayList.list[position].item.photo_id?.let {
                if (it > 0) {
                    val bmp1 = AndroidFileUtils(context).loadContactPhotoThumbnail(arrayList.list[position].item.photo_uri.toString())
                    image_item.setImageBitmap(bmp1)
                }
            }
        }

        // https://stackoverflow.com/questions/2383580/how-do-i-load-a-contact-photo

        fun onClick(v: View?) {
            val adapterPosition = absoluteAdapterPosition
            if (!arrayList.list[adapterPosition].selected) {
                checkbox_list_dialog.isChecked = true
                arrayList.list[adapterPosition].selected = true
            } else {
                checkbox_list_dialog.isChecked = false
                arrayList.list[adapterPosition].selected = false
            }
            onItemClickListener.onItemClick(arrayList.list[adapterPosition], adapterPosition)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: SelectedItem<ContactItem>, position: Int)
    }
}