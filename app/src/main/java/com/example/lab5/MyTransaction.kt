package com.example.lab5
import android.content.Context
import android.graphics.Color
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.SurfaceControl.Transaction
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5.databinding.FragmentHomeBinding
import com.example.lab5.databinding.ItemTransactionBinding
import java.io.Serializable

data class MyTransaction(
    val amount: Double,
    val description: String,
    val category: String,
    val timestamp: String,
    val isIncome: Boolean
):Serializable

class TransactionViewModel: ViewModel()
{
    private val _currentBalance = MutableLiveData<Double>().apply { value = 0.0 }
    val currentBalance: LiveData<Double> get() = _currentBalance

    private val _currentIncome = MutableLiveData<Double>().apply { value = 0.0 }
    val currentIncome: LiveData<Double> get() = _currentIncome

    private val _currentExpenses = MutableLiveData<Double>().apply { value = 0.0 }
    val currentExpenses: LiveData<Double> get() = _currentExpenses

    private val _transactionList = MutableLiveData<List<MyTransaction>>(emptyList())
    val transactionList: LiveData<List<MyTransaction>> = _transactionList

    fun addTransaction(transaction: MyTransaction){
        val updateList = _transactionList.value.orEmpty().toMutableList()
        updateList.add(transaction)
        _transactionList.value = updateList

        val newBalance = _currentBalance.value?.plus(transaction.amount) ?: transaction.amount
        _currentBalance.value = newBalance

        if(transaction.amount > 0) {
            val newIncome = _currentIncome.value?.plus(transaction.amount) ?: transaction.amount
            _currentIncome.value = newIncome
        }
        else
        {
            val newExpenses = _currentExpenses.value?.plus(transaction.amount) ?: transaction.amount
            _currentExpenses.value = newExpenses
        }
    }

    fun setTransaction(transactions: List<MyTransaction>){
        _transactionList.value = transactions
    }


}


class TransactionAdapter(private val transactions: MutableList<MyTransaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        if(transaction.amount > 0)
            holder.binding.amountTextView.text = String.format("+%.2f₴", transaction.amount)
        else
            holder.binding.amountTextView.text = String.format("%.2f₴", transaction.amount)
        holder.binding.descriptionTextView.text = transaction.description
        holder.binding.categoryTextView.text = transaction.category
        holder.binding.timestampTextView.text = transaction.timestamp
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newList: List<MyTransaction>) {
        transactions.clear()
        transactions.addAll(newList)
        notifyDataSetChanged()
    }
}