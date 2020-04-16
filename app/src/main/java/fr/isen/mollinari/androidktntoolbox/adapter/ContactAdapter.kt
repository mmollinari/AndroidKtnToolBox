package fr.isen.mollinari.androidktntoolbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_recycler_cell.view.contactName

class ContactAdapter(private val contacts: List<String>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_recycler_cell, parent, false))

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contactName.text = contacts[position]
    }

    class ContactViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val contactName: TextView = contactView.contactName
    }
}
