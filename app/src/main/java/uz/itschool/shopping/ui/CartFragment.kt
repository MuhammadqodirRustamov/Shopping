package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import retrofit2.Call
import retrofit2.Response
import uz.itschool.shopping.adapter.CartProductsAdapter
import uz.itschool.shopping.databinding.FragmentCartBinding
import uz.itschool.shopping.model.CartData
import uz.itschool.shopping.model.Product
import uz.itschool.shopping.model.ProductX
import uz.itschool.shopping.model.User
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import uz.itschool.shopping.service.SharedPrefHelper

private const val ARG_PARAM1 = "product"
private const val ARG_PARAM2 = "quantity"

class CartFragment : Fragment() {
    var product: Product? = null
    private lateinit var binding: FragmentCartBinding
    private var quantity: Int = 0
    private val api: APIService = APIClient.getInstance().create(APIService::class.java)
    private lateinit var user: User
    private var didLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
            quantity = it.getInt(ARG_PARAM2)
            didLogin = it.getBoolean("didLogin")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shared = SharedPrefHelper.getInstance(requireContext())
        user = shared.getUser()!!

        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.cartRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartBackFab.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        setCart()
        setuser()
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (didLogin) {
                    findNavController().popBackStack()
                }
                findNavController().popBackStack()
            }

        })
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setuser() {
        binding.cartUserImage.load(user.image)
        binding.cartName.text = user.firstName + " " + user.lastName
        binding.cartUsername.text = "@${user.username}"
        binding.cartEmail.text = user.email
    }

    private fun setCart() {
        api.getCartsOfUser(user.id).enqueue(object : retrofit2.Callback<CartData> {
            override fun onResponse(call: Call<CartData>, response: Response<CartData>) {
                if (response.isSuccessful) {
                    val products =
                        if (response.body()!!.carts.isNotEmpty()) response.body()!!.carts[0].products.toMutableList() else mutableListOf()
                    if (product != null) {
                        val productX = ProductX(
                            0.0,
                            0.0,
                            product!!.id,
                            product!!.price,
                            quantity,
                            product!!.thumbnail,
                            product!!.title,
                            product!!.price * quantity
                        )
                        products.add(0, productX)
                    }
                    binding.cartRecycler.adapter = CartProductsAdapter(products)
                }
            }

            override fun onFailure(call: Call<CartData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }
}