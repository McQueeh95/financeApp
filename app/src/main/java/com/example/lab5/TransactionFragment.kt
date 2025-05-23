package com.example.lab5

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab5.databinding.FragmentHomeBinding
import com.example.lab5.databinding.FragmentTransactionBinding
import com.example.lab5.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    //private lateinit var ratesAdapter: CurrencyRateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            val rates = fetchCurrencyRates()
            val currencyList = if (rates != null) {
                val usdToUah = rates["UAH"]
                val usdToEur = rates["EUR"]

                if (usdToUah == null || usdToEur == null) {
                    Toast.makeText(context, "Валюти UAH або EUR не знайдені в курсах", Toast.LENGTH_SHORT).show()
                    null
                } else {
                    val usdBuy = usdToUah * 0.995
                    val usdSell = usdToUah * 1.005

                    val eurToUah = usdToUah / usdToEur
                    val eurBuy = eurToUah * 0.995
                    val eurSell = eurToUah * 1.005

                    listOf(
                        MyCurrencyRate("USD", usdBuy, usdSell),
                        MyCurrencyRate("EUR", eurBuy, eurSell)
                    )
                }
            } else {
                null
            }

            if (currencyList != null) {
                val adapterForCurrency = CurrencyRateAdapter(currencyList) { currency ->
                    Toast.makeText(context, "To buy 1 ${currency.currency} you need ${String.format("%.4f", currency.buyRate)} UAH", Toast.LENGTH_SHORT).show()
                }

                binding.ratesRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.ratesRecyclerView.adapter = adapterForCurrency
            } else {
                Toast.makeText(context, "Помилка при отримані курсу валют", Toast.LENGTH_SHORT).show()
            }
        }


        transactionAdapter = TransactionAdapter(mutableListOf())
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRecyclerView.adapter = transactionAdapter

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        viewModel.transactionList.observe(viewLifecycleOwner) { updatedList ->
            val sortedList = updatedList.sortedByDescending { myTransaction ->
                SimpleDateFormat("dd/MM/yyyy").parse(myTransaction.timestamp) }
            transactionAdapter.updateList(sortedList)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    suspend fun fetchCurrencyRates(): Map<String, Double>? {
            return try {
                val response = RetrofitInstance.api.getRates("e9b49c9764b64e6c8f59eb71773c00f7")
                val ratesMap = response.rates
                ratesMap.filterKeys { it == "UAH" || it == "EUR"}
            } catch (e: Exception) {
                Log.e("CurrencyRates", "Error: ${e.localizedMessage}")
                null
            }
    }
}