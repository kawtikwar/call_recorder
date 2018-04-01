package com.amuramarketing.callrecorder.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.amuramarketing.callrecorder.database.DatabaseUtils;
import com.amuramarketing.callrecorder.models.CallDetailsModel;
import com.amuramarketing.callrecorder.models.CallList;
import com.amuramarketing.callrecorder.network.APIUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this


    public UploadService() {
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
//            Toast.makeText(this, "Inside Upload service", Toast.LENGTH_SHORT).show();
          //  uploadCallLogs();
        }
    }

    private void uploadCallLogs() {
        ArrayList<CallDetailsModel> callDetailsModels = DatabaseUtils.getCallDetails(this);
        if (null != callDetailsModels && !callDetailsModels.isEmpty()) {
            List<MultipartBody.Part> files = new ArrayList<MultipartBody.Part>();
            List<CallDetailsModel> callDetailsList = new ArrayList<CallDetailsModel>();

            for (int i = 0; i < callDetailsModels.size(); i++) {
                CallDetailsModel callDetailsToPost = new CallDetailsModel();

                callDetailsToPost.setCallStatus(callDetailsModels.get(i).getCallStatus());
                callDetailsToPost.setCallType(callDetailsModels.get(i).getCallType());
                callDetailsToPost.setDate(callDetailsModels.get(i).getDate());
                callDetailsToPost.setDuration(callDetailsModels.get(i).getDuration());
                callDetailsToPost.setStartTime(callDetailsModels.get(i).getStartTime());
                callDetailsToPost.setEndTime(callDetailsModels.get(i).getEndTime());
                File file = new File(callDetailsModels.get(i).getFilePath());

                String fileName = System.currentTimeMillis() + "_" + file.getName();

                callDetailsToPost.setFilePath(fileName);

                RequestBody reqFile = RequestBody.create(MediaType.parse("audio"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]",
                        fileName, reqFile);

                files.add(body);
                callDetailsList.add(callDetailsToPost);
            }








                final CallList callList = new CallList();
                callList.setCallList(callDetailsList);


                APIUtils.getAPIInterface().postCallDetail(files, callList).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("response", "" + response.message());
                        if (response != null && response.message() != null) {
                            try {
                                Gson gson = new Gson();
                                CallList responseCallList = gson.fromJson(response.message(), CallList.class);
                                if (responseCallList != null && responseCallList.getCallList() != null && !responseCallList.getCallList().isEmpty()) {
                                    for (int i = 0; i < responseCallList.getCallList().size(); i++) {
                                        responseCallList.getCallList().get(i).setIs_sync(1);// set sync flag true
                                        DatabaseUtils.insertCallDetails(UploadService.this, responseCallList.getCallList().get(i));
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override


                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.e("onFailure", "" + t.getCause());
                    }
                });

        }
    }
}