package com.jooany.letsdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;

import jakarta.transaction.Transactional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long>, ProposalCustomRepository {

	@Transactional
	@Modifying
	@Query("DELETE FROM Proposal entity WHERE entity.sale = :sale")
	void deleteAllBySale(@Param("sale") Sale sale);
}