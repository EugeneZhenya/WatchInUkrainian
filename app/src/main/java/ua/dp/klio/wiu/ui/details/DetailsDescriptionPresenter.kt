package ua.dp.klio.wiu.ui.details

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import ua.dp.klio.wiu.domain.Movie

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
        viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
        item: Any
    ) {
        val movie = item as Movie

        viewHolder.title.text = movie.title
        viewHolder.subtitle.text = movie.originalTitle
        viewHolder.body.text = movie.description
    }
}