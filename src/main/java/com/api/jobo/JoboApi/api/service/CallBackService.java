package com.api.jobo.JoboApi.api.service;



import com.api.jobo.JoboApi.api.domain.BatchMpesaModels;
import com.api.jobo.JoboApi.api.domain.MpesaModels;

import java.util.List;

public interface CallBackService {
    BatchMpesaModels.MpesaStkCallback saveNewCallback(BatchMpesaModels.MpesaStkCallback callback);
    List<BatchMpesaModels.MpesaStkCallback> getAllCallbacks();
    Boolean deleteCallback(String documentId);
}
