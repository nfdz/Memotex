package io.github.nfdz.memotex.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.nfdz.memotex.R
import kotlinx.android.synthetic.main.activity_tutorial.*

fun Context.startTutorialActivity() {
    startActivity(Intent(this, TutorialActivity::class.java))
}

class TutorialActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setContentView(R.layout.activity_tutorial)
        val pagerAdapter = TutorialPagerAdapter(supportFragmentManager)
        tutorial_vp.adapter = pagerAdapter
        tutorial_iv_exit.setOnClickListener { finish() }
        tutorial_vp.addOnPageChangeListener(this)
        tutorial_iv_previous.setOnClickListener {
            tutorial_vp.setCurrentItem(tutorial_vp.currentItem - 1, true)
        }
        tutorial_iv_next.setOnClickListener {
            tutorial_vp.setCurrentItem(tutorial_vp.currentItem + 1, true)
        }
        updateProgress(tutorial_vp.currentItem)
    }

    override fun onPageScrollStateChanged(p0: Int) { }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }

    override fun onPageSelected(position: Int) {
        updateProgress(position)
    }

    private fun updateProgress(position: Int) {
        tutorial_tv_progress.text = "${position+1}/3"
        tutorial_iv_previous.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
        tutorial_iv_next.visibility = if (position == 2) View.INVISIBLE else View.VISIBLE
    }

    private class TutorialPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return TutorialFragment().apply { arguments = Bundle().apply { putInt("POSITION", position) } }
        }

        override fun getCount() = 3
    }

    class TutorialFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val position = arguments?.getInt("POSITION") ?: 0
            return inflater.inflate(when(position) {
                1 -> R.layout.fragment_tutorial_editor
                2 -> R.layout.fragment_tutorial_exercise_level
                else -> R.layout.fragment_tutorial_welcome
            }, container, false)
        }

    }

}
