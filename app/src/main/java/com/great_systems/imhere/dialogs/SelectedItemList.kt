package com.great_systems.imhere.dialogs

class SelectedItemList<T>(var list: ArrayList<SelectedItem<T>>) {

    constructor() : this(ArrayList()) {
    }

    fun toItemList(vararg checked: Boolean): ArrayList<T> {
        var newList = ArrayList<T>()

        list.forEach {
            if (it.selected in checked) {
                newList.add(it.item)
            }
        }

        return newList
    }

    fun addAll(list: List<T>) {
        list.forEach {
            this.list.add(SelectedItem(it))
        }
    }
}