package com.example.android_online_store.project.activities.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.databinding.FragmentProductsBinding
import com.example.android_online_store.project.activities.*
import com.example.android_online_store.project.adapters.ProductsListAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.Product


class ProductsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onResume(){
        super.onResume()
        getProdListFromDB()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when(id){
            R.id.action_newproduct -> {

                startActivity(Intent(activity, NewProductActivity::class.java))

                return true

            }

            R.id.my_orders -> {

                startActivity(Intent(activity, MyOrdersActivity::class.java))

                return true

            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartActivity::class.java))
                return true
            }
            R.id.sold_products -> {
                startActivity(Intent(activity, SoldProductActivity::class.java))
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun getProdListFromDB(){

        FirestoreController().getProductsList(this)

    }


    fun successGetProdFromDB(prodList: ArrayList<Product>){

        val rv: RecyclerView = binding.rvMyProductItems
        val tv: TextView = binding.tvNoProductsFound

        if(prodList.size > 0){

            rv.visibility = View.VISIBLE
            tv.visibility = View.GONE

            rv.layoutManager = LinearLayoutManager(activity)
            rv.setHasFixedSize(true)
            val adapter = ProductsListAdapter(requireActivity(), prodList, this)
            rv.adapter = adapter
        }else{
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE
        }

    }

    fun deleteProduct(prodID: String){
        FirestoreController().deleteProd(this, prodID)
    }

    fun prodDeleteSuccess(){
        Toast.makeText(requireActivity(),"Product deleted successfully !",Toast.LENGTH_SHORT).show()
        getProdListFromDB()
    }

}