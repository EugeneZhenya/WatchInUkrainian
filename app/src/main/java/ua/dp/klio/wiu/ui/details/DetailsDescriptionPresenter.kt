package ua.dp.klio.wiu.ui.details

import android.os.Build
import android.text.Html
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
        // viewHolder.body.text = movie.description
        viewHolder.body.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(movie.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(movie.description)
        }
    }
}