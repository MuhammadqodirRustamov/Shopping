package uz.itschool.shopping.ui

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
import uz.itschool.shopping.model.Product
import uz.itschool.shopping.model.ProductData
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import uz.itschool.shopping.service.SharedPrefHelper

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var lastSearch = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeAllRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.homeCategoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val api = APIClient.getInstance().create(APIService::class.java)
        val shared = SharedPrefHelper.getInstance(requireContext())
        val user = shared.getUser()
        if (shared != null) binding.homeAvatarIv.load(user.image)

        binding.homeAvatarIv.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user", user)
            findNavController().navigate(R.id.action_productFragment_to_userFragment, bundle)
        }


        api.getAll().enqueue(object : Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                val products = response.body()?.products!!
                changeProductsAdapter(products)
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
        api.getCategories().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val categories = response.body()!!
                binding.homeCategoryRv.adapter = CategoryAdapter(
                    categories,
                    requireContext(),
                    binding.homeCategoryRv,
                    object : CategoryAdapter.CategoryPressed {
                        override fun onPressed(category: String) {
                            if (category == "") {
                                api.getAll().enqueue(object : Callback<ProductData>{
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

            override fun onFailure(call: Call<List<String>>, t: Throwable) {

            }

        })

        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == lastSearch) return false

                api.search(query!!).enqueue(object : Callback<ProductData>{
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
                lastSearch = query

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })






        binding.homeFilterFab.setOnClickListener {
            if (binding.homeCategoryRv.isVisible) binding.homeCategoryRv.visibility = View.GONE
            else binding.homeCategoryRv.visibility = View.VISIBLE
        }

        return binding.root
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
            })
    }

}