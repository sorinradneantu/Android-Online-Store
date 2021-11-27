package com.example.android_online_store.project.activities.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.databinding.FragmentDashboardBinding
import com.example.android_online_store.project.activities.SettingsActivity
import com.example.android_online_store.project.adapters.AllProductsListAdapter
import com.example.android_online_store.project.adapters.ProductsListAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.Product

class DashboardFragment : Fragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        getAllProductsList()
    }

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when(id){
            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))

                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successGetAllProductsList(dashboardProductsList: ArrayList<Product>){

        val rv: RecyclerView = binding.rvDashboardItems
        val tv: TextView = binding.tvNoDashboardItemsFound


        if(dashboardProductsList.size > 0){
            tv.visibility = View.GONE
            rv.visibility = View.VISIBLE

            rv.layoutManager = LinearLayoutManager(activity)
            rv.setHasFixedSize(true)
            val adapter = AllProductsListAdapter(requireActivity(), dashboardProductsList)
            rv.adapter = adapter

        }else{
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE
        }

    }

    private fun getAllProductsList(){
        FirestoreController().getAllProductsList(this@DashboardFragment)
    }

}