package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.request.ProposalSaveReq;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/proposals")
@RequiredArgsConstructor
@Slf4j
public class ProposalController {

    @Autowired
    private final ProposalService proposalService;

    @GetMapping("/{id}")
    public Response<ProposalListRes> getProposalList(@PathVariable Long id, Pageable pageable, Authentication authentication) {
        return Response.success(proposalService.getProposalList(id, pageable, authentication.getName()));
    }

    @PostMapping("/{id}")
    public Response<Void> saveProposal(@PathVariable Long id, @RequestBody ProposalSaveReq proposalSaveReq, Authentication authentication){
        proposalService.saveProposal(id, proposalSaveReq.getBuyerPrice(), authentication.getName());
        return Response.success();
    }
}
