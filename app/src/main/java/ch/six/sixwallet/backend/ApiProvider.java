package ch.six.sixwallet.backend;

import ch.six.sixwallet.backend.six_p2p.SixApi;
import retrofit.RestAdapter;

public class ApiProvider {

    private final String SIX_API_URL = "https://ppp.ti8m.ch/hackathon/";

    private SixApi mSixApi;

    public ApiProvider() {
        initSixApi();
    }

    public SixApi getSixApi() {
        return mSixApi;
    }

    private void initSixApi() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SIX_API_URL)
                .setLogLevel(RestAdapter.LogLevel.valueOf("FULL"))
                .build();

        mSixApi = restAdapter.create(SixApi.class);
    }

}
