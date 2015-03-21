package ch.six.sixwallet.backend;

import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import retrofit.RestAdapter;

public class ApiProvider {

    private final String SIX_API_URL = "https://ppp.ti8m.ch/hackathon/";

    private final String RUNKEEPER_API_URL = "https://api.runkeeper.com";

    private SixApi mSixApi;

    private RunKeeperApi mRunKeeperApi;

    public ApiProvider() {
        initSixApi();
        initRunKeeperApi();
    }

    public SixApi getSixApi() {
        return mSixApi;
    }

    public RunKeeperApi getRunKeeperApi() {
        return mRunKeeperApi;
    }

    private void initSixApi() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SIX_API_URL)
                //.setLogLevel(RestAdapter.LogLevel.valueOf("FULL"))
                .build();

        mSixApi = restAdapter.create(SixApi.class);
    }

    private void initRunKeeperApi() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(RUNKEEPER_API_URL)
                .setLogLevel(RestAdapter.LogLevel.valueOf("FULL"))
                .build();

        mRunKeeperApi = restAdapter.create(RunKeeperApi.class);
    }

}
