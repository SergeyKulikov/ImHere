package com.great_systems.imhere.dialogs.multiselect

interface MultiSelectDialogListener {
    fun onPositiveClick()
    fun onNegativeClick()
    fun onNeutralClick()
    fun onCheckedChangedListener()
}