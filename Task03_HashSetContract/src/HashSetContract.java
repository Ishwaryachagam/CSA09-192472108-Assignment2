import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class HashSetContract {
    // Deliberately broken: equals compares id, but hashCode is inherited from Object.
    static class BrokenPatient {
        int id;
        BrokenPatient(int id) { this.id = id; }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BrokenPatient)) return false;
            return id == ((BrokenPatient) obj).id;
        }
        // Intentionally no hashCode override.
    }

    static class Patient {
        private final int id;
        private final String name;
        Patient(int id, String name) { this.id = id; this.name = name; }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Patient)) return false;
            Patient other = (Patient) obj;
            return id == other.id;
        }
        @Override public int hashCode() { return Objects.hash(id); }
        @Override public String toString() { return id + ":" + name; }
    }

    // Demonstrates why changing fields used by equals/hashCode after insertion is unsafe.
    static class MutableKey {
        int id;
        MutableKey(int id) { this.id = id; }
        @Override public boolean equals(Object obj) {
            return obj instanceof MutableKey && id == ((MutableKey) obj).id;
        }
        @Override public int hashCode() { return Objects.hash(id); }
    }

    public static void main(String[] args) {
        System.out.println("TEST 1: Broken equals/hashCode contract");
        Set<BrokenPatient> broken = new HashSet<>();
        BrokenPatient b1 = new BrokenPatient(101);
        BrokenPatient b2 = new BrokenPatient(101);
        System.out.println("b1.equals(b2): " + b1.equals(b2));
        broken.add(b1);
        broken.add(b2);
        System.out.println("HashSet size (may be 2 because hashes differ): " + broken.size());

        System.out.println("\nTEST 2: Correct equals and hashCode");
        Set<Patient> patients = new HashSet<>();
        patients.add(new Patient(201, "Asha"));
        patients.add(new Patient(201, "Asha duplicate"));
        patients.add(new Patient(202, "Ravi"));
        System.out.println("Patients: " + patients);
        System.out.println("HashSet size (expected 2): " + patients.size());

        System.out.println("\nTEST 3: Mutable key hazard");
        Set<MutableKey> keys = new HashSet<>();
        MutableKey key = new MutableKey(10);
        keys.add(key);
        System.out.println("Contains before mutation: " + keys.contains(key));
        key.id = 99;
        System.out.println("Contains after mutation (may be false): " + keys.contains(key));

        System.out.println("\nTreeSet alternative using an immutable comparable key:");
        TreeSet<Integer> sortedIds = new TreeSet<>();
        sortedIds.add(303);
        sortedIds.add(101);
        sortedIds.add(202);
        System.out.println(sortedIds);
        System.out.println("Use immutable key fields; never mutate fields used by equals/hashCode while stored.");
    }
}
