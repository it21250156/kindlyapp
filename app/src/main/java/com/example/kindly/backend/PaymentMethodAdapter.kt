package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.PaymentMethodDB
import com.example.kindly.R

class PaymentMethodAdapter(private val paymentMethods: List<PaymentMethodDB>) :
    RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNumberTextView: TextView = itemView.findViewById(R.id.tvCardNo)
        val cvvTextView: TextView = itemView.findViewById(R.id.tvCVV)
        val expDateTextView: TextView = itemView.findViewById(R.id.tvExpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment, parent, false)
        return PaymentMethodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethods[position]
        holder.cardNumberTextView.text = paymentMethod.cardNumber
        holder.cvvTextView.text = paymentMethod.cvv
        holder.expDateTextView.text = paymentMethod.expDate
    }

    override fun getItemCount(): Int {
        return paymentMethods.size
    }
}
