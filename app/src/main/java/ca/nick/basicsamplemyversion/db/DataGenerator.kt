package ca.nick.basicsamplemyversion.db

import ca.nick.basicsamplemyversion.db.entities.CommentEntity
import ca.nick.basicsamplemyversion.db.entities.ProductEntity
import java.util.*
import java.util.concurrent.TimeUnit

object DataGenerator {

    private val FIRST = arrayOf("Special edition", "New", "Cheap", "Quality", "Used")

    private val SECOND = arrayOf("Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle")

    private val DESCRIPTION = arrayOf(
        "is finally here", "is recommended by Stan S. Stanman",
        "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"
    )

    private val COMMENTS =
        arrayOf("Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6")

    private var nextProductId = 0

    fun generateProducts(): List<ProductEntity> {
        val products = mutableListOf<ProductEntity>()
        val random = Random()

        for (i in 0 until FIRST.size) {
            for (j in 0 until SECOND.size) {
                val name = "${FIRST[i]} ${SECOND[j]}"

                products.add(
                    ProductEntity(
                        name = name,
                        description = "$name ${DESCRIPTION[j]}",
                        price = random.nextInt(240),
                        id = FIRST.size * i + j + 1
                    )
                )
            }
        }

        return products
    }

    fun generateProduct() =
        ProductEntity(
            id = nextProductId++,
            name = "$nextProductId: ${FIRST[nextProductId % FIRST.lastIndex]} ${SECOND[nextProductId % SECOND.lastIndex]}",
            description = "${FIRST[nextProductId % FIRST.lastIndex]} ${DESCRIPTION[nextProductId % DESCRIPTION.lastIndex]}",
            price = nextProductId * Random().nextInt(13)
        )

    fun generateCommentsForProducts(products: List<ProductEntity>): List<CommentEntity> {
        val comments = mutableListOf<CommentEntity>()
        val random = Random()

        products.forEach {
            val commentsNumber = random.nextInt(5) + 1

            (0 until commentsNumber).mapTo(comments) { i ->
                CommentEntity(
                    productId = it.id,
                    text = "${COMMENTS[i]} for ${it.name}",
                    postedAt = Date(
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(
                            (commentsNumber - 1).toLong()
                        ) + TimeUnit.HOURS.toMillis(1)
                    )
                )
            }
        }

        return comments
    }
}
