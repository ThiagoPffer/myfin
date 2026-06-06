package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BankService {

    BankEntity getBankById(UUID id);

    ScrollingOutput<BankEntity> getBankScrollPageByFilter(PageRequest page, String filter);

}
