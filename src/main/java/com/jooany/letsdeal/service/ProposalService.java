package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.ProposalRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import com.jooany.letsdeal.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProposalListRes getProposalList(Long saleId, Pageable pageable, String userName){
        checkIsSaleExist(saleId);
        Page<ProposalRes> proposalList = proposalRepository.findAllBySaleId(saleId, userName, pageable);
        List<MyProposalRes> myProposalList = proposalRepository.findAllBySaleIdAndUserName(saleId, userName);

        ProposalListRes proposalListRes = new ProposalListRes(proposalList, myProposalList);
        return proposalListRes;
    }

    @Transactional
    public void saveProposal(Long saleId, Integer buyerPrice, String userName){
        User user = getUserOrException(userName);
        Sale sale = getSaleOrException(saleId);
        Proposal proposal = Proposal.of(user, sale, buyerPrice);
        proposalRepository.save(proposal);
    }

    private Sale checkIsSaleExist(Long saleId){
        if(saleRepository.countSaleById(saleId) == 0){
            throw new LetsDealAppException(ErrorCode.SALE_NOT_FOUND);
        }
        return null;
    }

    private User getUserOrException(String userName){
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 사용자를 찾을 수 없습니다.", userName)));
    }

    private Sale getSaleOrException(Long saleId){
        return saleRepository.findById(saleId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
    }
}
