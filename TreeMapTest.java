import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreeMapTest {
    private TreeMap<Integer, String> treeMap;

    @BeforeEach
    public void setUp() {
        treeMap = new TreeMap<>();
    }

    @Test
    public void testSizeInitiallyZero() {
        assertEquals(0, treeMap.size());
    }

    @Test
    public void testPutAndSize() {
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        assertEquals(2, treeMap.size());
    }

    @Test
    public void testPutAndGet() {
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        assertEquals("One", treeMap.get(1));
        assertEquals("Two", treeMap.get(2));
    }

    @Test
    public void testPutAndContainsKey() {
        treeMap.put(1, "One");
        assertTrue(treeMap.containsKey(1));
        assertFalse(treeMap.containsKey(2));
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(treeMap.get(1));
    }

    @Test
    public void testClear() {
        treeMap.put(1, "One");
        treeMap.clear();
        assertEquals(0, treeMap.size());
        assertNull(treeMap.get(1));
    }

    @Test
    public void testToKeyArray() {
        treeMap.put(2, "Two");
        treeMap.put(1, "One");
        Integer[] keys = treeMap.toKeyArray(new Integer[2]);
        assertArrayEquals(new Integer[]{1, 2}, keys);
    }

    @Test
    public void testToValueArray() {
        treeMap.put(2, "Two");
        treeMap.put(1, "One");
        String[] values = treeMap.toValueArray(new String[2]);
        assertArrayEquals(new String[]{"One", "Two"}, values);
    }

    @Test
    public void testToKeyArrayWithLargerArray() {
        treeMap.put(2, "Two");
        treeMap.put(1, "One");
        Integer[] keys = treeMap.toKeyArray(new Integer[5]);
        assertArrayEquals(new Integer[]{1, 2, null, null, null}, keys);
    }

    @Test
    public void testToValueArrayWithLargerArray() {
        treeMap.put(2, "Two");
        treeMap.put(1, "One");
        String[] values = treeMap.toValueArray(new String[5]);
        assertArrayEquals(new String[]{"One", "Two", null, null, null}, values);
    }

    @Test
    public void testPutDuplicateKey() {
        treeMap.put(1, "One");
        treeMap.put(1, "Uno");
        assertEquals("Uno", treeMap.get(1));
    }

    @Test
    public void testPutNullKey() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            treeMap.put(null, "NullKey");
        });
        assertEquals("Key cannot be null.", thrown.getMessage());
    }
}
