package ua.dp.klio.wiu.ui.main

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import android.view.ViewGroup
import ua.dp.klio.wiu.domain.Movie

import ua.dp.klio.wiu.ui.common.loadUrl
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setInfoAreaBackgroundColor(Color.BLACK)
        }
        /* sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        } */

        /* updateCardBackgroundColor(cardView, false) */
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val movie = item as Movie

        if (movie.episode > 0)
        {
            with(viewHolder.view as ImageCardView) {
                titleText = movie.title
                contentText = movie.originalTitle
                setMainImageDimensions(CARD_HEIGHT, CARD_WIDTH)
                mainImageView.loadUrl(movie.coverImageUrl!!)
            }
        } else if (movie.isKlio) {
            with(viewHolder.view as ImageCardView) {
                titleText = movie.title
                contentText = movie.originalTitle
                setMainImageDimensions(CARD_HEIGHT, CARD_WIDTH)
                mainImageView.loadUrl(movie.poster!!)
            }
        } else {
            with(viewHolder.view as ImageCardView) {
                titleText = movie.title
                contentText = movie.originalTitle
                setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
                mainImageView.loadUrl(movie.poster!!)
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        with(viewHolder.view as ImageCardView) {
            mainImage = null
        }
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val CARD_WIDTH = 264
        private val CARD_HEIGHT = 396
    }
}