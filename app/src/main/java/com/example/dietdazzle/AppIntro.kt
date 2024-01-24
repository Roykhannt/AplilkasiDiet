package com.example.dietdazzle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dietdazzle.AppIntro.Companion.MAX_STEP
import com.example.dietdazzle.databinding.IntroAppContentBinding
import com.example.dietdazzle.databinding.IntroAppDesignBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator

class AppIntro : Fragment() {
    private var _binding: IntroAppContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.intro_app_content, container, false)
        _binding = IntroAppContentBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //............................................................
        binding.viewPager2.adapter = AppIntroViewPager2Adapter()

        //............................................................
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
        }.attach()

        //............................................................
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(position== MAX_STEP -1){
                    binding.btnNext.text                  = getString(R.string.intro_next)//"Next"
                    binding.btnNext.contentDescription    = getString(R.string.intro_next)//"Next"
                    binding.btnNext.visibility = MaterialButton.VISIBLE
                }else{
                    binding.btnNext.visibility = MaterialButton.INVISIBLE
                }
            }
        })

        //............................................................
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_appIntro_to_loginActivity)
        }
    }

    companion object {
        const val MAX_STEP = 3
    }
}
//...............................................................................


//................................................................................
class AppIntroViewPager2Adapter : RecyclerView.Adapter<PagerVH2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH2 {
        return PagerVH2(
            IntroAppDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //get the size of color array
    override fun getItemCount(): Int = MAX_STEP // Int.MAX_VALUE

    //binding the screen with view
    override fun onBindViewHolder(holder: PagerVH2, position: Int) = holder.itemView.run {

        with(holder) {
            if (position == 0) {
                bindingDesign.introTitle.text = context.getString(R.string.intro_title_1)
                bindingDesign.introDescription.text = context.getString(R.string.intro_description_1)
                bindingDesign.introImage.setImageResource(R.drawable.woman1)
            }
            if (position == 1) {
                bindingDesign.introTitle.text = context.getString(R.string.intro_title_2)
                bindingDesign.introDescription.text = context.getString(R.string.intro_description_2)
                bindingDesign.introImage.setImageResource(R.drawable.woman2)
            }
            if (position == 2) {
                bindingDesign.introTitle.text = context.getString(R.string.intro_title_3)
                bindingDesign.introDescription.text = context.getString(R.string.intro_description_3)
                bindingDesign.introImage.setImageResource(R.drawable.woman3)
            }
        }
    }
}
class PagerVH2(val bindingDesign: IntroAppDesignBinding) : RecyclerView.ViewHolder(bindingDesign.root)

