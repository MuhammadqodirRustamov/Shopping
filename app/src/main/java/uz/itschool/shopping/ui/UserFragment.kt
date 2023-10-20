package uz.itschool.shopping.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import uz.itschool.shopping.R
import uz.itschool.shopping.databinding.FragmentUserBinding
import uz.itschool.shopping.model.User

private const val ARG_PARAM1 = "user"

class UserFragment : Fragment() {
    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_PARAM1) as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater, container, false)

        if (user == null){
            Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}