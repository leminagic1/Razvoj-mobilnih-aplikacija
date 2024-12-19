package com.example.s1rma

import android.content.Context
import android.graphics.Bitmap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BiljkaAdapter(
    private var mode: Mode,

    private var onItemClick: ((Biljka) -> Unit)? = null
) : ListAdapter<Biljka, RecyclerView.ViewHolder>(BiljkaDiffCallBack()) {
    private var currentReferencePlant: Biljka? = null





    private val MEDICINSKI_VIEW_TYPE = 0
    private val KUHARSKI_VIEW_TYPE = 1
    private val BOTANICKI_VIEW_TYPE = 2

    private var trefleDAO: TrefleDAO? = null




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

    fun setTrefleDAO(trefleDAO: TrefleDAO) {
        this.trefleDAO = trefleDAO
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
//        private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
//            var width = bitmap.width
//            var height = bitmap.height
//            val ratio: Float = when {
//                width > height -> width.toFloat() / height.toFloat()
//                height > width -> height.toFloat() / width.toFloat()
//                else -> 1.0f
//            }
//
//            // Ensure width is within maxSize
//            width = maxSize
//            height = (width / ratio).toInt()
//
//            // Resize or crop the bitmap
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
//
//            // Optionally, you can crop the bitmap further if n
//
//            // val croppedBitmap = Bitmap.createBitmap(scaledBitmap, startX, startY, newWidth, newHeight)
//
//            return scaledBitmap
//        }
//        private fun loadImage(biljka: Biljka) {
//            CoroutineScope(Dispatchers.Main).launch {
//                val bitmap = withContext(Dispatchers.IO) {
//                    // Check if the image is in the database
//                    val biljkaBitmap = BiljkaDatabase.getInstance(itemView.context).biljkaDao()
//                        .getBiljkaBitmapById(biljka.id)
//                    if (biljkaBitmap != null) {
//                        Log.d("LoadImage", "Image found in database for biljka ID: ${biljka.id}")
//                        biljkaBitmap.bitmap
//                    } else {
//                        Log.d(
//                            "LoadImage",
//                            "Image not found in database, fetching online for biljka ID: ${biljka.id}"
//                        )
//                        // Fetch from online service
//                        val fetchedBitmap = TrefleDAO().getImage(biljka)
//                        val resizedBitmap = resizeBitmap(fetchedBitmap, 106) // Resize the image
//
//                        // Convert resized bitmap to ByteArray using Konvertori
//                      //  val byteArray = Konvertori().fromBitmap(resizedBitmap)
//
//
//                        // Add the fetched image to the database
//                        val success = biljkaDAO.addImage(biljka.id, resizedBitmap)
//                        if (success) {
//                            Log.d(
//                                "LoadImage",
//                                "Image successfully added to database for biljka ID: ${biljka.id}"
//                            )
//                        } else {
//                            Log.d(
//                                "LoadImage",
//                                "Failed to add image to database for biljka ID: ${biljka.id}"
//                            )
//                        }
//                        resizedBitmap // Return resized bitmap for display or further use
//                    }
//                }
//                // Set bitmap to ImageView or handle as needed
//                slikaImageView?.setImageBitmap(bitmap)
//                Log.d("LoadImage", "Image displayed for biljka ID: ${biljka.id}")
//            }
//
//
//        }
//        private fun loadImage(biljka: Biljka) {
//            CoroutineScope(Dispatchers.Main).launch {
//                val bitmap = withContext(Dispatchers.IO) {
//                    // Check if the image is in the database
//                    val biljkaBitmap = BiljkaDatabase.getInstance(itemView.context).biljkaDao()
//                        .getBiljkaBitmapById(biljka.id)
//                    if (biljkaBitmap != null) {
//                        Log.d("LoadImage", "Image found in database for biljka ID: ${biljka.id}")
//                        biljkaBitmap.bitmap
//                    } else {
//                        Log.d(
//                            "LoadImage",
//                            "Image not found in database, fetching online for biljka ID: ${biljka.id}"
//                        )
//                        // Fetch from online service
//                        val fetchedBitmap = TrefleDAO().getImage(biljka)
//                        val resizedBitmap = resizeBitmap(fetchedBitmap, 106) // Resize the image
//                        // Add the fetched image to the database
//                        val success = BiljkaDatabase.getInstance(itemView.context).biljkaDao()
//                            .addImage(biljka.id, resizedBitmap)
//                        if (success) {
//                            Log.d(
//                                "LoadImage",
//                                "Image successfully added to database for biljka ID: ${biljka.id}"
//                            )
//                        } else {
//                            Log.d(
//                                "LoadImage",
//                                "Failed to add image to database for biljka ID: ${biljka.id}"
//                            )
//                        }
//                        resizedBitmap
//                    }
//                }
//                slikaImageView?.setImageBitmap(bitmap)
//                Log.d("LoadImage", "Image displayed for biljka ID: ${biljka.id}")
//            }

 //           }
private fun loadImage(biljka: Biljka) {
    CoroutineScope(Dispatchers.Main).launch{
        val bitmap = withContext(Dispatchers.IO) {
            TrefleDAO().getImage(biljka)
        }
        slikaImageView?.setImageBitmap(bitmap)
    }
}

        fun bindMedicinskiMode(biljka: Biljka) {

            slikaImageView?.setImageResource(R.drawable.cvijet)
            nazivTextView?.text = biljka.naziv
            loadImage(biljka)
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
       loadImage(biljka)
            val profilOkusa = biljka.profilOkusa
            profilOkusaBiljkeTextView?.text = "${profilOkusa?.opis}"
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
       loadImage(biljka)
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

