package com.sashashpota.wallet.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
@Sql(statements = "delete from balance")
public class BalanceServiceIT {
    @Autowired
    private WalletService service;

    @Test
    public void integrationScenario() throws WalletException {
        // 1. Make a withdrawal of USD 200 for user with id 1.
        withdraw100USDAndValidate();
        // 2. Make a deposit of USD 100 to user with id 1.
        service.deposit("1", "USD", 100);
        // 3. Check that all balances are correct
        var balances = service.getBalances("1");
        assertThat(balances.entrySet(), hasSize(1));
        assertThat(balances, hasEntry("USD", 100));
        // 4. Make a withdrawal of USD 200 for user with id 1.
        withdraw100USDAndValidate();
        // 5. Make a deposit of EUR 100 to user with id 1.
        service.deposit("1", "EUR", 100);
        // 6. Check that all balances are correct
        assertBalances(100);
        // 7. Make a withdrawal of USD 200 for user with id 1.
        withdraw100USDAndValidate();
        // 8. Make a deposit of USD 100 to user with id 1.
        service.deposit("1", "USD", 100);
        // 9. Check that all balances are correct
        assertBalances(200);
        // 10. Make a withdrawal of USD 200 for user with id 1.
        service.withdraw("1", "USD", 200);
        // 11. Check that all balances are correct
        assertBalances(0);
        // 12. Make a withdrawal of USD 200 for user with id 1.
        withdraw100USDAndValidate();
    }

    private void assertBalances(int usdAmount) {
        var balances = service.getBalances("1");
        assertThat(balances.entrySet(), hasSize(2));
        assertThat(balances, allOf(hasEntry("USD", usdAmount), hasEntry("EUR", 100)));
    }

    private void withdraw100USDAndValidate() {
        try {
            service.withdraw("1", "USD", 200);
            fail("WalletException should be thrown");
        } catch (WalletException e) {
            assertEquals("insufficient funds", e.getMessage());
        }
    }
}
