package com.athkar.sa.ui.homeScreen.athkar.athkarItem

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.athkar.sa.Constants
import com.athkar.sa.R
import com.athkar.sa.databinding.FragmentAthkarItemBinding
import com.athkar.sa.db.entity.Athkar
import com.athkar.sa.ui.homeScreen.athkar.AthkarViewModel
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AthkarItemFragment : BaseFragment<FragmentAthkarItemBinding>({ inflater, container ->
    FragmentAthkarItemBinding.inflate(inflater, container, false)
}) {

    private val viewModel by viewModels<AthkarViewModel>({ requireParentFragment() })

    private lateinit var althker: Athkar
    override fun FragmentAthkarItemBinding.init() {
        althker = arguments?.getParcelable<Athkar>(Constants.ATHKAR_BUNDLE_KEY_OBJECT)!!
        setAthkars(althker)
        container.setOnClickListener {
            viewModel.updateEvent()
        }
    }


    private fun setAthkars(athkar: Athkar) {
        if (athkar.QuranContent != null) {
            val quran = athkar.QuranContent
            binding.bQuran.text = quran.getBasmlah()
            binding.bQuran.isVisible = true
            binding.content.text = quran.surah
            binding.source.text = quran.getAyah()
            binding.content.typeface = ResourcesCompat.getFont(requireContext(), R.font.quran1)
        } else if (athkar.hadithContent != null) {
            val hadith = athkar.hadithContent
            binding.bQuran.isVisible = false
            if (hadith.listHadith != null) {
                val typeValue = TypedValue()
                requireContext().theme.resolveAttribute(
                    com.google.android.material.R.attr.colorPrimary,
                    typeValue,
                    true
                )
                hadith.listHadith.forEachIndexed { index, s ->
                    val spanText = spanContent(s, typeValue.data)
                    binding.content.append(spanText)
                    binding.content.append("\n\n")
                }
            } else {
                binding.content.text = hadith.hadith
            }
            binding.source.text = "${athkar.strength}"
            binding.source.isVisible = athkar.strength != null

        } else {
            throw Exception("This object dose not have any content $athkar")
        }
        if (athkar.times > 0) {
            showTimes(athkar.times)
        } else {
            hideTimes()
        }
        if (athkar.howTo != null) {
            binding.howTo.isVisible = true
            binding.howTo.text = athkar.howTo
        } else {
            binding.howTo.isVisible = false
        }
        if (athkar.titleAlthker != null) {
            binding.tvTitleAlthker.isVisible = true
            
            binding.tvTitleAlthker.text = athkar.titleAlthker
        } else {
            binding.tvTitleAlthker.isVisible = false
        }
    }

    private fun showTimes(times: Int) {
        binding.containerTimes.isVisible = true
        binding.tvTimes.text = times.toString().plus(" مرات")
    }

    private fun hideTimes() {
        binding.containerTimes.isVisible = false
    }

    override fun observe() {

    }

    private fun spanContent(text: String, color: Int): CharSequence {
        val hasDots = text.contains(":")
        val hasPrinthess = text.contains("(")
        val getSpan = text.toSpannable()
        return if (hasDots && hasPrinthess){
            val getIndex = text.indexOf(":")+1
            val indexOf1 = text.indexOf("(")
            val indexOf2 = text.indexOf(")")+1
            getSpan.set(0,getIndex,ForegroundColorSpan(color))
            getSpan.set(indexOf1,indexOf2,ForegroundColorSpan(color))
            getSpan
        } else if (hasDots) {
            val getIndex = text.indexOf(":")+1
            getSpan.setSpan(
                ForegroundColorSpan(color),
                0,
                getIndex,
                0
            )
            getSpan
        } else if (hasPrinthess) {
            val indexOf1 = text.indexOf("(")
            val indexOf2 = text.indexOf(")")+1
            getSpan.setSpan(
                ForegroundColorSpan(color),
                indexOf1,
                indexOf2,
               0
            )
            getSpan
        }else{
            text
        }
    }
//    private fun spanContent(text:String):CharSequence{
//        val regex = "".toRegex()
//        val spiltText = text.split(regex)
//    }
}