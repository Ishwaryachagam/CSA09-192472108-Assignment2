
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class StringBenchmark {

    static volatile long checksum = 0;

    static String createTicketUsingString(int id) {
        String ticket = "Ticket ID: ";
        ticket = ticket + id;
        ticket = ticket + ", Patient: Patient";
        ticket = ticket + id;
        ticket = ticket + ", Department: Cardiology";
        ticket = ticket + ", Status: Confirmed";
        return ticket;
    }

    static String createTicketUsingBuilder(int id) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("Ticket ID: ");
        ticket.append(id);
        ticket.append(", Patient: Patient");
        ticket.append(id);
        ticket.append(", Department: Cardiology");
        ticket.append(", Status: Confirmed");
        return ticket.toString();
    }

    static String createTicketUsingBuffer(int id) {
        StringBuffer ticket = new StringBuffer();
        ticket.append("Ticket ID: ");
        ticket.append(id);
        ticket.append(", Patient: Patient");
        ticket.append(id);
        ticket.append(", Department: Cardiology");
        ticket.append(", Status: Confirmed");
        return ticket.toString();
    }

    static long measure(int count, int method) {
        long start = System.nanoTime();
        long totalLength = 0;

        for (int i = 1; i <= count; i++) {
            String ticket;

            if (method == 1)
                ticket = createTicketUsingString(i);
            else if (method == 2)
                ticket = createTicketUsingBuilder(i);
            else
                ticket = createTicketUsingBuffer(i);

            totalLength += ticket.length();
        }

        long end = System.nanoTime();
        checksum += totalLength;
        return end - start;
    }

    static long averageTime(int count, int method) {

        // Warm-up
        for (int i = 0; i < 2; i++)
            measure(count, method);

        long total = 0;

        // Five measurements
        for (int i = 0; i < 5; i++)
            total += measure(count, method);

        return total / 5;
    }

    public static void main(String[] args) {

        int[] workloads = {100, 1000, 5000};

        try {
            File folder = new File("Task01_StringBenchmark/results");
            folder.mkdirs();

            PrintWriter writer = new PrintWriter(
                new FileWriter("Task01_StringBenchmark/results/benchmark.csv"));

            writer.println(
                "Workload,String_ns,StringBuilder_ns,StringBuffer_ns");

            System.out.println("STRING PERFORMANCE BENCHMARK");

            for (int count : workloads) {

                System.out.println("\nTickets: " + count);

                long stringTime = averageTime(count, 1);
                long builderTime = averageTime(count, 2);
                long bufferTime = averageTime(count, 3);

                System.out.println("String: " + stringTime + " ns");
                System.out.println("StringBuilder: " + builderTime + " ns");
                System.out.println("StringBuffer: " + bufferTime + " ns");

                writer.println(count + "," + stringTime + ","
                    + builderTime + "," + bufferTime);
            }

            writer.close();

            System.out.println("\nCSV saved successfully!");
            System.out.println("Checksum: " + checksum);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
