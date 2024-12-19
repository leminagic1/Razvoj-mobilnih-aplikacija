package com.example.s1rma
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class KlimatskiTipAdapter(
    context: Context,
    private val items: List<KlimatskiTip>
) : ArrayAdapter<KlimatskiTip>(context, 0, items) {

    private val selectedItems = mutableListOf<KlimatskiTip>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        val textView = view?.findViewById<TextView>(android.R.id.text1)
        val item = getItem(position)

        textView?.text = item?.opis
        textView?.setOnClickListener {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item)
                textView.setTextColor(context.resources.getColor(android.R.color.black))
            } else {
                selectedItems.add(item!!)
                textView.setTextColor(context.resources.getColor(android.R.color.holo_blue_dark))
            }
        }

        return view!!
    }

    fun getSelectedItems(): List<KlimatskiTip> {
        return selectedItems
    }
}
