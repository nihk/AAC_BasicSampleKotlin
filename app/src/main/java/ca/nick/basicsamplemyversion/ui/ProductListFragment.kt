package ca.nick.basicsamplemyversion.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.db.DataGenerator
import ca.nick.basicsamplemyversion.db.entities.ProductEntity
import ca.nick.basicsamplemyversion.models.Product
import ca.nick.basicsamplemyversion.viewmodels.ProductListVM
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import java.util.*

class ProductListFragment : Fragment() {

    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProductListVM
    private var listener: OnProductClickedListener? = null

    interface OnProductClickedListener {
        fun onProductClicked(product: Product)
    }

    companion object {
        val TAG = ProductListFragment::class.java.simpleName

        fun newInstance() = ProductListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        adapter = ProductAdapter(listener!!)
        view.products_list.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductListVM::class.java)
        viewModel.observableProducts.observe(this,
            Observer<List<ProductEntity>> { products ->
                if (products != null) {
                    loading.visibility = View.GONE
                    adapter.setProductList(products)
                } else {
                    loading.visibility = View.VISIBLE
                }
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnProductClickedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_product_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.add_product -> {
                viewModel.addProduct(DataGenerator.generateProduct())
                true
            }
            R.id.nuke -> {
                viewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
