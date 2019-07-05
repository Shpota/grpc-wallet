package com.sashashpota.wallet.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BalanceId implements Serializable {
    private String userId;
    private String currency;

    public BalanceId() {
    }

    public BalanceId(String userId, String currency) {
        this.userId = userId;
        this.currency = currency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceId balanceId = (BalanceId) o;
        return userId.equals(balanceId.userId) &&
                currency.equals(balanceId.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currency);
    }
}
