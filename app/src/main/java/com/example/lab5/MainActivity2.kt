package com.example.lab5
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import com.example.lab5.MyTransaction

import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter

import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab5.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(view.root)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dateInput = view.editTextDate

        val toggleGroup = view.typeToggleGroup
        val buttonExpence = view.btnExpense
        val buttonIncome = view.btnIncome

        buttonExpence.isChecked = true

        val categoryAdapter = ArrayAdapter.createFromResource(
            this,R.array.category_array,
            android.R.layout.simple_spinner_item
        )

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        view.spinner.adapter = categoryAdapter
        view.spinner.setSelection(0)


        toggleGroup.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            when(checkedId){
                R.id.btnExpense ->
                {
                    if(isChecked){
                        buttonExpence.setBackgroundColor(Color.parseColor("#3F51B5"))
                        buttonIncome.setBackgroundColor(Color.TRANSPARENT)
                        buttonIncome.setTextColor(Color.BLACK)
                        buttonExpence.setTextColor(Color.WHITE)
                    }
                }
                R.id.btnIncome ->
                {
                    if(isChecked){
                        buttonExpence.setBackgroundColor(Color.TRANSPARENT)
                        buttonIncome.setBackgroundColor(Color.parseColor("#3F51B5"))
                        buttonExpence.setTextColor(Color.BLACK)
                        buttonIncome.setTextColor(Color.WHITE)
                    }
                }
            }


        }


        val addButton = view.addIncomeButton

        dateInput.setOnTouchListener(){ _, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                dateInput.performClick()
                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, selectedYear, selectedMount, selectedDay ->
                        dateInput.setText("$selectedDay/${selectedMount + 1}/$selectedYear")
                    },
                    year, month, dayOfMonth
                )
                datePickerDialog.show()
                return@setOnTouchListener true
            }
            false
            }

        /*dateInput.setOnClickListener{
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMount, selectedDay ->
                    dateInput.setText("$selectedDay/${selectedMount + 1}/$selectedYear")
                },
                year, month, dayOfMonth
            )
            datePickerDialog.show()
        }*/
        addButton.setOnClickListener {
            val resTrunsaction = getTransactionFromInput(view)
            if(resTrunsaction != null){
                val intent = Intent()
                intent.putExtra("transaction", resTrunsaction) as? MyTransaction
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else{
                Toast.makeText(this, "Заповніть усі поля", Toast.LENGTH_SHORT).show()
            }
        }

        view.imageButton.setOnClickListener{
            finish()
        }
    }
}

private fun getTransactionFromInput(view: ActivityMain2Binding): MyTransaction?{
    val amountText = view.editTextNumberSigned.text.toString()
    val description = view.editTextText.text.toString()
    val date = view.editTextDate.text.toString()
    //val category = view.autoCompleteTextView.text.toString()
    val category = view.spinner.selectedItem.toString()
    val isIncome = view.typeToggleGroup.checkedButtonId == R.id.btnIncome


    if(amountText.isBlank() || date.isBlank() || category.isBlank())
    {
        return null
    }

    var amount = amountText.toDoubleOrNull() ?: return null

    if(!isIncome)
        amount *= -1

    return MyTransaction(
        amount = amount,
        description = description,
        category = category,
        timestamp = date,
        isIncome = isIncome
    )
}
