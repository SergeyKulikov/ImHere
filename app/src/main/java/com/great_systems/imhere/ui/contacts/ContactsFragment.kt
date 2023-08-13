package com.great_systems.imhere.ui.contacts

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.great_systems.imhere.R
import com.great_systems.imhere.databinding.FragmentContactBinding
import com.great_systems.imhere.entity.ContactItem


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ContactsFragment : Fragment() {

    private val CONTACT_ID = ContactsContract.Contacts._ID
    private val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
    private val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
    private val PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
    private val PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID

    private var _binding: FragmentContactBinding? = null

    val contacts: MutableList<ContactItem> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactBinding.inflate(inflater, container, false)

        showContacts()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.contactsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun showContacts() {
        getAll(this.requireContext())?.let { contacts.addAll(it) }
    }

    fun getAll(context: Context): ArrayList<ContactItem>? {
        val cr: ContentResolver = context.getContentResolver()
        val pCur: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(PHONE_NUMBER, PHONE_CONTACT_ID),
            null,
            null,
            null
        )
        if (pCur != null) {
            if (pCur.getCount() > 0) {
                val pIdx = pCur.getColumnIndex(CONTACT_ID)
                val pIdx1 = pCur.getColumnIndex(DISPLAY_NAME)
                val phones: HashMap<Int, ArrayList<String>> = HashMap()
                while (pCur.moveToNext()) {
                    val contactId: Int = pCur.getInt(pIdx)
                    var curPhones: ArrayList<String> = ArrayList()
                    if (phones.containsKey(contactId)) {
                        curPhones = phones[contactId]!!
                    }
                    curPhones.add(pCur.getString(pIdx1))
                    phones[contactId] = curPhones
                }
                val cur: Cursor? = cr.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    arrayOf(CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER),
                    "$HAS_PHONE_NUMBER > 0",
                    null,
                    "$DISPLAY_NAME ASC"
                )
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        val contacts: ArrayList<ContactItem> = ArrayList()
                        val idx = cur.getColumnIndex(CONTACT_ID)
                        val idx1 = cur.getColumnIndex(DISPLAY_NAME)

                        while (cur.moveToNext()) {
                            if (idx > 0) {
                                val id: Int = cur.getInt(idx)
                                if (phones.containsKey(id)) {
                                    val con = ContactItem()
                                    if (idx1 > 0) {
                                        con.setMyId(id)
                                        con.setName(cur.getString(idx1))
                                        con.setPhone(TextUtils.join(",", phones[id]!!.toArray()))
                                        contacts.add(con)
                                    }
                                }
                            }
                        }
                        return contacts
                    }
                    cur.close()
                }
            }
            pCur.close()
        }
        return null
    }
}