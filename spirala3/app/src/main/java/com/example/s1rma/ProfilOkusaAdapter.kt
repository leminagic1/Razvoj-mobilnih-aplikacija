package com.example.s1rma
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class ProfilOkusaAdapter(private val context: Context, private val options: Array<ProfilOkusaBiljke>) : BaseAdapter() {

    private var selectedPosition = -1

    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): Any {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            viewHolder = ViewHolder()
            viewHolder.enumTextView = view.findViewById(android.R.id.text1)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val option = getItem(position) as ProfilOkusaBiljke
        viewHolder.enumTextView.text = option.opis


        if (position == selectedPosition) {
            viewHolder.enumTextView.setBackgroundColor(context.resources.getColor(android.R.color.holo_blue_light))
        } else {
            viewHolder.enumTextView.setBackgroundColor(Color.TRANSPARENT)
        }


        viewHolder.enumTextView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }

        return view!!
    }

    fun getSelectedOption(): ProfilOkusaBiljke? {
        return if (selectedPosition != -1 && selectedPosition < options.size) {
            options[selectedPosition]
        } else {
            null
        }

    }

    private class ViewHolder {
        lateinit var enumTextView: TextView
    }
}