import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListComparison {
    private static volatile long checksum;

    private static long testArrayList(int n) {
        List<Integer> list = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        for (int i = 0; i < n; i++) checksum += list.get(i); // indexed access
        list.add(n / 2, -1);
        list.remove(n / 2);
        return System.nanoTime() - start;
    }

    private static long testLinkedList(int n) {
        List<Integer> list = new LinkedList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        for (int i = 0; i < n; i++) checksum += list.get(i);
        list.add(n / 2, -1);
        list.remove(n / 2);
        return System.nanoTime() - start;
    }

    private static long testArrayDeque(int n) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) deque.addLast(i);
        for (int i = 0; i < n; i++) checksum += deque.removeFirst();
        return System.nanoTime() - start;
    }

    private static long average(int n, int type) {
        for (int i = 0; i < 2; i++) {
            if (type == 1) testArrayList(n);
            else if (type == 2) testLinkedList(n);
            else testArrayDeque(n);
        }
        long total = 0;
        for (int i = 0; i < 5; i++) {
            if (type == 1) total += testArrayList(n);
            else if (type == 2) total += testLinkedList(n);
            else total += testArrayDeque(n);
        }
        return total / 5;
    }

    public static void main(String[] args) throws Exception {
        int[] sizes = {1000, 10000, 50000};
        File folder = new File("Task02_ListComparison/results");
        folder.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(folder, "list_benchmark.csv")))) {
            pw.println("Items,ArrayList_ns,LinkedList_ns,ArrayDeque_ns");
            for (int n : sizes) {
                long a = average(n, 1), l = average(n, 2), d = average(n, 3);
                System.out.printf("Items=%d | ArrayList=%d ns | LinkedList=%d ns | ArrayDeque=%d ns%n",
                        n, a, l, d);
                pw.printf("%d,%d,%d,%d%n", n, a, l, d);
            }
        }

        // Playlist use case: random access favours ArrayList.
        ArrayList<String> playlist = new ArrayList<>();
        playlist.add("Song A");
        playlist.add("Song B");
        playlist.add("Song C");
        System.out.println("\nPlaylist: " + playlist);
        System.out.println("Current song at index 1: " + playlist.get(1));

        // Up Next is a FIFO queue: add at rear, play/remove from front.
        ArrayDeque<String> upNext = new ArrayDeque<>();
        upNext.offerLast("Song D");
        upNext.offerLast("Song E");
        upNext.offerLast("Song F");
        System.out.println("Up Next queue (front to rear): " + upNext);
        System.out.println("Playing next: " + upNext.pollFirst());
        System.out.println("Remaining queue: " + upNext);
        System.out.println("Checksum: " + checksum);
        System.out.println("CSV saved: Task02_ListComparison/results/list_benchmark.csv");
    }
}
