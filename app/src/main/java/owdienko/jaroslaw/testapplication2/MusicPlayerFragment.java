package owdienko.jaroslaw.testapplication2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.Random;


/**
 * Created by Jaroslaw Owdienko on 4/12/2017. All rights reserved TestApplication2!
 */

public class MusicPlayerFragment extends Fragment {

    private ImageButton fragmentPlayerBtnPlay, fragmentPlayerBtnPause;
    private MediaPlayer fragmentPlayerMediaPlayer;
    private SeekBar fragmentPlayerSeekBar;

    private int[] musicList = {
            R.raw.ramin_djawadi_westworld_ost,
            R.raw.kaleo_way_down_we_go,
            R.raw.the_lord_of_the_rings_the_shire,
            R.raw.bach_suite_cello_solo_in_g_major,
            R.raw.dustin_ohalloran_opus,
            R.raw.hans_zimmer_interstellar_main_theme,
            R.raw.sting_shape_of_my_heart,
            R.raw.tstm_was_it_a_dream
    };

    private final Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.music_player_view, container, false);
        initUIElements(fragmentView);
        fragmentPlayerBtnPlay.setOnClickListener(buttonPlayListener());
        fragmentPlayerBtnPause.setOnClickListener(buttonPauseListener());
        fragmentPlayerSeekBar.setOnTouchListener(seekBarListener());
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "fragment onViewCreated ");
        fragmentPlayerMediaPlayer.start();
        startPlayProgressUpdater();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentPlayerMediaPlayer.pause();
    }


    private void initUIElements(View fragmentView) {
        fragmentPlayerBtnPlay = (ImageButton) fragmentView.findViewById(R.id.play_button);
        fragmentPlayerBtnPause = (ImageButton) fragmentView.findViewById(R.id.pause_button);
        fragmentPlayerSeekBar = (SeekBar) fragmentView.findViewById(R.id.seekBar);

        fragmentPlayerMediaPlayer = MediaPlayer.create(getContext(), musicList[new Random().nextInt(musicList.length)]);
        fragmentPlayerSeekBar.setMax(fragmentPlayerMediaPlayer.getDuration());
    }

    private View.OnClickListener buttonPlayListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPlayerMediaPlayer.start();
                startPlayProgressUpdater();
            }
        };
    }

    private View.OnClickListener buttonPauseListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPlayerMediaPlayer.pause();
            }
        };
    }

    private View.OnTouchListener seekBarListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fragmentPlayerMediaPlayer.isPlaying()) {
                    fragmentPlayerMediaPlayer.pause();
                    fragmentPlayerMediaPlayer.seekTo(fragmentPlayerSeekBar.getProgress());
                    fragmentPlayerMediaPlayer.start();
                }
                return false;
            }
        };
    }

    public void startPlayProgressUpdater() {
        fragmentPlayerSeekBar.setProgress(fragmentPlayerMediaPlayer.getCurrentPosition());

        if (fragmentPlayerMediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification, 100);
        }
    }

}
