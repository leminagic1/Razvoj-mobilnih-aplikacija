package com.example.s1rma
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView


class JelaAdapter(private val context: Context) : BaseAdapter() {

    val jelaList = mutableListOf<String>()

    fun dodajJelo(jelo: String) {
        jelaList.add(jelo)
        notifyDataSetChanged()
    }

    fun izmijeniJelo(oldJelo: String, newJelo: String) {
        val index = jelaList.indexOf(oldJelo)
        if (index != -1) {
            jelaList[index] = newJelo
            notifyDataSetChanged()
        }
    }

    fun ukloniJelo(jelo: String) {
        jelaList.remove(jelo)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return jelaList.size
    }

    override fun getItem(position: Int): Any {
        return jelaList[position]
    }

    fun getAllJela(): List<String> {
        return jelaList.toList()
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
            viewHolder.jeloTextView = view.findViewById(android.R.id.text1)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val jelo = getItem(position) as String
        viewHolder.jeloTextView.text = jelo


        return view!!
    }

    private class ViewHolder {
        lateinit var jeloTextView: TextView

    }
}
