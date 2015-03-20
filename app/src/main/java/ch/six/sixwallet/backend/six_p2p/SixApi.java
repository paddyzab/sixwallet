package ch.six.sixwallet.backend.six_p2p;

import ch.six.sixwallet.backend.six_p2p.models.Balance;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import rx.Observable;

public interface SixApi {

    //TODO: instead of adding header by hand, we may consider using Request Interceptor
    @GET("/balance/")
    Observable<Balance> getCurrentBalance(@Header("usertoken") String userToken);

    @PUT("/balance/transaction/request/")
    void createPaymentRequest(@Header("usertoken") String userToken, @Body RequestTransaction requestTransaction, Callback<?> callback);

}
