import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenericLeaderboard {
    static class Player {
        private final String name;
        private final int points;
        Player(String name, int points) { this.name = name; this.points = points; }
        public String getName() { return name; }
        public int getPoints() { return points; }
        @Override public String toString() { return name + " (" + points + ")"; }
    }

    // Bounded generic method: accepts Player or subclasses.
    static <T extends Player> void printPlayers(List<T> players) {
        for (T player : players) System.out.println(player);
    }

    // Wildcard: accepts a list of any Player subtype.
    static void showTopPlayers(List<? extends Player> players, int limit) {
        for (int i = 0; i < Math.min(limit, players.size()); i++)
            System.out.println((i + 1) + ". " + players.get(i));
    }

    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Panthers", 90));
        players.add(new Player("Eagles", 95));
        players.add(new Player("Cobras", 90));
        players.add(new Player("Bisons", 85)); // included so all four teams are tested

        Comparator<Player> ranking = Comparator
                .comparingInt(Player::getPoints).reversed()
                .thenComparing(Player::getName);

        players.sort(ranking);

        System.out.println("LEADERBOARD (points descending, name ascending for ties)");
        showTopPlayers(players, players.size());

        System.out.println("\nGeneric bounded method:");
        printPlayers(players);

        System.out.println("\nTie handling:");
        System.out.println("Cobras and Panthers both have 90 points; name breaks the tie.");
        System.out.println("Comparator transitivity: if A ranks before B and B before C, "
                + "the comparator must rank A before C. Use consistent comparison keys.");

        // Demonstrate comparator contract on a sorted copy.
        List<Player> copy = new ArrayList<>(players);
        Collections.sort(copy, ranking);
        System.out.println("Sorted copy: " + copy);

        System.out.println("\nComplexity: sorting is O(n log n); displaying all players is O(n).");
    }
}
