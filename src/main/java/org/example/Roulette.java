package org.example;

import org.example.model.Bet;
import org.example.model.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Roulette {

    private Logger log = Logger.getLogger(Roulette.class.getName());

    private static final String FILE_ARG = "file";

    private static final String TEST_ARG = "test";

    private static final String SCHEDULE_ARG = "schedule";

    static final String FILE_NAME = "players.txt";

    private List<Player> players = Collections.synchronizedList(new ArrayList<>());

    private Round round;

    private int role;

    private Integer schedule = 30;

    private boolean test = false;

    public void run(String... args) {

        //Use the default file if no file arg is found
        String fileName = FILE_NAME;

        for(String arg: args){
            String[] param = arg.split("=");

            switch (param[0]){

                case FILE_ARG:{
                    fileName = param[1];
                    break;
                }

                case SCHEDULE_ARG:{
                    schedule = Integer.valueOf(param[1]);
                    break;
                }

                case TEST_ARG:{
                    test = Boolean.parseBoolean(param[1]);
                    break;
                }
            }
        }

        if(!test) {
            try {
                loadFile(fileName);
            }catch (IOException e){
                log.log(Level.SEVERE, "Could not load file " + fileName, e);
            }
            loadPlayers();
        }

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
        round = new Round(players);
        executor.scheduleAtFixedRate(round, schedule, schedule, TimeUnit.SECONDS);

        if(test){
            try {
                Thread.sleep((schedule * 1000) + 500 );
            } catch (InterruptedException e) {
                log.severe("Interrupted??");
            }
        }

        while(true && !test){
            loadPlayers();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.fine("Thread Interrupted");
            }
        }
    }

    void loadPlayers() {
        log.finest("Loading players");
        Scanner in = new Scanner(System.in);

        String name = playerName(in);
        String bt = bet(in);
        String amt = amount(in);

        updatePlayer(name, bt, amt);
    }

    private void updatePlayer(String name, String bet, String amount) {
        synchronized (players) {
            for (Player player : players) {
                if (player.getName().equalsIgnoreCase(name)) {
                    player.addBet(bet, amount);
                }
            }
        }
    }

    private String amount(Scanner in) {
        System.out.print("Your amount: ");
        String amount = in.nextLine();
        while(notValidAmount(amount)){
            System.out.println("Not a valid amount");
            System.out.print("Your amount: ");
            amount = in.nextLine();
        }
        return amount;
    }

    private String bet(Scanner in) {
        System.out.print("Your bet, valid values are [1..36, ODD, EVEN]:");
        String bet = in.nextLine();
        while(notValidBet(bet)){
            System.out.println("Your bet is not valid");
            System.out.print("Your bet, valid values are [1..36, ODD, EVEN]:");
            bet = in.nextLine();
        }
        return bet;
    }

    private String playerName(Scanner in) {
        System.out.print("Player name: ");
        String name = in.nextLine();
        while(!findPlayer(name)){
            System.out.println("Player not found");
            System.out.print("Player name: ");
            name = in.nextLine();
        }
        return name;
    }

    boolean notValidBet(String bet) {

        if(bet.equalsIgnoreCase("ODD"))
            return false;

        if(bet.equalsIgnoreCase("EVEN"))
            return false;

        try {
            int block = Integer.parseInt(bet);
            return block < 1 || block > 36;

        }catch (NumberFormatException e){
            return true;
        }
    }

    void loadFile(String fileName) throws IOException {
        Path file = Path.of(fileName);
        if(Files.isReadable(file)){
            Stream<String> lines = Files.lines(file);
            List<String> lineList = lines.collect(Collectors.toList());

            for(String line: lineList){
                String[] player = line.split(",");
                String name = player[0];
                String totalWin = player[1];
                String totalBet = player[2];
                players.add(new Player(name, totalWin, totalBet));
            }


        }else{
            log.severe("Could not load players from File" + file.toAbsolutePath().toString());
            System.exit(1);
        }
        log.finest("Players File Read: " + players);

    }

    List<Player> getPlayers(){
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean findPlayer(String name) {

        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name))
                return true;
        }

        return false;
    }

    public boolean notValidAmount(String amount) {

        try {
            Double amt = Double.parseDouble(amount);
        }catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    public void setRole(int role) {
        this.role = role;
    }

    class Round extends TimerTask{

        private int number = 1;
        private final List<Player> players;
        private StringBuffer printBuffer = new StringBuffer();

        public Round(List<Player> players){
            this.players = players;
        }

        private boolean isEven(int outcome){
            return (outcome % 2) == 0;
        }

        private BigDecimal addWinings(Player player, Bet bet, int multiple){
            BigDecimal wins = bet.getAmount().multiply(new BigDecimal(multiple));
            System.out.println( "\n" + player.getName() + " YOU HAVE WON! " + "R" + wins);
            bet.setWinings(wins);
            return wins;
        }

        private void play(Player player, int role){
            BigDecimal wins;

            for(Bet bet: player.getBets()) {
                String outcome = Player.WON;
                if (bet.isBetN() && Integer.parseInt(bet.getBet()) == role) {
                    wins = addWinings(player, bet, 36);

                    //outcome is even && player bet EVEN
                }else if (isEven(role) && bet.getBet().equalsIgnoreCase("EVEN")) {
                    wins = addWinings(player, bet,2);

                    //outcome is odd && playyer bet ODD
                }else if (!isEven(role) && bet.getBet().equalsIgnoreCase("ODD")) {
                    wins = addWinings(player, bet,2);

                }else {

                    System.out.println("\n" + player.getName() + " You loose! " + "R" + bet.getAmount());
                    wins = new BigDecimal("0");
                    outcome = Player.LOOSE;

                }
                bet.setOutcome(outcome);
                bet.setWinings(wins);
                player.addTotalWin(wins);
                player.addTotalBet(bet.getAmount());
                print(player, bet);
            }

        }

        private void print(Player player, Bet bet) {
            printBuffer.append("\n").append(player.getName()).append("\t\t\t").append(bet.getBet()).append("\t\t")
                    .append(bet.getOutcome()).append("\t\t").append(bet.getWinngs().toPlainString()).append("\n");
        }


        @Override
        public void run() {

            synchronized (players){
                Random random = new Random(System.currentTimeMillis());
                if(!test) {
                    role = random.nextInt(37);
                }
                printBuffer.append("\n\n").append("Round: ").append(number++).append('\n')
                        .append("Roled: ").append(role).append('\n')
                        .append("Player").append("\t\t\t").append("Bet").append("\t\t").append("Outcome")
                        .append("\t\t").append("Winnings").append("\n")
                        .append("---").append("\n");



                for(Player player : players){
                    play(player, role);
                }

                printBuffer.append('\n').append("\t\t\t").append("Total Win").append("\t\t").append("Total Bet").append("\n")
                        .append("-----").append("\n");
                for(Player player: players){
                    printTotals(player);
                }
            }
            System.out.println(printBuffer.toString());
            printBuffer = new StringBuffer();
        }

        private void printTotals(Player player) {
            this.printBuffer.append(player.getName()).append("\t\t\t").append(player.getTotalWin().toPlainString())
                    .append("\t\t").append(player.getTotalBet().toPlainString()).append("\n");
        }
    }
}

