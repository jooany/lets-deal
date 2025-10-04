package com.jooany.letsdeal.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooany.letsdeal.controller.dto.ProposalMessage;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.ProposalSaveReq;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.facade.RedissonLockMaxProposalFacade;
import com.jooany.letsdeal.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

	private final SaleService saleService;
	private final RedissonLockMaxProposalFacade redissonLockMaxProposalFacade;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

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
	public Response<Void> updateSale(
		@PathVariable Long id,
		@Valid @RequestPart("saleCreateReq") SaleSaveReq saleCreateReq,
		@RequestPart(required = false) List<MultipartFile> imageFiles,
		Authentication authentication
	) throws IOException {
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
	public Response<ProposalListRes> getProposalList(
		@PathVariable Long id,
		Pageable pageable,
		Authentication authentication
	) {
		return Response.success(saleService.getProposalList(id, pageable, authentication.getName()));
	}

	@PostMapping("/{id}/proposals")
	public Response<Void> saveProposal(
		@PathVariable Long id,
		@Valid @RequestBody ProposalSaveReq proposalSaveReq,
		Authentication authentication
	) {
		redissonLockMaxProposalFacade.saveProposal(id, proposalSaveReq.getBuyerPrice(), authentication.getName());
		return Response.success();
	}

	@PostMapping("/{id}/proposals-async")
	public Response<Void> saveProposalAsync(
		@PathVariable("id") Long productId,
		@Valid @RequestBody ProposalSaveReq proposalSaveReq,
		Authentication authentication
	) throws JsonProcessingException {
		ProposalMessage message = new ProposalMessage(
			productId,
			authentication.getName(),
			proposalSaveReq.getBuyerPrice()
		);

		String jsonMessage = objectMapper.writeValueAsString(message);

		kafkaTemplate.send("proposal-topic", jsonMessage);

		return Response.success();
	}

	@DeleteMapping("/{id}/proposals/{proposalId}")
	public Response<Void> deleteProposal(
		@PathVariable Long id,
		@PathVariable Long proposalId,
		Authentication authentication
	) {
		redissonLockMaxProposalFacade.deleteProposal(id, proposalId, authentication.getName());
		return Response.success();
	}

	@PatchMapping("/{id}/proposals/{proposalId}")
	public Response<Void> updateSale(
		@PathVariable Long id,
		@PathVariable Long proposalId,
		Authentication authentication
	) throws IOException {
		saleService.refuseProposal(id, proposalId, authentication.getName());
		return Response.success();
	}
}
