package com.jooany.letsdeal.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.ProposalSaveReq;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
    @Controller 가 아닌 @RestController을 사용하는 이유 : JSON 형태의 데이터로 반환하기 위해서이다.
    전자는 반환값이 View 이지만, 후자는 반환값이 직접 데이터이기 때문에 JSON 형태의 데이터로 반환할 수 있다.
 */
@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Slf4j
public class SaleController {

	private final SaleService saleService;

	@GetMapping
	public Response<Page<SaleRes>> getSaleList(SearchCondition condition, Pageable pageable,
		Authentication authentication) {
		return Response.success(saleService.getSaleList(condition, pageable, authentication.getName()));
	}

	@GetMapping("/{id}")
	public Response<SaleInfoRes> getSale(@PathVariable Long id) {
		return Response.success(saleService.getSaleInfo(id));
	}

	@PostMapping
	public Response<Void> saveSale(@Valid @RequestPart("saleCreateReq") SaleSaveReq saleCreateReq,
		@RequestPart(required = false) List<MultipartFile> imageFiles,
		Authentication authentication) throws IOException {
		saleService.saveSale(saleCreateReq, imageFiles, authentication.getName());
		return Response.success();
	}

	@PatchMapping("/{id}")
	public Response<Void> updateSale(@PathVariable Long id,
		@Valid @RequestPart("saleCreateReq") SaleSaveReq saleCreateReq,
		@RequestPart(required = false) List<MultipartFile> imageFiles,
		Authentication authentication) throws IOException {
		UserDto userDto = (UserDto)authentication.getPrincipal();
		saleService.updateSale(id, userDto.getId(), userDto.getUserRole(), saleCreateReq, imageFiles);
		return Response.success();
	}

	@DeleteMapping("/{id}")
	public Response<Void> deleteSale(@PathVariable Long id, Authentication authentication) {
		saleService.deleteSale(id, authentication.getName());
		return Response.success();
	}

	@GetMapping("/{id}/proposals")
	public Response<ProposalListRes> getProposalList(@PathVariable Long id, Pageable pageable,
		Authentication authentication) {
		return Response.success(saleService.getProposalList(id, pageable, authentication.getName()));
	}

	@PostMapping("/{id}/proposals")
	public Response<Void> saveProposal(@PathVariable Long id, @Valid @RequestBody ProposalSaveReq proposalSaveReq,
		Authentication authentication) {
		saleService.saveProposal(id, proposalSaveReq.getBuyerPrice(), authentication.getName());
		return Response.success();
	}

	@DeleteMapping("/{id}/proposals/{proposalId}")
	public Response<Void> deleteProposal(@PathVariable Long id, @PathVariable Long proposalId,
		Authentication authentication) {
		saleService.deleteProposal(id, proposalId, authentication.getName());
		return Response.success();
	}

	@PatchMapping("/{id}/proposals/{proposalId}")
	public Response<Void> updateSale(@PathVariable Long id, @PathVariable Long proposalId,
		Authentication authentication) throws IOException {
		saleService.refuseProposal(id, proposalId, authentication.getName());
		return Response.success();
	}
}
