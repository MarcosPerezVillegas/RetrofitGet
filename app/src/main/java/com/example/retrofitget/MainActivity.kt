package com.example.retrofitget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitget.Adapter.PerroAdapter
import com.example.retrofitget.Class.APIServicio
import com.example.retrofitget.Class.ClassPerro
import com.example.retrofitget.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    android.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PerroAdapter
    private val imagenesPerros = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svPerro.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = PerroAdapter(imagenesPerros)
        binding.rvPerro.layoutManager=LinearLayoutManager(this)
        binding.rvPerro.adapter=adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buscarXNombre(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<ClassPerro> = getRetrofit().create(APIServicio::class.java).getPerrosXRaza("$query/images")
            val Perros:ClassPerro?=call.body()
            runOnUiThread {
                if(call.isSuccessful){
                    val imagenes:List<String> = Perros?.imagenes ?: emptyList()
                    imagenesPerros.clear()
                    imagenesPerros.addAll(imagenes)
                    adapter.notifyDataSetChanged()
                }
                else{
                    showError()
                }
                ocultarTeclado()
            }
        }

    }

    private fun ocultarTeclado() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            buscarXNombre(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}