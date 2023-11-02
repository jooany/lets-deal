package com.jooany.letsdeal.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.jooany.letsdeal.service.SaleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedissonLockMaxProposalFacade {

	private RedissonClient redissonClient;

	private SaleService saleService;

	public RedissonLockMaxProposalFacade(RedissonClient redissonClient, SaleService saleService) {
		this.redissonClient = redissonClient;
		this.saleService = saleService;
	}

	public void saveProposal(Long saleId, Integer buyerPrice, String userName) {
		RLock lock = redissonClient.getLock(saleId.toString());

		try {
			boolean available = lock.tryLock(60, 1, TimeUnit.SECONDS);

			if (!available) {
				log.info("lock 획득 실패");
				return;
			}

			saleService.saveProposal(saleId, buyerPrice, userName);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	public void deleteProposal(Long saleId, Long proposalId, String userName) {
		RLock lock = redissonClient.getLock(saleId.toString());

		try {
			boolean available = lock.tryLock(60, 1, TimeUnit.SECONDS);

			if (!available) {
				log.info("lock 획득 실패");
				return;
			}

			saleService.deleteProposal(saleId, proposalId, userName);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}