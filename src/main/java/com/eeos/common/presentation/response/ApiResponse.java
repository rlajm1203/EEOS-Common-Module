package com.eeos.common.presentation.response;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

// TODO: Spring Web 의존성 없애기
@Getter
public class ApiResponse<B> extends ResponseEntity<B> {

	public ApiResponse(final HttpStatus status) {
		super(status);
	}

	public ApiResponse(final B body, final HttpStatus status) {
		super(body, status);
	}

	public ApiResponse(final B body, MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, new HttpHeaders(headers), status);
	}

    public ApiResponse(final B body, HttpHeaders headers , HttpStatus status){
        super(body, headers, status);
    }
}
