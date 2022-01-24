package com.api.jobo.JoboApi.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class PayJsonResponse {
	private int code;
	private boolean success;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String trx_id;
	private Object data;


	public PayJsonResponse() {
		super();
	}



	public PayJsonResponse(boolean success, String message, String trx_id, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.trx_id = trx_id;
		this.data = data;
	}
}
