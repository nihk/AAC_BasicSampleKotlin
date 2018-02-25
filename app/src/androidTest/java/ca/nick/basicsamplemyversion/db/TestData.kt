package ca.nick.basicsamplemyversion.db

import ca.nick.basicsamplemyversion.db.entities.CommentEntity
import ca.nick.basicsamplemyversion.db.entities.ProductEntity
import java.util.*

object TestData {

    val PRODUCT_ENTITY = ProductEntity(
        1, "name", "desc",
        3
    )

    val PRODUCT_ENTITY2 = ProductEntity(
        2, "name2", "desc2",
        20
    )

    val PRODUCTS = Arrays.asList<ProductEntity>(
        PRODUCT_ENTITY,
        PRODUCT_ENTITY2
    )

    val COMMENT_ENTITY = CommentEntity(
        1, PRODUCT_ENTITY.id,
        "desc", Date()
    )

    val COMMENT_ENTITY2 = CommentEntity(
        2,
        PRODUCT_ENTITY2.id, "desc2", Date()
    )

    val COMMENTS = Arrays.asList<CommentEntity>(
        COMMENT_ENTITY,
        COMMENT_ENTITY2
    )
}