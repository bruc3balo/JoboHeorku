package com.api.jobo.JoboApi.api.repository;

import com.api.jobo.JoboApi.api.domain.MpesaModels;
import com.api.jobo.JoboApi.globals.GlobalVariables;
import com.api.jobo.JoboApi.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


@Repository
public class StkCallbackFirebaseRepo extends AbstractFirestoreRepository<MpesaModels.MpesaStkCallback> {

    protected StkCallbackFirebaseRepo(Firestore firestore) {
        super(firestore, GlobalVariables.stkCallback);
    }
}
