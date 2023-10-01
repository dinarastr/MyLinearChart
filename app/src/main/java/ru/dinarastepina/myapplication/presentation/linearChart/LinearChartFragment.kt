package ru.dinarastepina.myapplication.presentation.linearChart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.dinarastepina.myapplication.R
import ru.dinarastepina.myapplication.databinding.FragmentLinearChartBinding

class LinearChartFragment : Fragment() {

    private var _vb: FragmentLinearChartBinding? = null
    private val vb: FragmentLinearChartBinding
        get() = _vb!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = FragmentLinearChartBinding.inflate(inflater, container, false)

        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }
}