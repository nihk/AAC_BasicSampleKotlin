package ca.nick.basicsamplemyversion.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.models.Comment
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter(
    private val listener: ProductFragment.OnCommentClickedListener
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var commentList: List<Comment> = emptyList()

    fun setCommentList(commentList: List<Comment>) {
        if (this@CommentAdapter.commentList.isEmpty()) {
            this@CommentAdapter.commentList = commentList
            notifyItemRangeInserted(0, commentList.size)
        } else {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = this@CommentAdapter.commentList.size

                override fun getNewListSize() = commentList.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    this@CommentAdapter.commentList[oldItemPosition].id == commentList[newItemPosition].id

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldComment = this@CommentAdapter.commentList[oldItemPosition]
                    val newComment = commentList[newItemPosition]

                    return oldComment.id == newComment.id
                            && oldComment.postedAt == newComment.postedAt
                            && oldComment.text == newComment.text
                }
            })

            this@CommentAdapter.commentList = commentList
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
            .let { CommentViewHolder(it) }
    }

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) =
        holder.bindComment(commentList[position], listener)

    class CommentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindComment(comment: Comment, listener: ProductFragment.OnCommentClickedListener) {
            with(view) {
                text.text = comment.text
                posted_at.text = comment.postedAt.toString()
                setOnClickListener { listener.onCommentClicked(comment) }
            }
        }
    }
}