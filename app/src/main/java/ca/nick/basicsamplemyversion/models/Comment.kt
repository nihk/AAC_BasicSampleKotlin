package ca.nick.basicsamplemyversion.models

import java.util.Date

interface Comment {
    val id: Int
    val productId: Int
    val text: String
    val postedAt: Date
}