package com.jooany.letsdeal.controller.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jooany.letsdeal.controller.dto.MyProposalRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProposalListRes {
	private Page<ProposalRes> proposalList;
	private List<MyProposalRes> myProposalList;
}
