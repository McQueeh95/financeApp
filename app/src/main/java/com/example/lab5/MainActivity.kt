package com.example.lab5

import android.app.Activity
import android.bluetooth.le.TransportBlock
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl.Transaction
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.lab5.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import com.example.lab5.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val transactionList = mutableListOf<Transaction>()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: TransactionViewModel
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        setContentView(view.root)

        //fetchCurrencyRates()
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val newTransaction = result.data?.getSerializableExtra("transaction") as? MyTransaction
                newTransaction?.let { viewModel.addTransaction(it)
                }
            }
        }
        val addTransactionButton = view.floatingActionButton
        addTransactionButton.setOnClickListener()
        {
            val intent = Intent(this, MainActivity2::class.java)
            resultLauncher.launch(intent)
        }
        view.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.homeFragment -> loadFragment(HomeFragment())
                R.id.transactionFragment -> loadFragment(TransactionFragment())
                R.id.settingsFragment -> loadFragment(SettingsFragment())
                else -> false
            }
        }
    }


    private fun loadFragment(fragment: Fragment): Boolean{
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
        return true
    }

    /*private fun fetchCurrencyRates() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getRates("e9b49c9764b64e6c8f59eb71773c00f7")
                Log.d("CurrencyRates", "Rates: ${response.rates}")
            } catch (e: Exception) {
                Log.e("CurrencyRates", "Error: ${e.localizedMessage}")
            }
        }
    }*/

}