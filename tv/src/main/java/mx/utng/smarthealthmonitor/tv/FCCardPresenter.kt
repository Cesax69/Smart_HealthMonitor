package mx.utng.smarthealthmonitor.tv

import android.graphics.Color
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

class FCCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(300, 200)
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card = viewHolder.view as ImageCardView
        
        if (item is LecturaFC) {
            card.titleText = "${item.valorBpm} bpm"
            card.contentText = item.hora

            val bgColor = if (item.esNormal) {
                Color.parseColor("#1B4F8A")  // primary
            } else {
                Color.parseColor("#B3261E")  // error
            }
            // Método correcto para tarjetas Leanback:
            card.infoAreaBackground = null 
            card.setInfoAreaBackgroundColor(bgColor)
            // Asegurar que se vea algo aunque no haya imagen
            card.mainImage = null 
        } else if (item is String) {
            card.titleText = item
            card.setInfoAreaBackgroundColor(Color.DKGRAY)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val card = viewHolder.view as ImageCardView
        card.mainImage = null
    }
}
