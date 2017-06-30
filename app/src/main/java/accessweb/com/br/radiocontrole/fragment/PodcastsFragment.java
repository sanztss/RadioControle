package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PodcastsListAdapter;
import accessweb.com.br.radiocontrole.model.Podcast;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static java.security.AccessController.getContext;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PodcastsListAdapter adapter;
    List<Podcast> podecasts;

    public PodcastsFragment() {
        // Required empty public constructor
    }

    public static List<Podcast> getData() {
        List<Podcast> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Podcast podcast = new Podcast();

            podcast.setTituloPodcast("Podcast título " + i);
            podcast.setLinkPodcast("http://e.mundopodcast.com.br/podprogramar/podprogramar-013-sistemas-controle-versao.mp3");
            podcast.setDataPublicacaoPodcast("Publicado no dia 0" + i + "/06/2017");

            data.add(podcast);

        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podcasts, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new PodcastsListAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        //Podcasts item = podecasts.get(itemPosition);
                        Log.v("aaa","" + itemPosition);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

