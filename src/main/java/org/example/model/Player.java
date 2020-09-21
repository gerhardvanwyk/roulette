package org.example.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final String WON = "WON";
    public static final String LOOSE = "LOOSE";

    private final String name;
    private BigDecimal totalWin;
    private BigDecimal totalBet;

    private final List<Bet> bets = new ArrayList<>();

    public Player(String name){
        this.name = name;
    }

    public Player(String name, String totalWin, String totalBet){
        this.name = name;
        this.totalWin = new BigDecimal(totalWin);
        this.totalBet = new BigDecimal(totalBet);
    }

    public String getName() {
        return name;
    }

    public void addBet(String bet, String amount) {
        this.bets.add(new Bet(bet, new BigDecimal(amount, new MathContext(2))));
    }

    public List<Bet> getBets() {
        return this.bets;
    }

    public BigDecimal getTotalWin() {
        return totalWin;
    }

    public void addTotalWin(BigDecimal win) {
        this.totalWin = this.totalWin.add(win);
    }

    public BigDecimal getTotalBet() {
        return totalBet;
    }

    public void addTotalBet(BigDecimal bet) {
        this.totalBet = this.totalBet.add(bet);
    }
}
