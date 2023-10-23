package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.button.MaterialButton
import uz.itschool.shopping.R
import uz.itschool.shopping.databinding.FragmentUserBinding
import uz.itschool.shopping.model.User
import uz.itschool.shopping.service.SharedPrefHelper

class UserFragment : Fragment() {
    private var user: User? = null
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.userBackFab.setOnClickListener { findNavController().popBackStack() }

        checkUser()

        if (user == null) {
            setNoAccount()
        } else {
            setAccount()
        }

        return binding.root
    }

    private fun checkUser() {
        val shared = SharedPrefHelper.getInstance(requireContext())
        user = shared.getUser()
    }

    @SuppressLint("SetTextI18n")
    private fun setAccount() {
        binding.userNotLoggedInIv.visibility = View.GONE
        binding.userName.text = user!!.firstName + " " + user!!.lastName
        binding.userUsername.text = "@" + user!!.username
        binding.userEmail.text = user!!.email
        binding.userImage.load(user!!.image)
        setAlert()
        setMyCartListener()
    }

    private fun setMyCartListener() {
        binding.userMyCartMb.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_cartFragment)
        }
    }

    private fun setAlert() {
        binding.userLogInOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).create()
            val inflator = this.layoutInflater
            val view = inflator.inflate(R.layout.log_out_alert, null)

            view.findViewById<ImageView>(R.id.alert_image).load(user!!.image)
            view.findViewById<MaterialButton>(R.id.alert_yes).setOnClickListener {
                val shared = SharedPrefHelper.getInstance(requireContext())
                shared.logOut()
                setNoAccount()
                alertDialog.dismiss()
            }
            view.findViewById<MaterialButton>(R.id.alert_no).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.setView(view)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun setNoAccount() {
        binding.userAccountInfoParent.visibility = View.GONE
        val log: MaterialButton = binding.userLogInOut
        binding.userNotLoggedInIv.visibility = View.VISIBLE
        log.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue2))
        log.text = "Log in"
        log.icon = ContextCompat.getDrawable(requireContext(), R.drawable.login_icon)
        log.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
        }
    }
}