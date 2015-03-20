package ch.six.sixwallet.backend;

import android.database.Observable;

import ch.six.sixwallet.backend.models.Balance;
import retrofit.http.GET;
import retrofit.http.Header;

public interface SixApi {

    @GET("/balance/")
    Observable<Balance> getCurrentBalance(@Header("usertoken") String userToken);

}
