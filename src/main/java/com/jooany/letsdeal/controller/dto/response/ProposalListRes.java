package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProposalListRes {
    private Page<ProposalRes> proposalList;
    private List<MyProposalRes> myProposalList;
}
