package org.example.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public class Bet {

    private String bet;
    private BigDecimal amount;
    private BigDecimal winnings;
    private String outcome;

    public Bet(String bet, BigDecimal amount) {
        this.bet = bet;
        this.amount = amount;
    }

    /**
     * Is the bet a number
     *
     * @return
     */
    public boolean isBetN() {
        if (bet.equalsIgnoreCase("ODD") || bet.equalsIgnoreCase("EVEN")) {
            return false;
        }
        return true;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setWinings(BigDecimal wins) {
        this.winnings = wins;
    }

    public BigDecimal getWinngs() {
        return this.winnings;
    }


    public void setWinings(String amount) {
        this.winnings = new BigDecimal(amount, new MathContext(2));;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet1 = (Bet) o;
        return Objects.equals(bet, bet1.bet) &&
                Objects.equals(amount, bet1.amount) &&
                Objects.equals(winnings, bet1.winnings) &&
                Objects.equals(outcome, bet1.outcome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bet, amount, winnings, outcome);
    }
}
