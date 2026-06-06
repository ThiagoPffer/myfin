package com.thpiffer.myfin.app.resource.impl;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.mapper.BankMapper;
import com.thpiffer.myfin.app.resource.BankResource;
import com.thpiffer.myfin.app.service.BankService;
import com.thpiffer.myfin.core.dto.PagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankResourceImpl implements BankResource {

    private final BankService bankService;
    private final BankMapper bankMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<BankResponse>> getBankScrollPageByFilter(PagingRequest request) {
        var output = bankService.getBankScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter());
        return ResponseEntity.ok()
                .header("X-Has-Next", String.valueOf(output.hasNext()))
                .body(output.content().stream().map(bankMapper::toResponse).toList());
    }

}
