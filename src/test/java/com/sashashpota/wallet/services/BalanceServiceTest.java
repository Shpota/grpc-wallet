package com.sashashpota.wallet.services;

import com.sashashpota.wallet.model.Balance;
import com.sashashpota.wallet.model.BalanceId;
import com.sashashpota.wallet.repositories.BalanceRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
@Sql(statements = "delete from balance")
public class BalanceServiceTest {
    @Autowired
    private WalletService service;
    @Autowired
    private BalanceRepository repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldGetBalances() {
        Balance first = new Balance("id", "EUR", 2);
        Balance second = new Balance("id", "USD", 3);
        repository.save(first);
        repository.save(second);

        Map<String, Integer> balances = service.getBalances("id");

        assertThat(balances.entrySet(), hasSize(2));
        assertThat(balances, allOf(hasEntry("EUR", 2), hasEntry("USD", 3)));
    }

    @Test
    public void shouldDeposit() throws WalletException {
        service.deposit("id", "EUR", 2);

        Balance balance = repository.findById(new BalanceId("id", "EUR")).get();
        assertThat(balance.getAmount(), is(2));
    }

    @Test
    public void shouldDepositGivenExistingBalance() throws WalletException {
        repository.save(new Balance("id", "EUR", 2));

        service.deposit("id", "EUR", 3);

        Balance balance = repository.findById(new BalanceId("id", "EUR")).get();
        assertThat(balance.getAmount(), is(5));
    }

    @Test
    public void shouldFailToDepositGivenNotSupportedCurrency() throws WalletException {
        expectedException.expect(WalletException.class);
        expectedException.expectMessage("unknown currency");

        service.deposit("id", "UAH", 5);
    }

    @Test
    public void shouldWithdraw() throws WalletException {
        repository.save(new Balance("id", "EUR", 3));

        service.withdraw("id", "EUR", 2);

        Balance balance = repository.findById(new BalanceId("id", "EUR")).get();
        assertThat(balance.getAmount(), is(1));
    }

    @Test
    public void shouldFailToWithdrawGivenNotSupportedCurrency() throws WalletException {
        expectedException.expect(WalletException.class);
        expectedException.expectMessage("unknown currency");

        service.withdraw("id", "UAH", 5);
    }

    @Test
    public void shouldFailToWithdrawGivenUnknownId() throws WalletException {
        expectedException.expect(WalletException.class);
        expectedException.expectMessage("insufficient funds");

        service.withdraw("id", "EUR", 5);
    }

    @Test
    public void shouldFailToWithdrawGivenInsufficientFunds() throws WalletException {
        repository.save(new Balance("id", "EUR", 3));
        expectedException.expect(WalletException.class);
        expectedException.expectMessage("insufficient funds");

        service.withdraw("id", "EUR", 5);
    }
}
