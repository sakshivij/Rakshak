package com.example.rakshak

import android.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ContactAdapter(private val contactlist: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameUser: TextView
        val number: TextView

        init {
            nameUser = view.findViewById<View>(R.id.name_of_user) as TextView
            number = view.findViewById<View>(R.id.number_of_user) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contactList, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contactVal = contactlist[position]
        holder.nameUser.setText(contactVal.name)
        holder.number.setText(contactVal.contactNum)
        //holder.year.setText(movie.getYear())
    }

    override fun getItemCount(): Int {
        return contactlist.size
    }


}
