package com.eclatsol.userblinkitclone

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.eclatsol.userblinkitclone.adapters.AdapterCategory
import com.eclatsol.userblinkitclone.databinding.FragmentHomeBinding
import com.eclatsol.userblinkitclone.models.Category


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStatusBarColor()
        setAllCategories()
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()
        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Category(Constants.allProductsCategory[i],Constants.allProductsCategoryIcon[i]))
        }

        binding.rvCategories.adapter = AdapterCategory(categoryList)
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusColors = ContextCompat.getColor(requireContext(), R.color.orange)
            statusBarColor = statusColors
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}