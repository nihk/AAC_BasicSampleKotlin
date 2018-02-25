package ca.nick.basicsamplemyversion

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

object EspressoTestUtil {

    fun disableAnimations(activityTestRule: ActivityTestRule<out FragmentActivity>) {
        activityTestRule
            .activity
            .supportFragmentManager
            .registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        v: View,
                        savedInstanceState: Bundle?
                    ) {
                        traverseViews(v)
                    }
                }, true
            )
    }

    fun traverseViews(view: View) {
        if (view is ViewGroup) {
            traverseViewGroup(view)
        } else {
            if (view is ProgressBar) {
                disableProgressBarAnimation(view)
            }
        }
    }

    fun traverseViewGroup(viewGroup: ViewGroup) {
        if (viewGroup is RecyclerView) {
            disableRecyclerViewAnimations(viewGroup)
        } else {
            val count = viewGroup.childCount
            for (i in 0 until count) {
                traverseViews(viewGroup.getChildAt(i))
            }
        }
    }

    fun disableProgressBarAnimation(progressBar: ProgressBar) {
        progressBar.indeterminateDrawable = ColorDrawable(Color.BLUE)
    }

    fun disableRecyclerViewAnimations(recyclerView: RecyclerView) {
        recyclerView.itemAnimator = null
    }

}