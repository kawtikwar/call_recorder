package com.amuramarketing.callrecorder.network;


import com.amuramarketing.callrecorder.models.CallDetailsModel;
import com.amuramarketing.callrecorder.models.CallList;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIInterface {

    //@Body – Sends Java objects as request body
    //@Query – We can simply add a method parameter with @Query() and a query parameter name, describing the type. To URL encode a query use the form:
    //@Field – send data as form-urlencoded. This requires a @FormUrlEncoded annotation attached with the method.
    //The @Field parameter works only with a POST

    @Multipart
    @POST("Services/get_call_status")
    Call<ResponseBody> postCallDetails(@Part MultipartBody.Part recordingFile,
                                       @Part("data") CallDetailsModel data);
    @Multipart
    @POST("Services/get_call_status")
    Call<ResponseBody> postCallDetail(@Part List<MultipartBody.Part> files,
                                      @Part("data") CallList datas);
}
