package ch.six.sixwallet.backend;

import retrofit.RestAdapter;

public class ApiProvider {

    private final String SIX_API_URL = "ppp.ti8m.ch/hackathon/";

    private SixApi mSixApi;

    public ApiProvider() {
        initSixp2pApi();
    }

    public SixApi getSixApi() {
        return mSixApi;
    }

    private void initSixp2pApi() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SIX_API_URL)
                .setLogLevel(RestAdapter.LogLevel.valueOf("FULL"))
                .build();

        restAdapter.create(SixApi.class);
    }

}
