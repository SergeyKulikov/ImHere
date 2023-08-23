package com.great_systems.imhere.dialogs

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.great_systems.imhere.R
import com.great_systems.imhere.databinding.DialogMultiSelectBinding
import com.great_systems.imhere.dialogs.multiselect.MultiSelectDialogListener
import com.great_systems.imhere.dialogs.multiselect.MultiSelectDialogRecyclerViewAdapter
import com.great_systems.imhere.dialogs.multiselect.MultiSelectDialogViewModel


class MultiSelectDialog2 (private val title: String?,
                          private val list: ArrayList<String>,
                          private var selectedItems: SparseBooleanArray,
                          val returnVal: (selectedItems: SparseBooleanArray) -> Unit
) : DialogFragment(), MultiSelectDialogListener {

    // https://developer.alexanderklimov.ru/android/views/checkbox.php

    private var _binding: DialogMultiSelectBinding? = null
    private val binding get() = _binding!!
    val viewModel: MultiSelectDialogViewModel by viewModels()

    private lateinit var adapter : MultiSelectDialogRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMultiSelectBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        viewModel.multiSelectDialogListener = this
        binding.rvCheckList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCheckList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        init()
    }

    private fun populateRecyclerView() {
        adapter = MultiSelectDialogRecyclerViewAdapter(requireContext(), list, selectedItems,
            object : MultiSelectDialogRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(isCheckedList: SparseBooleanArray, position: Int) {
                    selectedItems = isCheckedList
                }
            })
        binding.rvCheckList.adapter = adapter
    }

    private fun init(){
        populateRecyclerView()
        if (!title.isNullOrEmpty()) {
            binding.tvTitle.visibility = View.VISIBLE
            viewModel.title = title
        }
        else{
            binding.tvTitle.visibility = View.GONE
        }

        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (dialog!!.isShowing) {
                    dialog!!.cancel()
                }
            }
            true
        }
    }

    override fun onPositiveClick() {
        returnVal(selectedItems)
        dialog?.dismiss()
    }

    override fun onNegativeClick() {
        dialog?.cancel()
    }

    override fun onNeutralClick() {
        selectedItems.clear()
        adapter.setData(selectedItems)
    }

    override fun onCheckedChangedListener() {
        // requireView().snackbar("Items Changed")
    }
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

}