package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.core.dto.PagingRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankResource {

    ResponseEntity<List<BankResponse>> getBankScrollPageByFilter(PagingRequest request);

}
