package uz.itschool.shopping.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.shopping.databinding.FragmentLoginBinding
import uz.itschool.shopping.model.Login
import uz.itschool.shopping.model.Product
import uz.itschool.shopping.model.User
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import uz.itschool.shopping.service.SharedPrefHelper

private const val ARG_PARAM1 = "product"

class LoginFragment : Fragment() {
    private var product : Product? = null
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setLoginClickedListener()
        setBackButtonListener()

        return binding.root
    }

    private fun setBackButtonListener() {
        binding.loginBackFab.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun setLoginClickedListener() {
        binding.loginLoginMb.setOnClickListener {
            val username : String = binding.loginUsername.text.toString()
            val password : String = binding.loginPassword.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(requireContext(), "Complete the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val api = APIClient.getInstance().create(APIService::class.java)
            api.login(Login(username, password)).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(requireContext(), "Incorrect login or password", Toast.LENGTH_SHORT).show()
                        binding.loginPassword.setText("")
                        return
                    }
                    if (product == null){
                        val shared = SharedPrefHelper.getInstance(requireContext())
                        val user = response.body()!!
                        shared.setUser(user)
                        requireActivity().onBackPressed()
                    }else{
                        //TODO: Go to cart
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("TAG", "$t")
                }

            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
        }
    }
}