package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/proposals")
@RequiredArgsConstructor
@Slf4j
public class ProposalController {

    @Autowired
    private final ProposalService proposalService;

    @GetMapping("/{id}")
    public Response<Page<ProposalRes>> getProposalList(@PathVariable Long id, Pageable pageable, Authentication authentication) {
        return Response.success(proposalService.getProposalList(id, pageable, authentication.getName()));
    }
}
