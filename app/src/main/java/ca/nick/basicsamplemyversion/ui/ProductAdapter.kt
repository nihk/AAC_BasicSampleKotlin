package ca.nick.basicsamplemyversion.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.models.Product
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter(
    private val listener: ProductListFragment.OnProductClickedListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList: List<Product> = emptyList()

    fun setProductList(productList: List<Product>) {
        if (this@ProductAdapter.productList.isEmpty()) {
            this@ProductAdapter.productList = productList
            notifyItemRangeInserted(0, productList.size)
        } else {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = this@ProductAdapter.productList.size

                override fun getNewListSize() = productList.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    this@ProductAdapter.productList[oldItemPosition].id == productList[newItemPosition].id

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldProduct = this@ProductAdapter.productList[oldItemPosition]
                    val newProduct = productList[newItemPosition]

                    return oldProduct.id == newProduct.id
                            && oldProduct.description == newProduct.description
                            && oldProduct.name == newProduct.name
                            && oldProduct.price == newProduct.price
                }
            })

            this@ProductAdapter.productList = productList
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
            .let { ProductViewHolder(it) }
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bindProduct(productList[position], listener)

    class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindProduct(product: Product, listener: ProductListFragment.OnProductClickedListener) {
            with(view) {
                name.text = product.name
                price.text = product.price.toString()
                description.text = product.description
                setOnClickListener { listener.onProductClicked(product) }
            }
        }
    }
}