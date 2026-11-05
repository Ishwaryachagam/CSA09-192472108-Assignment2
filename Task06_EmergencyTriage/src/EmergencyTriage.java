import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class EmergencyTriage {
    static class Patient implements Comparable<Patient> {
        final String name;
        final int severity; // 1 = most urgent, 5 = least urgent
        final long arrivalOrder;

        Patient(String name, int severity, long arrivalOrder) {
            this.name = name;
            this.severity = severity;
            this.arrivalOrder = arrivalOrder;
        }

        @Override public int compareTo(Patient other) {
            int bySeverity = Integer.compare(this.severity, other.severity);
            if (bySeverity != 0) return bySeverity;
            return Long.compare(this.arrivalOrder, other.arrivalOrder);
        }

        @Override public String toString() {
            return name + " [severity=" + severity + ", arrival=" + arrivalOrder + "]";
        }
    }

    private static long benchmark(int n) {
        PriorityQueue<Patient> queue = new PriorityQueue<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++)
            queue.offer(new Patient("Patient-" + i, (i % 5) + 1, i));
        while (!queue.isEmpty()) queue.poll();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws Exception {
        PriorityQueue<Patient> triage = new PriorityQueue<>();
        long arrival = 0;

        triage.offer(new Patient("Asha", 3, arrival++));
        triage.offer(new Patient("Ravi", 1, arrival++));
        triage.offer(new Patient("Meena", 2, arrival++));
        triage.offer(new Patient("Kiran", 1, arrival++));
        triage.offer(new Patient("Divya", 4, arrival++));
        triage.offer(new Patient("Arun", 2, arrival++));
        triage.offer(new Patient("Nila", 5, arrival++));
        triage.offer(new Patient("Vijay", 3, arrival++));

        System.out.println("EMERGENCY TRIAGE ORDER (polling priority queue):");
        while (!triage.isEmpty()) System.out.println(triage.poll());

        int[] sizes = {1000, 10000, 50000};
        File folder = new File("Task06_EmergencyTriage/results");
        folder.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(folder, "triage_benchmark.csv")))) {
            pw.println("Patients,InsertAndPoll_ns");
            for (int n : sizes) {
                benchmark(n); // warm-up
                long elapsed = benchmark(n);
                System.out.println("Benchmark n=" + n + ": " + elapsed + " ns");
                pw.printf("%d,%d%n", n, elapsed);
            }
        }

        System.out.println("\nStarvation: low-priority patients may wait too long if urgent arrivals continue.");
        System.out.println("Aging: gradually improve a waiting patient's effective priority over time.");
        System.out.println("Alternatives: a linked-list queue (simple FIFO) or five FIFO queues, "
                + "one per severity, with a policy to prevent starvation.");
        System.out.println("CSV saved: Task06_EmergencyTriage/results/triage_benchmark.csv");
    }
}
