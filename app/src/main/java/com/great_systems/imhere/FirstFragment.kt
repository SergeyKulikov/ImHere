package com.great_systems.imhere

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.great_systems.imhere.databinding.FragmentFirstBinding
import com.great_systems.imhere.dialogs.MultiSelectContactDialogRecyclerViewAdapter
import com.great_systems.imhere.dialogs.SelectedItem
import com.great_systems.imhere.dialogs.SelectedItemList
import com.great_systems.imhere.dialogs.SimpleDialog
import com.great_systems.imhere.entity.ContactItem
import com.great_systems.imhere.location.LocationData
import com.great_systems.imhere.location.LocationDetector
import com.great_systems.imhere.room.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            val contactList =
                Repository.selectContactsFromAndDevice(this@FirstFragment.requireContext())
            contactList.list.sortWith(compareBy({ !it.selected }, { it.item.name }))

            val adapter =
                MultiSelectContactDialogRecyclerViewAdapter(this@FirstFragment.requireContext(),
                    contactList,
                    object : MultiSelectContactDialogRecyclerViewAdapter.OnItemClickListener {
                        override fun onItemClick(item: SelectedItem<ContactItem>, position: Int) {
                            LocationDetector(requireActivity(), onGotData = {
                                sendLocationDialog(it, listOf(item.item))
                            })
                        }
                    })
            adapter.checkable = false

            val mLayoutManager = LinearLayoutManager(context)
            binding.rvLayout.recyclerView.layoutManager = mLayoutManager

            val dividerItemDecoration = DividerItemDecoration(
                binding.rvLayout.recyclerView.context, mLayoutManager.orientation
            )
            binding.rvLayout.recyclerView.addItemDecoration(dividerItemDecoration)
            binding.rvLayout.recyclerView.adapter = adapter
        }

        binding.buttonFirst.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val deviceContacts = Repository.loadContactsFromDevice(requireContext())
                val databaseContacts = Repository.selectContactsFromDB()
                val contacts: SelectedItemList<ContactItem> = SelectedItemList()

                deviceContacts.forEach {
                    val item = SelectedItem(it)
                    item.selected = findContact(it, databaseContacts)
                    contacts.list.add(item)
                }

                SimpleDialog.multiselectContactDialog(requireContext(),
                    childFragmentManager,
                    contactList = contacts,
                    onSelectedChange = { newContacts ->
                        // Новые контакты бдут сохранены в БД, а те которые отключены буду удалены
                        CoroutineScope(Dispatchers.Main).launch {
                            Repository.clearContacts()
                            Repository.saveContactsToDB(newContacts.toItemList(true))
                        }
                    })
            }
            // findNavController().navigate(R.id.contactsFragment)
        }
    }

    private fun sendLocationDialog(locationData: LocationData, contacts: List<ContactItem>) {
        // отправить данные

        val pm: PackageManager = requireActivity().packageManager
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val text = "Я нахожусть в этой точке ${locationData.firstAddress}: " +
                "${locationData.locationResult.longitude},${locationData.locationResult.latitude}"

        // задаем получателя
        val telNumber = contacts[0].phones[0]
        intent.putExtra(contacts[0].name, "$telNumber@s.whatsapp.net")
        val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)

        if (info != null) {
            // проверяем есть ли Whatsapp (может выкинуть Exception если нет whatsapp)
            intent.setPackage("com.whatsapp")
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Share with"))
        }
    }

    private fun getDeviceLocation() {
        LocationDetector(requireActivity(), onGotData = {

        })
    }

    private fun findContact(
        contactItem: ContactItem, databaseContacts: List<ContactItem>
    ): Boolean {
        databaseContacts.forEach {
            if (it.id == contactItem.id) {
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}