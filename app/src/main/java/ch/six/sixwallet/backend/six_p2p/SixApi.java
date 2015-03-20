package ch.six.sixwallet.backend.six_p2p;

import java.util.List;

import ch.six.sixwallet.backend.six_p2p.models.Balance;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import ch.six.sixwallet.backend.six_p2p.models.Transaction;
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

    @GET("/balance/transactions/")
    Observable<List<Transaction>> getTransactions(@Header("usertoken") String userToken);

    @PUT("/balance/transaction/request/")
    void createPaymentRequest(@Header("usertoken") String userToken,
            @Body RequestTransaction requestTransaction, Callback<?> callback);

}
