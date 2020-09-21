package org.example;

import org.example.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class RouletteTest {

    @DisplayName("Load the player file")
    @Test
    public void loadFile() throws IOException {

        Roulette runner = new Roulette();
        runner.loadFile(runner.FILE_NAME);

        List<Player> playersList = runner.getPlayers();

        Assertions.assertEquals(2, playersList.size());
        Assertions.assertEquals("Tiki_Monkey", playersList.get(0).getName());
        Assertions.assertEquals("Barbara", playersList.get(1).getName());

        Assertions.assertEquals(new BigDecimal("1.0"), playersList.get(0).getTotalWin());
        Assertions.assertEquals(new BigDecimal("2.0"), playersList.get(1).getTotalWin());

        Assertions.assertEquals(new BigDecimal("2.0"), playersList.get(0).getTotalBet());
        Assertions.assertEquals(new BigDecimal("1.0"), playersList.get(1).getTotalBet());


    }

    @DisplayName("Find the players name")
    @Test
    public void findName(){

        Player betty = new Player("betty");
        Player haai = new Player("haai");
        Roulette runner = new Roulette();
        runner.addPlayer(betty);
        runner.addPlayer(haai);

        boolean found = runner.findPlayer("betty");

        Assertions.assertTrue(found);

        found = runner.findPlayer("afds");

        Assertions.assertFalse(found);
    }

    @DisplayName("Valid Bet")
    @Test
    public void notValidBet(){
        Roulette runner = new Roulette();

        boolean result = runner.notValidBet("ODD");
        Assertions.assertFalse(result);

        result = runner.notValidBet("EVEN");
        Assertions.assertFalse(result);

        result = runner.notValidBet("1");
        Assertions.assertFalse(result);

        result = runner.notValidBet("36");
        Assertions.assertFalse(result);

        result = runner.notValidBet("0");
        Assertions.assertTrue(result);

        result = runner.notValidBet("37");
        Assertions.assertTrue(result);

        result = runner.notValidBet("ere");
        Assertions.assertTrue(result);

        result = runner.notValidBet("@#$%^^");
        Assertions.assertTrue(result);
    }

    @DisplayName("Not valid amount")
    @Test
    public void notValidAmount(){
        Roulette runner = new Roulette();

        boolean result = runner.notValidAmount("1.0");
        Assertions.assertFalse(result);

        result = runner.notValidAmount("1.w");
        Assertions.assertTrue(result);

        result = runner.notValidAmount("gg");
        Assertions.assertTrue(result);

        result = runner.notValidAmount("@#)");
        Assertions.assertTrue(result);

        result = runner.notValidAmount("1.258");
        Assertions.assertFalse(result);
    }

    @DisplayName("Bet EVEN won")
    @Test
    public void betEvenWon(){
        //1. Create player
        Player player = new Player("Betty");
        player.addBet("EVEN", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(36);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(130, wins.doubleValue());
        Assertions.assertEquals(Player.WON, player.getBets().get(0).getOutcome());
    }

    @DisplayName("Bet ODD won")
    @Test
    public void betODDWon(){
        //1. Create player
        Player player = new Player( "Betty");
        player.addBet("ODD", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(35);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(130, wins.doubleValue());
        Assertions.assertEquals(Player.WON, player.getBets().get(0).getOutcome());
    }

    @DisplayName("Bet number won")
    @Test
    public void betNumberWon(){
        //1. Create player
        Player player = new Player("Betty");
        player.addBet("12", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(12);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(2340, wins.doubleValue());
        Assertions.assertEquals(Player.WON, player.getBets().get(0).getOutcome());
    }


    @DisplayName("Bet number loose")
    @Test
    public void betNumberLoose(){
        //1. Create player
        Player player = new Player("Betty");
        player.addBet("12", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(17);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(0, wins.doubleValue());
        Assertions.assertEquals(Player.LOOSE, player.getBets().get(0).getOutcome());
    }

    @DisplayName("Bet ODD loose")
    @Test
    public void betODDLoose(){
        //1. Create player
        Player player = new Player("Betty");
        player.addBet("ODD", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(16);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(0, wins.doubleValue());
        Assertions.assertEquals(Player.LOOSE, player.getBets().get(0).getOutcome());
    }

    @DisplayName("Bet EVEN loose")
    @Test
    public void betEVENLoose(){
        //1. Create player
        Player player = new Player("Betty");
        player.addBet("EVEN", "65");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(17);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal wins = player.getBets().get(0).getWinngs();
        Assertions.assertEquals(0, wins.doubleValue());
        Assertions.assertEquals(Player.LOOSE, player.getBets().get(0).getOutcome());
    }

    @DisplayName("Total bet and Winnings")
    @Test
    public void betTotals(){
        //1. Create player
        Player player = new Player("Betty", "45.00", "68.00");
        player.addBet("EVEN", "65");
        player.addBet("ODD", "78");
        player.addBet("23", "41");

        //2. Add the player to the game
        Roulette runner = new Roulette();
        runner.addPlayer(player);

        //3. Role the dice
        runner.setRole(17);

        //4. Play the game
        String[] args = {"test=true", "schedule=2"};
        runner.run(args);

        //5.Verify the outcome
        BigDecimal totalWin = player.getTotalWin();
        BigDecimal totalBet = player.getTotalBet();
        Assertions.assertEquals(201, totalWin.doubleValue());
        Assertions.assertEquals(252, totalBet.doubleValue());

        Assertions.assertEquals(Player.LOOSE, player.getBets().get(0).getOutcome());
        Assertions.assertEquals(Player.WON, player.getBets().get(1).getOutcome());
        Assertions.assertEquals(Player.LOOSE, player.getBets().get(2).getOutcome());
    }


}
