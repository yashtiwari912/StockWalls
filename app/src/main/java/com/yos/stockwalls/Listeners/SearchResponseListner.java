package com.yos.stockwalls.Listeners;

import com.yos.stockwalls.Models.SearchApiResponse;

public interface SearchResponseListner {
    void onFetch(SearchApiResponse response, String message);
    void onError(String message);


}
