package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.BatchMpesaModels;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.jobo.JoboApi.globals.GlobalRepo.stkCallbackFirebaseRepo;


@Service
public class CallBackServiceImpl implements CallBackService {

    @Override
    public BatchMpesaModels.MpesaStkCallback saveNewCallback(BatchMpesaModels.MpesaStkCallback callback) {
        long id = stkCallbackFirebaseRepo.retrieveAll().size() + 1;
        callback.setDocumentId(String.valueOf(id));
        callback.setId(id);
        return stkCallbackFirebaseRepo.save(callback);
    }

    @Override
    public List<BatchMpesaModels.MpesaStkCallback> getAllCallbacks() {
        return stkCallbackFirebaseRepo.retrieveAll();
    }

    @Override
    public Boolean deleteCallback(String documentId) {
        return stkCallbackFirebaseRepo.remove(documentId);
    }


}
