package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.PodcastApp;
import accessweb.com.br.radiocontrole.util.CacheData;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastsListAdapter extends RecyclerView.Adapter<PodcastsListAdapter.MyViewHolder> {
    List<PodcastApp> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private MediaPlayer mp;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private Boolean isPlaying = false;
    private Boolean firstTime = true;
    private int lastPodcast;
    private String timeAgo;
    RecyclerView mRecyclerView;

    Runnable notification;


    public PodcastsListAdapter(Context context, List<PodcastApp> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.podcasts_row, parent, false);
        mRecyclerView = (RecyclerView) parent;
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PodcastApp current = data.get(position);
        CacheData cacheData = new CacheData(context);
        holder.tituloPodcast.setText(current.getTituloPodcast());
        holder.linkPodcast.setText(current.getLinkPodcast());
        Date now = new Date();
        Date past = current.getDataPublicacaoPodcast();
        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
            timeAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " dias atrás";
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0 ) {
            timeAgo = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " horas atrás";
        } else if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime())> 0 ) {
            timeAgo = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutos atrás";
        } else {
            timeAgo = "agora";
        }
        holder.dataPublicacaoPodcast.setText(timeAgo);
        holder.podcast_control.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        holder.btnPlayPausePodcast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO
                if (!isPlaying){
                    try {

                        // SE FOR A PRIMEIRA VEZ QUE APERTA PLAY -> CRIA-SE UM NOVO PODCAST
                        if (firstTime){

                            lastPodcast = position;
                            holder.btnPlayPausePodcast.setVisibility(View.INVISIBLE);
                            holder.loadingIcon.setVisibility(View.VISIBLE);
                            mp = new MediaPlayer();
                            mp.setDataSource(String.valueOf(Uri.parse(current.getLinkPodcast())));
                            mp.prepareAsync();
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                public void onPrepared(final MediaPlayer mp) {
                                    mediaFileLengthInMilliseconds = mp.getDuration();
                                    primarySeekBarProgressUpdater(holder, position);
                                    mp.start();
                                    holder.loadingIcon.setVisibility(View.GONE);
                                    holder.btnPlayPausePodcast.setVisibility(View.VISIBLE);
                                    holder.btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_white);
                                    isPlaying = true;
                                    firstTime = false;
                                }

                            });

                        // SE FOR A SEGUNDA OU MAIS VEZES QUE APERTA PLAY
                        }else {

                            // SE FOI NO MESMO PODCAST -> RESUME()
                            if (lastPodcast == position){
                                primarySeekBarProgressUpdater(holder, position);
                                mp.start();
                                holder.btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_white);
                                isPlaying = true;

                            //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                            }else {

                                // PARAR RUNNABLE
                                handler.removeCallbacks(notification);

                                // RESETAR ITENS DO PODCAST ANTERIOR
                                ImageButton btnItemAnterior = (ImageButton) mRecyclerView.getChildAt(lastPodcast).findViewById(R.id.btnPlayPausePodcast);
                                btnItemAnterior.setImageResource(R.drawable.ic_play_white);
                                TextView tempoItemAnterior = (TextView) mRecyclerView.getChildAt(lastPodcast).findViewById(R.id.tempoAudioPodcast);
                                tempoItemAnterior.setText("00:00");

                                // INICIAR NOVO PLAYER DE PODCAST
                                mp.stop();
                                lastPodcast = position;
                                holder.btnPlayPausePodcast.setVisibility(View.INVISIBLE);
                                holder.loadingIcon.setVisibility(View.VISIBLE);
                                mp = new MediaPlayer();
                                mp.setDataSource(String.valueOf(Uri.parse(current.getLinkPodcast())));
                                mp.prepareAsync();
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                    public void onPrepared(final MediaPlayer mp) {
                                        mediaFileLengthInMilliseconds = mp.getDuration();
                                        primarySeekBarProgressUpdater(holder, position);
                                        mp.start();
                                        holder.loadingIcon.setVisibility(View.GONE);
                                        holder.btnPlayPausePodcast.setVisibility(View.VISIBLE);
                                        holder.btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_white);
                                        isPlaying = true;
                                        firstTime = false;
                                    }

                                });
                            }

                        }
                    } catch (IOException e) {
                        Log.v("Exception: ", e.getMessage());
                    }

                // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position){
                        mp.pause();
                        holder.btnPlayPausePodcast.setImageResource(R.drawable.ic_play_white);
                        isPlaying = false;

                    //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {
                        try {

                            // PARAR RUNNABLE
                            handler.removeCallbacks(notification);

                            // RESETAR ITENS DO PODCAST ANTERIOR
                            ImageButton btnItemAnterior = (ImageButton) mRecyclerView.getChildAt(lastPodcast).findViewById(R.id.btnPlayPausePodcast);
                            btnItemAnterior.setImageResource(R.drawable.ic_play_white);
                            TextView tempoItemAnterior = (TextView) mRecyclerView.getChildAt(lastPodcast).findViewById(R.id.tempoAudioPodcast);
                            tempoItemAnterior.setText("00:00");

                            // INICIAR NOVO PLAYER DE PODCAST
                            mp.stop();
                            lastPodcast = position;
                            holder.btnPlayPausePodcast.setVisibility(View.INVISIBLE);
                            holder.loadingIcon.setVisibility(View.VISIBLE);
                            mp = new MediaPlayer();
                            mp.setDataSource(String.valueOf(Uri.parse(current.getLinkPodcast())));
                            mp.prepareAsync();
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                public void onPrepared(final MediaPlayer mp) {
                                    mediaFileLengthInMilliseconds = mp.getDuration();
                                    primarySeekBarProgressUpdater(holder, position);
                                    mp.start();
                                    holder.loadingIcon.setVisibility(View.GONE);
                                    holder.btnPlayPausePodcast.setVisibility(View.VISIBLE);
                                    holder.btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_white);
                                    isPlaying = true;
                                    firstTime = false;
                                }

                            });
                        }catch (IOException e){
                            Log.v("Exception: ", e.getMessage());
                        }
                    }
                }

            }
        });

        // SEEKBAR CHANGE
        holder.progressBarPodcast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mp != null && fromUser){
                    playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * holder.progressBarPodcast.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mp != null){
                    mp.seekTo(playPositionInMillisecconds);
                }else {
                    holder.progressBarPodcast.setProgress(startPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPodcast;
        TextView linkPodcast;
        TextView dataPublicacaoPodcast;
        TextView tempoAudioPodcast;
        ImageButton btnPlayPausePodcast;
        SeekBar progressBarPodcast;
        ProgressBar loadingIcon;
        RelativeLayout podcast_control;

        public MyViewHolder(View itemView) {
            super(itemView);

            tituloPodcast = (TextView) itemView.findViewById(R.id.tituloPodcast);
            linkPodcast = (TextView) itemView.findViewById(R.id.linkPodcast);
            dataPublicacaoPodcast = (TextView) itemView.findViewById(R.id.dataPublicacaoPodcast);
            tempoAudioPodcast = (TextView) itemView.findViewById(R.id.tempoAudioPodcast);
            btnPlayPausePodcast = (ImageButton) itemView.findViewById(R.id.btnPlayPausePodcast);
            progressBarPodcast = (SeekBar) itemView.findViewById(R.id.progressBarPodcast);
            loadingIcon = (ProgressBar) itemView.findViewById(R.id.loadingIcon);
            podcast_control = (RelativeLayout) itemView.findViewById(R.id.podcast_control);
        }
    }

    private void primarySeekBarProgressUpdater(final MyViewHolder holder, final int position) {
        holder.progressBarPodcast.setProgress((int)(((float)mp.getCurrentPosition()/mediaFileLengthInMilliseconds)*100));
        holder.tempoAudioPodcast.setText("" + milliSecondsToTimer((long) mp.getCurrentPosition()));
        notification = new Runnable() {
            public void run() {
                primarySeekBarProgressUpdater(holder, position);
            }
        };
        handler.postDelayed(notification,1000);
    }

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String minutesFormatted;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (minutes < 10){
            minutesFormatted = "0" + minutes;
        } else {
            minutesFormatted = "" + minutes;
        }

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutesFormatted + ":" + secondsString;

        return finalTimerString;
    }
}
