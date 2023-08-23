package com.great_systems.imhere.ui.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.great_systems.imhere.databinding.FragmentContactBinding
import com.great_systems.imhere.entity.ContactItem
import com.great_systems.imhere.room.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null

    val contacts: MutableList<ContactItem> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactBinding.inflate(inflater, container, false)

        contactsAdapter = ContactAdapter()
        binding.rvContacts.adapter = contactsAdapter
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvContacts.setLayoutManager(layoutManager)
        // binding.rvContacts.setHasFixedSize(true)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {

            Repository.selectAllContactsLD()
                .observe(
                    viewLifecycleOwner
                ) { value ->
                    contactsAdapter.setData(value)
                    contactsAdapter.notifyDataSetChanged()
                }
        }


        // binding.buttonFirst.setOnClickListener {
        //     findNavController().navigate(R.id.contactsFragment)
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}