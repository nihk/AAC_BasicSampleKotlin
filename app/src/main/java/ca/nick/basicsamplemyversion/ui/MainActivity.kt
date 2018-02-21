package ca.nick.basicsamplemyversion.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.models.Comment
import ca.nick.basicsamplemyversion.models.Product

// TODO: unit/Android tests
class MainActivity : AppCompatActivity(), ProductListFragment.OnProductClickedListener,
    ProductFragment.OnCommentClickedListener {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    ProductListFragment.newInstance(),
                    ProductListFragment.TAG
                )
                .commit()
        }
    }

    override fun onProductClicked(product: Product) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("product")
            .replace(R.id.fragment_container, ProductFragment.newInstance(product.id), null)
            .commit()
    }

    override fun onCommentClicked(comment: Comment) {
        toast?.cancel()
        Toast.makeText(this, comment.text, Toast.LENGTH_SHORT)
            .also { toast = it }
            .show()
    }
}
