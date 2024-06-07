package ua.dp.klio.wiu.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.dp.klio.wiu.player.PlaybackActivity
import ua.dp.klio.wiu.R
import ua.dp.klio.wiu.data.server.RemoteConnection
import ua.dp.klio.wiu.data.server.Result
import ua.dp.klio.wiu.data.server.toDomainMovie
import ua.dp.klio.wiu.domain.Movie
import ua.dp.klio.wiu.ui.main.CardPresenter
import ua.dp.klio.wiu.ui.main.MainActivity

/**
 * A wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its metadata plus related videos.
 */
class VideoDetailsFragment : DetailsSupportFragment() {

    private var mSelectedMovie: Movie? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mPresenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

        mSelectedMovie = activity!!.intent.getSerializableExtra(DetailsActivity.MOVIE) as Movie
        if (mSelectedMovie != null) {
            mPresenterSelector = ClassPresenterSelector()
            mAdapter = ArrayObjectAdapter(mPresenterSelector)
            setupDetailsOverviewRow()
            setupDetailsOverviewRowPresenter()
            if ((mSelectedMovie!!.season > 0) && (mSelectedMovie!!.episode == 0))
            {
                setupRelatedMovieListRow()
            }
            adapter = mAdapter
            initializeBackground(mSelectedMovie)
            onItemViewClickedListener = ItemViewClickedListener()
        } else {
            val intent = Intent(activity!!, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeBackground(movie: Movie?) {
        mDetailsBackground.enableParallax()
        Glide.with(activity!!)
            .asBitmap()
            .centerCrop()
            .error(R.drawable.default_background)
            .load(movie?.backgroundImageUrl)
            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    mDetailsBackground.coverBitmap = bitmap
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(mSelectedMovie)
        row.imageDrawable = ContextCompat.getDrawable(activity!!, R.drawable.default_background)
        var width = convertDpToPixel(activity!!, DETAIL_THUMB_WIDTH)
        var height = convertDpToPixel(activity!!, DETAIL_THUMB_HEIGHT)
        var posterUrl = mSelectedMovie?.poster

        if (mSelectedMovie?.episode!! > 0) {
            width = convertDpToPixel(activity!!, DETAIL_THUMB_HEIGHT)
            height = convertDpToPixel(activity!!, DETAIL_THUMB_WIDTH)
            posterUrl = mSelectedMovie?.coverImageUrl
        }

        Glide.with(activity!!)
            .load(posterUrl)
            .centerCrop()
            .error(R.drawable.default_background)
            .into<SimpleTarget<Drawable>>(object : SimpleTarget<Drawable>(width, height) {
                override fun onResourceReady(
                    drawable: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    row.imageDrawable = drawable
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })

        val actionAdapter = ArrayObjectAdapter()

        actionAdapter.add(
            Action(
                ACTION_WATCH_TRAILER,
                resources.getString(R.string.watch_trailer)
            )
        )

        if (mSelectedMovie!!.tvId == 0)
        {
            actionAdapter.add(
                Action(
                    ACTION_WATCH_MOVIE,
                    resources.getString(R.string.watch_movie)
                )
            )
        }

        if (mSelectedMovie!!.episode > 0)
        {
            actionAdapter.add(
                Action(
                    ACTION_WATCH_MOVIE,
                    resources.getString(R.string.watch_movie)
                )
            )
        }

        row.actionsAdapter = actionAdapter

        mAdapter.add(row)
    }

    private fun setupDetailsOverviewRowPresenter() {
        // Set detail background.
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.backgroundColor =
            ContextCompat.getColor(activity!!, R.color.default_background)

        // Hook up transition element.
        val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
        sharedElementHelper.setSharedElementEnterTransition(
            activity, DetailsActivity.SHARED_ELEMENT_NAME
        )
        detailsPresenter.setListener(sharedElementHelper)
        detailsPresenter.isParticipatingEntranceTransition = true

        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == ACTION_WATCH_TRAILER) {
                val intent = Intent(activity!!, PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.PLAY_TRAILER, true)
                intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie)
                startActivity(intent)
            } else if (action.id == ACTION_WATCH_MOVIE) {
                val intent = Intent(activity!!, PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.PLAY_TRAILER, false)
                intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie)
                startActivity(intent)
            } else {
                Toast.makeText(activity!!, action.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }

    private fun setupRelatedMovieListRow() {
        Log.v("TAG", "setupRelatedMovieListRow")
        val cardPresenter = CardPresenter()

        val startingIndex = 0
        for (i in startingIndex until mSelectedMovie!!.season) {
            val seasonId = i+1
            val seasonTitle = "Сезон $seasonId"
            Log.v("TAG", seasonTitle)

            val listRowsAdapter = ArrayObjectAdapter(cardPresenter)
            GlobalScope.launch {
                val seasonRowAdapter = RemoteConnection.service
                    .listEpisodesTVs(mSelectedMovie!!.tvId, seasonId)
                    .results.map { it.toDomainMovie() }

                listRowsAdapter.addAll(0, seasonRowAdapter)
            }

            val header = HeaderItem(0, seasonTitle)
            mAdapter.add(ListRow(header, listRowsAdapter))

            mPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        }

    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.applicationContext.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Movie) {
                val intent = Intent(activity!!, DetailsActivity::class.java)
                intent.putExtra(resources.getString(R.string.movie), item)

                val bundle =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity!!,
                        (itemViewHolder?.view as ImageCardView).mainImageView,
                        DetailsActivity.SHARED_ELEMENT_NAME
                    )
                        .toBundle()
                startActivity(intent, bundle)
            }
        }
    }

    companion object {
        private val TAG = "VideoDetailsFragment"

        private val ACTION_WATCH_TRAILER = 1L
        private val ACTION_WATCH_MOVIE = 2L
        private val ACTION_BUY = 3L

        private val DETAIL_THUMB_WIDTH = 264
        private val DETAIL_THUMB_HEIGHT = 396

        private val NUM_COLS = 10
    }
}
