package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import uz.itschool.shopping.R
import uz.itschool.shopping.databinding.FragmentUserBinding
import uz.itschool.shopping.model.User

private const val ARG_PARAM1 = "user"

class UserFragment : Fragment() {
    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_PARAM1) as User?
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(inflater, container, false)

        if (user == null){
            binding.userAccountInfoParent.visibility =  View.GONE
            val log:MaterialButton = binding.userLogInOut
            log.setBackgroundColor(resources.getColor(R.color.blue2))
            log.text = "Log in"
            log.icon = resources.getDrawable(R.drawable.login_icon)

            log.setOnClickListener {
                findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            }

        }else{
            binding.userNotLoggedInIv.visibility = View.GONE
        }

        return binding.root
    }
}