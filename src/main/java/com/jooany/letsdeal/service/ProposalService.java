package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.repository.ProposalRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public ProposalListRes getProposalList(Long id, Pageable pageable, String userName){
        checkIsSaleExist(id);
        Page<ProposalRes> proposalList = proposalRepository.findAllBySaleId(id, userName, pageable);
        List<MyProposalRes> myProposalList = proposalRepository.findAllBySaleIdAndUserName(id, userName);

        ProposalListRes proposalListRes = new ProposalListRes(proposalList, myProposalList);
        return proposalListRes;
    }

    @Transactional
    public void saveProposal(Long id, String userName){

    }


    private Sale checkIsSaleExist(Long saleId){
        if(saleRepository.countSaleById(saleId) == 0){
            throw new LetsDealAppException(ErrorCode.SALE_NOT_FOUND);
        }
        return null;
    }
}
