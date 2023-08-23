package com.great_systems.imhere.dialogs.multiselect

import android.view.View
import androidx.lifecycle.ViewModel

class MultiSelectDialogViewModel constructor() : ViewModel() {
    var title: String? = null
    var multiSelectDialogListener: MultiSelectDialogListener? = null

    fun onPositiveClick(view: View) {
        multiSelectDialogListener?.onPositiveClick()
    }
    fun onNegativeClick(view: View) {
        multiSelectDialogListener?.onNegativeClick()
    }
    fun onNeutralClick(view: View) {
        multiSelectDialogListener?.onNeutralClick()
    }
    fun onSelectedItemsChanged(){
        multiSelectDialogListener?.onCheckedChangedListener()
    }
}