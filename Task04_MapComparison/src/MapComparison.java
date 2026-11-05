import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MapComparison {
    private static volatile long checksum;

    private static Map<Integer, String> createMap(int type, int n) {
        Map<Integer, String> map;
        if (type == 1) map = new HashMap<>();
        else if (type == 2) map = new TreeMap<>();
        else map = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) map.put(i, "Medicine-" + i);
        return map;
    }

    private static long lookupTime(Map<Integer, String> map, int[] keys) {
        long start = System.nanoTime();
        for (int key : keys) {
            String value = map.get(key);
            if (value != null) checksum += value.length();
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws Exception {
        int[] sizes = {1000, 10000, 50000};
        final int lookupCount = 100000;
        Random random = new Random(42);

        File folder = new File("Task04_MapComparison/results");
        folder.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(folder, "map_benchmark.csv")))) {
            pw.println("Entries,HashMap_100k_lookup_ns,TreeMap_100k_lookup_ns,LinkedHashMap_100k_lookup_ns");
            for (int n : sizes) {
                int[] keys = new int[lookupCount];
                for (int i = 0; i < lookupCount; i++) keys[i] = random.nextInt(n);

                Map<Integer, String> hash = createMap(1, n);
                Map<Integer, String> tree = createMap(2, n);
                Map<Integer, String> linked = createMap(3, n);

                // Warm-up
                lookupTime(hash, keys); lookupTime(tree, keys); lookupTime(linked, keys);
                long h = lookupTime(hash, keys);
                long t = lookupTime(tree, keys);
                long l = lookupTime(linked, keys);

                System.out.printf("Entries=%d | HashMap=%d ns | TreeMap=%d ns | LinkedHashMap=%d ns%n",
                        n, h, t, l);
                pw.printf("%d,%d,%d,%d%n", n, h, t, l);

                System.out.println("HashMap iteration sample: " + hash.keySet().stream().limit(5).toList());
                System.out.println("TreeMap first keys: " + tree.keySet().stream().limit(5).toList());
                System.out.println("LinkedHashMap first keys: " + linked.keySet().stream().limit(5).toList());
            }
        }

        Map<String, Integer> pharmacy = new HashMap<>();
        pharmacy.put("Paracetamol", 120);
        pharmacy.put("Cetirizine", 80);
        pharmacy.put("ORS", 60);
        System.out.println("\nPharmacy stock: " + pharmacy);
        System.out.println("Paracetamol stock: " + pharmacy.get("Paracetamol"));

        System.out.println("\nHashtable is synchronized legacy map; use ConcurrentHashMap for many concurrent updates.");
        System.out.println("Sorted array + binarySearch offers O(log n) lookup after O(n log n) sorting, "
                + "but updates are costly.");
        System.out.println("Checksum: " + checksum);
        System.out.println("CSV saved: Task04_MapComparison/results/map_benchmark.csv");
    }
}
