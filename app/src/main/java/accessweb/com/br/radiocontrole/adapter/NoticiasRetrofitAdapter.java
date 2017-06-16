package accessweb.com.br.radiocontrole.adapter;

import accessweb.com.br.radiocontrole.model.NoticiaFeed;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Des. Android on 16/06/2017.
 */

public interface NoticiasRetrofitAdapter {
    @GET("/dynamo/rss2.xml")
    Call<NoticiaFeed> getItems();
}
