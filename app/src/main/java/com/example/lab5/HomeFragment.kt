package com.example.lab5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab5.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import android.graphics.Color

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    //private var currentBalance: Double = 0.0

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
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater, container, false)
        //val transactionListView = view.transactionRecycleView
        //transactionListView.layoutManager = LinearLayoutManager(requireContext())
        //transactionListView.adapter = transactionA
        // Inflate the layout for this fragment
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionAdapter = TransactionAdapter(mutableListOf())
        binding.transactionRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionRecycleView.adapter = transactionAdapter

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        viewModel.transactionList.observe(viewLifecycleOwner) { updatedList ->
            val sortedList = updatedList.sortedByDescending { myTransaction ->
                SimpleDateFormat("dd/MM/yyyy").parse(myTransaction.timestamp) }
            transactionAdapter.updateList(sortedList)
        }

        viewModel.currentBalance.observe(viewLifecycleOwner) { updateBalance ->
            binding.CurrentBalance.text = String.format("Поточний баланс: %.2f₴", updateBalance)
            if(updateBalance < 0)
                binding.CurrentBalance.setTextColor(Color.RED)
            else
                binding.CurrentBalance.setTextColor(Color.WHITE)

        }

        viewModel.currentIncome.observe(viewLifecycleOwner) { updateIncome ->
            binding.currentIncome.text = String.format("Дохід: %.2f₴", updateIncome)
        }

        viewModel.currentExpenses.observe(viewLifecycleOwner) { updateExpenses ->
            if(updateExpenses >= 0) {
                binding.CurrentOutcome.text = String.format("Витрати: %.2f₴", updateExpenses)
            }
            else
                binding.CurrentOutcome.text = String.format("Витрати: %.2f₴", updateExpenses * -1)

        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}