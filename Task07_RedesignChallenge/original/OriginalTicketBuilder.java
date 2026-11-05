public class OriginalTicketBuilder {
    // Baseline implementation: repeated immutable String concatenation.
    public static String createTicket(int id) {
        String ticket = "Ticket ID: ";
        ticket = ticket + id;
        ticket = ticket + ", Patient: Patient" + id;
        ticket = ticket + ", Department: Cardiology";
        ticket = ticket + ", Status: Confirmed";
        return ticket;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) System.out.println(createTicket(i));
    }
}
