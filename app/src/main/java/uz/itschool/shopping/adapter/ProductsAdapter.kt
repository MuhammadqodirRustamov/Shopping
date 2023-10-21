package uz.itschool.shopping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uz.itschool.shopping.R
import uz.itschool.shopping.model.MyBottomSheet
import uz.itschool.shopping.model.Product
import uz.itschool.shopping.service.SharedPrefHelper
import kotlin.math.roundToInt

class ProductsAdapter(var products:List<Product>, private val context: Context, private val productPressed: ProductPressed, private val bottomSheetInterface: MyBottomSheet.BottomSheetInterface) : RecyclerView.Adapter<ProductsAdapter.MyHolder>() {
    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.product_item_image)
        val title : TextView = itemView.findViewById(R.id.product_item_title)
        val brand : TextView = itemView.findViewById(R.id.product_item_brand)
        val price : TextView = itemView.findViewById(R.id.product_item_price)
        val rating : TextView = itemView.findViewById(R.id.product_item_rating)
        val cart : FloatingActionButton = itemView.findViewById(R.id.product_item_cart_fab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = products[position]
        holder.image.load(product.images[0])
        holder.brand.text = product.brand
        holder.rating.text = ((product.rating*10).roundToInt().toDouble()/10).toString()
        holder.title.text = product.title
        holder.price.text = product.price.toString() + " $"
        val anim = AnimationUtils.loadAnimation(context, R.anim.appear)
        holder.itemView.startAnimation(anim)
        holder.itemView.setOnClickListener {
            productPressed.onPressed(product)
        }
        holder.cart.setOnClickListener {
            MyBottomSheet(context, product, bottomSheetInterface)
        }
    }

    interface ProductPressed{
        fun onPressed(product: Product)
    }


}