package fr.isen.mollinari.androidktntoolbox


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class LifeCycleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLogInActivity("Cycle de vie fragment : onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView = inflater.inflate(R.layout.fragment_life_cycle, container, false)
        showLogInActivity("Cycle de vie fragment : onCreateView")
        return fragView
    }

    private fun showLogInActivity(logMessage: String) {
        if (activity != null) {
            (activity as LifeCycleActivity).showLog(logMessage)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        showLogInActivity("Cycle de vie fragment : onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        showLogInActivity("Cycle de vie fragment : onDetach")
    }

    override fun onResume() {
        super.onResume()
        showLogInActivity("Cycle de vie fragment : onResume")
    }

    override fun onPause() {
        super.onPause()
        showLogInActivity("Cycle de vie fragment : onPause")
    }

    override fun onStop() {
        super.onStop()
        showLogInActivity("Cycle de vie fragment : onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        showLogInActivity("Cycle de vie fragment : onDestroy")
    }

    companion object {
        fun newInstance() = LifeCycleFragment()
    }
}
