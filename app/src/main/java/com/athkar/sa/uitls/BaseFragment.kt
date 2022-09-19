package com.athkar.sa.uitls

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.athkar.sa.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

abstract class BaseFragment<T : ViewBinding>(private val inflater: (inflater: LayoutInflater, container: ViewGroup?) -> T) :
    Fragment() {

    protected val mainViewModel by activityViewModels<MainViewModel>()

    private var _binding: T? = null
    protected val binding get() = _binding!!

    open protected var shouldRemoveView = true
    val controller by lazy {
        findNavController()
    }

    abstract fun T.init()

    abstract fun observe()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (_binding == null) {
            _binding = inflater(inflater, container)
        }
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.init()

        observe()
    }

    fun <T> LiveData<T>.observe(onChange: (data: T) -> Unit) {
        observe(viewLifecycleOwner) {
            onChange(it)
        }

    }

    fun <T> Flow<T>.observeFlow(onChange: (data: T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect {
                    onChange(it)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (shouldRemoveView) {
            _binding = null
        }
    }
}