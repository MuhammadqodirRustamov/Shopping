package uz.itschool.shopping.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.shopping.R
import uz.itschool.shopping.databinding.FragmentHomeBinding
import uz.itschool.shopping.model.ProductData
import uz.itschool.shopping.networking.APIClient
import uz.itschool.shopping.networking.APIService
import kotlin.math.log

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val api = APIClient.getInstance().create(APIService::class.java)

//        api.getAll().enqueue(object : Callback<ProductData>{
//            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
//                Log.d("TAG", "onResponse: ${response.body()?.products}")
//            }
//
//            override fun onFailure(call: Call<ProductData>, t: Throwable) {
//                Log.d("TAG", "$t")
//            }
//        })

        return binding.root
    }

}