public class RedesignedTicketBuilder {
    // Revised implementation: build the ticket in one mutable buffer.
    public static String createTicket(int id) {
        return new StringBuilder()
                .append("Ticket ID: ").append(id)
                .append(", Patient: Patient").append(id)
                .append(", Department: Cardiology")
                .append(", Status: Confirmed")
                .toString();
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) System.out.println(createTicket(i));
    }
}
