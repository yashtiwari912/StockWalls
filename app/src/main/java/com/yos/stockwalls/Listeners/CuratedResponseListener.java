package com.yos.stockwalls.Listeners;

import com.yos.stockwalls.Models.CuratedApiResponse;

public interface CuratedResponseListener {
    void onFetch(CuratedApiResponse response,String message);
    void onError(String message);


}
