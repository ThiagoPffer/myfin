package com.thpiffer.myfin.app.service.impl;

import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.repository.BankRepository;
import com.thpiffer.myfin.app.service.BankService;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public BankEntity getBankById(UUID id) {
        return bankRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Bank not found with id: " + id));
    }

    @Override
    public ScrollingOutput<BankEntity> getBankScrollPageByFilter(PageRequest page, String filter) {
        var slice = bankRepository.findAll(filter, page);
        return new ScrollingOutput<>(slice.hasNext(), slice.getContent());
    }

}
