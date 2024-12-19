package com.example.s1rma

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BiljkaAdapter(
    private var mode: Mode,
    private var onItemClick: ((Biljka) -> Unit)? = null
) : ListAdapter<Biljka, RecyclerView.ViewHolder>(BiljkaDiffCallBack()) {
    private var currentReferencePlant: Biljka? = null
    // Tipovi stavki za svaki mod
    private val MEDICINSKI_VIEW_TYPE = 0
    private val KUHARSKI_VIEW_TYPE = 1
    private val BOTANICKI_VIEW_TYPE = 2

    override fun getItemViewType(position: Int): Int {
        val biljka = getItem(position)
        return when (mode) {
            Mode.MEDICINSKI -> MEDICINSKI_VIEW_TYPE
            Mode.KUHARSKI -> KUHARSKI_VIEW_TYPE
            Mode.BOTANICKI -> BOTANICKI_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MEDICINSKI_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.medicinski_item, parent, false)
                BiljkaViewHolder(itemView)
            }
            KUHARSKI_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.kuharski_item, parent, false)
                BiljkaViewHolder(itemView)
            }
            else -> {
                val itemView = inflater.inflate(R.layout.botanicki_item, parent, false)
                BiljkaViewHolder(itemView)
            }
        }
    }
    fun setOnItemClickListener(listener: (Biljka) -> Unit) {
        onItemClick = listener
        notifyDataSetChanged()
    }
    fun setCurrentReferencePlant(plant: Biljka) {
        currentReferencePlant = plant
        notifyDataSetChanged()
    }
    fun updateMode(mode: Mode) {
        this.mode = mode
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val biljka = getItem(position) // Dobavi podatke za trenutni položaj
        when (holder.itemViewType) {
            MEDICINSKI_VIEW_TYPE -> (holder as BiljkaViewHolder).bindMedicinskiMode(biljka)
            KUHARSKI_VIEW_TYPE -> (holder as BiljkaViewHolder).bindKuharskiMode(biljka)
            BOTANICKI_VIEW_TYPE -> (holder as BiljkaViewHolder).bindBotanickiMode(biljka)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.let { click ->
                click.invoke(biljka)
            }
        }
    }

    inner class BiljkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nazivTextView: TextView? = itemView.findViewById(R.id.nazivItem)
        private val porodicaTextView: TextView? = itemView.findViewById(R.id.porodicaItem)
        private val upozorenjeTextView: TextView? = itemView.findViewById(R.id.upozorenjeItem)
        private val jelo1TextView: TextView? = itemView.findViewById(R.id.jelo1Item)
        private val jelo2TextView: TextView? = itemView.findViewById(R.id.jelo2Item)
        private val jelo3TextView: TextView? = itemView.findViewById(R.id.jelo3Item)
        private val klimatskiTipTextView: TextView? = itemView.findViewById(R.id.klimatskiTipItem)
        private val zemljisniTipTextView: TextView? = itemView.findViewById(R.id.zemljisniTipItem)
        private val medicinskaKorist1TextView: TextView? = itemView.findViewById(R.id.korist1Item)
        private val medicinskaKorist2TextView: TextView? = itemView.findViewById(R.id.korist2Item)
        private val medicinskaKorist3TextView: TextView? = itemView.findViewById(R.id.korist3Item)
        private val profilOkusaBiljkeTextView: TextView? =
            itemView.findViewById(R.id.profilOkusaItem)
        private val slikaImageView: ImageView? = itemView.findViewById(R.id.slikaItem)

        fun bind(biljka: Biljka) {
            Log.d("", " pokazi mi mod ${mode}")

            when (mode) {
                Mode.MEDICINSKI -> bindMedicinskiMode(biljka)
                Mode.KUHARSKI -> bindKuharskiMode(biljka)
                Mode.BOTANICKI -> bindBotanickiMode(biljka)
            }


        }

        fun bindMedicinskiMode(biljka: Biljka) {
            slikaImageView?.setImageResource(R.drawable.cvijet)
            nazivTextView?.text = biljka.naziv

            upozorenjeTextView?.text = biljka.medicinskoUpozorenje
            biljka.medicinskeKoristi.forEachIndexed { index, korist ->
                when (index) {
                    0 -> medicinskaKorist1TextView?.text = "${korist.opis}"
                    1 -> medicinskaKorist2TextView?.text = "${korist.opis}"
                    2 -> medicinskaKorist3TextView?.text = "${korist.opis}"
                }

            }
            if (biljka.medicinskeKoristi.size < 3) {
                medicinskaKorist3TextView?.visibility = View.GONE
                if (biljka.medicinskeKoristi.size < 2) {
                    medicinskaKorist2TextView?.visibility = View.GONE
                }
            }
        }

        fun bindKuharskiMode(biljka: Biljka) {
            slikaImageView?.setImageResource(R.drawable.cvijet)
            nazivTextView?.text = biljka.naziv

            val profilOkusa = biljka.profilOkusa
            profilOkusaBiljkeTextView?.text = "${profilOkusa.opis}"
            jelo1TextView?.visibility = View.VISIBLE
            jelo2TextView?.visibility = View.VISIBLE
            jelo3TextView?.visibility = View.VISIBLE
            biljka.jela.forEachIndexed { index, jelo ->
                when (index) {
                    0 -> jelo1TextView?.text = "${jelo}"
                    1 -> jelo2TextView?.text = "${jelo}"
                    2 -> jelo3TextView?.text = "${jelo}"
                }


                }
            if (biljka.jela.size < 3) {
                jelo3TextView?.visibility = View.GONE
                if (biljka.jela.size < 2) {
                    jelo2TextView?.visibility = View.GONE
                }
            }



        }

        fun bindBotanickiMode(biljka: Biljka) {
            slikaImageView?.setImageResource(R.drawable.cvijet)
            nazivTextView?.text = biljka.naziv
            porodicaTextView?.text = biljka.porodica
            Log.d("ThreadCheck", "STA JE PORODICA ${biljka.porodica}")
            if (biljka.klimatskiTipovi.isNotEmpty()) {
                val firstKlimatskiTip = biljka.klimatskiTipovi.first()
                klimatskiTipTextView?.text = "${firstKlimatskiTip.opis}"
                Log.d("ThreadCheck", "STA JE PORODICA ${firstKlimatskiTip.opis}")
            }
            if (biljka.zemljisniTipovi.isNotEmpty()) {
                val firstZemljisniTip = biljka.zemljisniTipovi.first()
                zemljisniTipTextView?.text = "${firstZemljisniTip.naziv}"
                Log.d("ThreadCheck", "STA JE PORODICA ${firstZemljisniTip.naziv}")
            }

        }
        }
    }

    class BiljkaDiffCallBack : DiffUtil.ItemCallback<Biljka>() {
        override fun areItemsTheSame(oldItem: Biljka, newItem: Biljka): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Biljka, newItem: Biljka): Boolean {
            return oldItem == newItem
        }
    }

