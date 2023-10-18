package uz.itschool.shopping.ui

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import uz.itschool.shopping.R
import uz.itschool.shopping.adapter.ImageAdapter
import uz.itschool.shopping.databinding.FragmentProductBinding
import uz.itschool.shopping.model.Product
import kotlin.math.roundToInt

private const val ARG_PARAM1 = "product"

class ProductFragment : Fragment() {
    private lateinit var product: Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.productScreenBackFab.setOnClickListener {
            requireActivity().onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.productIamgeVp.layoutParams.height == LayoutParams.MATCH_PARENT) {
                    binding.productIamgeVp.updateLayoutParams {
                        height = 200.toPx(requireContext())
                    }
                    binding.productParentConstraint.setBackgroundColor(Color.WHITE)
                } else {
                    findNavController().popBackStack()
                }
            }
        })
        binding.productIamgeVp.adapter =
            ImageAdapter(product.images, binding.productIamgeVp, binding.productParentConstraint)



        binding.productScreenTitle.text = product.title
        binding.productScreenBrand.text = product.brand
        binding.productScreenPrice.text = product.price.toString() + " $"
        binding.productScreenDescription.text = product.description
        binding.productScreenRating.text =
            ((product.rating * 10).roundToInt().toDouble() / 10).toString()





        return binding.root
    }

    fun Int.toPx(context: Context) =
        this * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

}