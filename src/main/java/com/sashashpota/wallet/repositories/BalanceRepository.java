package com.sashashpota.wallet.repositories;

import com.sashashpota.wallet.model.Balance;
import com.sashashpota.wallet.model.BalanceId;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface BalanceRepository extends CrudRepository<Balance, BalanceId> {
    List<Balance> findAllById_UserId(String userId);

    @Lock(PESSIMISTIC_WRITE)
    Balance findBalanceById(BalanceId id);
}
