package ch.six.sixwallet.backend.runkeeper;

import ch.six.sixwallet.backend.runkeeper.models.FitnessActivityFeedPage;
import ch.six.sixwallet.backend.runkeeper.models.User;
import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

public interface RunKeeperApi {

    String authorisationUrl = "https://runkeeper.com/apps/authorize";
    String accessTokenUrl = "https://runkeeper.com/apps/token";

    String CLIENT_ID = "abe11cbab0f949b183164bfe2f3ea893";
    String CLIENT_SECRET = "0876a8b033414207a7bd48f5d5d8c9e5";

    @GET("/fitnessActivities")
    Observable<FitnessActivityFeedPage> getFitnessActivityFeedPage(@Header("Authorization") String userToken);

    @GET("/user")
    Observable<User> getUserDate(@Header("Authorization") String userToken);
}
