package ca.nick.basicsamplemyversion.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.models.Comment
import ca.nick.basicsamplemyversion.viewmodels.ProductVM
import kotlinx.android.synthetic.main.comment_item.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlinx.android.synthetic.main.product_item.*

class ProductFragment : Fragment() {

    private lateinit var adapter: CommentAdapter
    private lateinit var viewModel: ProductVM
    private var listener: OnCommentClickedListener? = null

    interface OnCommentClickedListener {
        fun onCommentClicked(comment: Comment)
    }

    companion object {
        private const val KEY_PRODUCT_ID = "product_id"

        fun newInstance(productId: Int): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle()
            args.putInt(KEY_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        adapter = CommentAdapter(listener!!)
        view.comment_list.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vmFactory = ProductVM.Factory(activity!!.application, arguments!!.getInt(KEY_PRODUCT_ID))
        viewModel = ViewModelProviders.of(this, vmFactory).get(ProductVM::class.java)

        viewModel.observableProduct.observe(this,
            Observer { product ->
                product?.run {
                    this@ProductFragment.name.text = name
                    this@ProductFragment.price.text = price.toString()
                    this@ProductFragment.description.text = description
                }
            })

        viewModel.observableComments.observe(this,
            Observer { comments ->
                if (comments != null) {
                    loading_comments.visibility = View.GONE
                    adapter.setCommentList(comments)
                } else {
                    loading_comments.visibility = View.VISIBLE
                }
            })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnCommentClickedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
