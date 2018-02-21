package ca.nick.basicsamplemyversion.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ca.nick.basicsamplemyversion.DataRepository
import ca.nick.basicsamplemyversion.MyBasicApp

class ProductVM(
    application: Application,
    repository: DataRepository,
    productId: Int
) : AndroidViewModel(application) {

    val observableProduct = repository.loadProduct(productId)
    val observableComments = repository.loadComments(productId)

    class Factory(
        private val application: Application,
        private val productId: Int
    ) : ViewModelProvider.NewInstanceFactory() {

        private val repository = (application as MyBasicApp).getRepository()

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductVM::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductVM(application, repository, productId) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}