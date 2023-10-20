package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.button.MaterialButton
import uz.itschool.shopping.R
import uz.itschool.shopping.databinding.FragmentUserBinding
import uz.itschool.shopping.model.User
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import uz.itschool.shopping.service.SharedPrefHelper

private const val ARG_PARAM1 = "user"

class UserFragment : Fragment() {
    private var user : User? = null
    private lateinit var binding: FragmentUserBinding
    private val api = APIClient.getInstance().create(APIService::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.userBackFab.setOnClickListener { requireActivity().onBackPressed() }

        checkUser()

        if (user == null){
            setNoAccount()
        }else{
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
            // TODO:
        }
    }

    private fun setAlert() {
        binding.userLogInOut.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Do you really want to log out?")

            builder.setPositiveButton("Yes") { _, _ ->
                val shared = SharedPrefHelper.getInstance(requireContext())
                shared.logOut()
                setNoAccount()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun setNoAccount(){
        binding.userAccountInfoParent.visibility =  View.GONE
        val log:MaterialButton = binding.userLogInOut
        binding.userNotLoggedInIv.visibility = View.VISIBLE
        log.setBackgroundColor(resources.getColor(R.color.blue2))
        log.text = "Log in"
        log.icon = resources.getDrawable(R.drawable.login_icon)
        log.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
        }
    }
}