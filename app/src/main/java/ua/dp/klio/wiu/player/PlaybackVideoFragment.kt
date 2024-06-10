package ua.dp.klio.wiu.player

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackSeekDataProvider
import ua.dp.klio.wiu.domain.Movie
import ua.dp.klio.wiu.ui.details.DetailsActivity

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (id, title, originalTitle, description, backgroundUrl, posterUrl, coverUrl, videoUrl, trailerUrl, releaseDate) =
            activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        val playTrailer = activity?.intent?.getBooleanExtra(DetailsActivity.PLAY_TRAILER, false)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        val playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = PlaybackTransportControlGlue(getActivity(), playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = title
        mTransportControlGlue.subtitle = originalTitle
        mTransportControlGlue.seekProvider = PlaybackSeekDataProvider()
        mTransportControlGlue.playWhenPrepared()

        if (playTrailer == true) {
            playerAdapter.setDataSource(Uri.parse(trailerUrl))
        } else {
            playerAdapter.setDataSource(Uri.parse(videoUrl))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(android.R.color.black);
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }
}