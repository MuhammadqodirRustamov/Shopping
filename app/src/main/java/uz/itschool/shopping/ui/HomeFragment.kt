package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.shopping.R
import uz.itschool.shopping.adapter.CategoryAdapter
import uz.itschool.shopping.adapter.ProductsAdapter
import uz.itschool.shopping.databinding.FragmentHomeBinding
import uz.itschool.shopping.model.Category
import uz.itschool.shopping.model.MyBottomSheet
import uz.itschool.shopping.model.Product
import uz.itschool.shopping.model.ProductData
import uz.itschool.shopping.model.User
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import uz.itschool.shopping.service.SharedPrefHelper

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var lastSearch = ""
    private var user: User? = null
    val api: APIService = APIClient.getInstance().create(APIService::class.java)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeAllRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.homeCategoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        checkUser()
        setAllProducts()
        setCategories()
        setSearchListener()
        setAvatarClickListener()
        setFilterListener()

        return binding.root
    }

    private fun checkUser() {
        val shared = SharedPrefHelper.getInstance(requireContext())
        user = shared.getUser()
        if (user != null) binding.homeAvatarIv.load(user!!.image)
    }

    private fun setCategories() {
        api.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                val categories = response.body()!!
                binding.homeCategoryRv.adapter = CategoryAdapter(
                    categories,
                    requireContext(),
                    binding.homeCategoryRv,
                    object : CategoryAdapter.CategoryPressed {
                        override fun onPressed(category: String) {
                            if (category == "") {
                                api.getAll().enqueue(object : Callback<ProductData> {
                                    override fun onResponse(
                                        call: Call<ProductData>,
                                        response: Response<ProductData>
                                    ) {
                                        val products = response.body()!!.products
                                        changeProductsAdapter(products)
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG", "$t")
                                    }

                                })
                            } else {
                                api.getByCategory(category).enqueue(object : Callback<ProductData> {
                                    override fun onResponse(
                                        call: Call<ProductData>,
                                        response: Response<ProductData>
                                    ) {
                                        val products = response.body()?.products!!
                                        changeProductsAdapter(products)
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG", "$t")
                                    }
                                })
                            }
                        }

                    })
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {

            }

        })
    }

    private fun setAllProducts() {
        api.getAll().enqueue(object : Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                val products = response.body()?.products!!
                Log.d("TAG", "onResponse: ${products}")
                changeProductsAdapter(products)
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }

    private fun setSearchListener() {
        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == lastSearch) return false
                api.search(newText!!).enqueue(object : Callback<ProductData> {
                    override fun onResponse(
                        call: Call<ProductData>,
                        response: Response<ProductData>
                    ) {
                        val products = response.body()!!.products
                        changeProductsAdapter(products)
                    }

                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                        Log.d("TAG", "$t")
                    }

                })
                lastSearch = newText
                return true
            }
        })
    }

    private fun setAvatarClickListener() {
        binding.homeAvatarIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userFragment)
        }
    }
    private fun setFilterListener(){
        binding.homeFilterFab.setOnClickListener {
            if (binding.homeCategoryRv.isVisible) binding.homeCategoryRv.visibility = View.GONE
            else binding.homeCategoryRv.visibility = View.VISIBLE
        }
    }

    fun changeProductsAdapter(products: List<Product>) {
        binding.homeAllRv.adapter =
            ProductsAdapter(products, requireContext(), object : ProductsAdapter.ProductPressed {
                override fun onPressed(product: Product) {
                    val bundle = Bundle()
                    bundle.putSerializable("product", product)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_productFragment,
                        bundle
                    )
                }
            }, object : MyBottomSheet.BottomSheetInterface{
                override fun onAdd(product: Product, quantity: Int) {
                    val shared = SharedPrefHelper.getInstance(requireContext())
                    val bundle = Bundle()
                    bundle.putInt("quantity", quantity)
                    bundle.putSerializable("product", product)
                    if (shared.getUser() == null){
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment, bundle)
                    }else{
                        findNavController().navigate(R.id.action_homeFragment_to_cartFragment, bundle)
                    }
                }

            })
    }
}