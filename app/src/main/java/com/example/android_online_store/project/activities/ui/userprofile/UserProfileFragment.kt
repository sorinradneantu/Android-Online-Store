package com.example.android_online_store.project.activities.ui.userprofile

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android_online_store.databinding.FragmentUserprofileBinding





class UserProfileFragment : Fragment() {


    //private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentUserprofileBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
       // notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentUserprofileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("shopPreferences", MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname_logged", "")
        val lastname = sharedPreferences.getString("lastname_logged", "")
        val email = sharedPreferences.getString("email_logged", "")
        val address = sharedPreferences.getString("address_logged", "")
        val phoneNumber = sharedPreferences.getString("phoneNr_logged", "")

        val firstNameTextView: TextView = binding.firstnameshow
        firstNameTextView.text = firstname
        val lastNameTextView: TextView = binding.lastnameshow
        lastNameTextView.text = lastname
        val emailTextView: TextView = binding.emailshow
        emailTextView.text = email
        val addressTextView: TextView = binding.addressshow
        addressTextView.text = address
        val phoneNrTextView: TextView = binding.phonenrshow
        phoneNrTextView.text = phoneNumber

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}