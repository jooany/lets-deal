package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.fixture.dto.DtoFixture;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.ProposalRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import com.jooany.letsdeal.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {
    @Mock
    private SaleRepository saleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProposalRepository proposalRepository;

    @InjectMocks
    private ProposalService proposalService;

    @DisplayName("가격제안 목록을 조회한다.")
    @Test
    @WithMockUser
    void getProposalList(){
        Long saleId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        String userName = "testUser";
        List<ProposalRes> proposals = DtoFixture.createProposalResList();
        Page<ProposalRes> proposalPages = new PageImpl<>(proposals, pageable, proposals.size());
        List<MyProposalRes> myProposals = DtoFixture.createMyProposalResList();
        given(saleRepository.countSaleById(saleId)).willReturn(1L);
        given(proposalRepository.findAllBySaleId(saleId, userName, pageable)).willReturn(proposalPages);
        given(proposalRepository.findAllBySaleIdAndUserName(saleId, userName)).willReturn(myProposals);

        ProposalListRes result = proposalService.getProposalList(saleId, pageable, userName);

        assertThat(result).isNotNull();
        assertThat(result.getProposalList().getContent()).isEqualTo(proposals);
        verify(saleRepository, times(1)).countSaleById(saleId);
        verify(proposalRepository, times(1)).findAllBySaleId(saleId, userName, pageable);
    }

    @DisplayName("상품에 가격을 제안한다.")
    @Test
    @WithMockUser
    void saveProposal(){
        Long saleId = 1L;
        Integer buyerPrice = 5000;
        String userName = "testUser";
        Sale sale = EntityFixture.createSale();
        User writer = sale.getUser();
        given(userRepository.findByUserName(userName)).willReturn(Optional.of(writer));
        given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));

        proposalService.saveProposal(1L, buyerPrice, userName);

        verify(userRepository, times(1)).findByUserName(userName);
        verify(saleRepository, times(1)).findById(saleId);
        verify(proposalRepository, times(1)).save(any(Proposal.class));
    }
}