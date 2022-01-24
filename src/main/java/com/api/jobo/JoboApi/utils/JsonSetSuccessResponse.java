package com.api.jobo.JoboApi.utils;


public class JsonSetSuccessResponse {
    public static JsonResponse setResponse(int apiCode, String apiDescription, String trxId, Object content) {
        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        serviceResponse.setData(content);
        return serviceResponse;
    }

    public static JsonResponse setResponse(Integer apiCode, String apiDescription,
                                                                          String trxId) {

        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;

        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;

    }


    public static JsonResponse setResponseNoContent(int apiCode, String apiDescription, String trxId) {
        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;
    }
    


}

