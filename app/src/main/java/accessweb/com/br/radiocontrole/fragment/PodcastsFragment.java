package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PodcastsListAdapter;
import accessweb.com.br.radiocontrole.model.Podcast;
import accessweb.com.br.radiocontrole.model.PodcastApp;
import accessweb.com.br.radiocontrole.model.Podcasts;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PodcastsListAdapter adapter;
    private List<PodcastApp> podcastApps = new ArrayList<>();

    public PodcastsFragment() {
        // Required empty public constructor
    }

    public static List<PodcastApp> getData() {
        List<PodcastApp> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            PodcastApp podcast = new PodcastApp();

            podcast.setTituloPodcast("PodcastApp título " + i);
            podcast.setLinkPodcast("http://e.mundopodcast.com.br/podprogramar/podprogramar-013-sistemas-controle-versao.mp3");
            podcast.setDataPublicacaoPodcast(new Date());

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

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                CacheData cacheData = new CacheData(getContext());
                Podcasts podcasts = client.radioIdPodcastsGet(cacheData.getString("idRadio"));

                for (Podcast podcast : podcasts){
                    PodcastApp podcastApp = new PodcastApp();
                    Timestamp timestamp = new Timestamp(podcast.getTimestamp());
                    Date dataPostagem = new Date(timestamp.getTime());

                    podcastApp.setTituloPodcast(podcast.getName());
                    podcastApp.setLinkPodcast("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/podcasts/" + podcast.getFile());
                    podcastApp.setDataPublicacaoPodcast(dataPostagem);
                    podcastApps.add(podcastApp);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                    adapter = new PodcastsListAdapter(getContext(), podcastApps);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        }.execute();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

