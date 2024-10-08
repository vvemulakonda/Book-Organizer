/**
 * A simple implementation of a binary search tree-type map.
 * This class provides methods to insert, retrieve, and manage key/value pairs in a sorted order based on the keys.
 * The keys in the map must implement the Comparable interface to ensure they can be compared and ordered.
 *
 * @param <K> the type of keys maintained by this map; must extend {@link Comparable}.
 * @param <V> the type of values maintained by this map.
 *
 * @author Vivek Vemulakonda
 * @version 1.0
 */
public class TreeMap<K extends Comparable<K>, V> implements TreeMapInterface<K, V> {

    private int size;
    private TmNode root;

    /**
     * A node in the binary search tree.
     * Contains a key-value pair and references to left and right child nodes.
     */
    private class TmNode {
        K key;
        V value;
        TmNode left;
        TmNode right;

        /**
         * Constructs a new node with the specified key and value.
         *
         * @param key the key of the node.
         * @param value the value associated with the key.
         */
        TmNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * Constructs an empty TreeMap.
     */
    public TreeMap() {
        root = null;
        size = 0;
    }

    /**
     * Retrieves the number of key/value pair elements managed by the map.
     *
     * @return the number of elements in the map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Clears the existing tree, removing any and all existing key/value pairs.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Retrieves the value corresponding to the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value corresponding to the specified key, or {@code null} if the key is not found.
     * @throws IllegalArgumentException if the key is {@code null}.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        TmNode node = get(root, key);
        return node != null ? node.value : null;
    }

    private TmNode get(TmNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    /**
     * Adds a key/value pair to the tree map.
     *
     * @param key the key in the key/value pair; used to organize the tree.
     * @param value the value in the key/value pair; this data is looked up through key-based searches.
     * @throws IllegalArgumentException if the key is {@code null}.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        root = put(root, key, value);
    }

    private TmNode put(TmNode node, K key, V value) {
        if (node == null) {
            size++;
            return new TmNode(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    /**
     * Checks if the tree contains the specified key.
     *
     * @param key the key to search for.
     * @return {@code true} if the key is in the tree map; {@code false} otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    /**
     * Retrieves an array of key data from the map, in order.
     *
     * @param array the array to fill in. If smaller than the map's size, a new array will be created. If larger than the
     *              map's size, data will be filled in from index 0, with a {@code null} reference just after the copied-in data.
     *              This parameter must not be {@code null}.
     * @return a reference to the filled-in array; may be a different array than the one passed in.
     * @throws IllegalArgumentException if the array is {@code null}.
     */
    @Override
    public K[] toKeyArray(K[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null.");
        }
        if (array.length < size) {
            array = (K[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(),
                    size);
        }
        fillKeys(root, array, 0);
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }

    private int fillKeys(TmNode node, K[] array, int index) {
        if (node == null) return index;
        index = fillKeys(node.left, array, index);
        array[index++] = node.key;
        index = fillKeys(node.right, array, index);
        return index;
    }

    /**
     * Retrieves an array of value data from the map, in key order.
     *
     * @param array the array to fill in. If smaller than the map's size, a new array will be created. If larger than the
     *              map's size, data will be filled in from index 0, with a {@code null} reference just after the copied-in data.
     *              This parameter must not be {@code null}.
     * @return a reference to the filled-in array; may be a different array than the one passed in.
     * @throws IllegalArgumentException if the array is {@code null}.
     */
    @Override
    public V[] toValueArray(V[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null.");
        }
        if (array.length < size) {
            array = (V[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(),
                    size);
        }
        fillValues(root, array, 0);
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }

    private int fillValues(TmNode node, V[] array, int index) {
        if (node == null) return index;
        index = fillValues(node.left, array, index);
        array[index++] = node.value;
        index = fillValues(node.right, array, index);
        return index;
    }
}
