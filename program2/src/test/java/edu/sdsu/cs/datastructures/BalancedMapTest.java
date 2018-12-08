/**
 * Instructor Provided Tests
 * CS310: Data Structures
 * San Diego State University
 */

package edu.sdsu.cs.datastructures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class BalancedMapTest {

    private static final int DEFAULT_TEST_SIZE = 100000;
    private static final int DEFAULT_START_KEY = 0;
    private static final int DEFAULT_CHECK = -1337;
    private static final int DEFAULT_IGNORE = -310;


    private IMap<String, Long> sut;

    private static List<Long> getBalancedKeySequence(long start,
                                                     long stop) {

        List<Long> toReturn = new LinkedList<>();

        if (start > stop) return toReturn;

        long mid = ((start + stop) / 2);
        toReturn.add(mid);
        toReturn.addAll(getBalancedKeySequence(start, mid - 1));
        toReturn.addAll(getBalancedKeySequence(mid + 1, stop));

        return toReturn;
    }

    private static String getFormattedKey(long unformattedKey) {
        return String.format("[%10s]", unformattedKey);
    }

    private static IMap<String, Long> addInOrderSequence(IMap<String,
            Long> target, int testSize, int startIntKey) {

        for (long curKeyNum = startIntKey; curKeyNum < testSize + startIntKey;
             curKeyNum++) {
            target.add(getFormattedKey(curKeyNum), curKeyNum);
        }

        return target;
    }

    private void checkInitializedCorrectly() {

        assertTrue("Expected: isEmpty() true", sut.isEmpty());
        assertEquals(String.format("Expected: size() == 0. Found [%d]", sut
                .size()), 0, sut.size());

        try {
            for (String key : sut.keyset())
                fail(String.format("Expected no keys. Found [%s]", key));
        } catch (NullPointerException e) {
            fail("Keyset iterator returns null. Iterable expected.");
            e.printStackTrace();
        }
        try {
            for (Long value : sut.values())
                fail(String.format("Expected no values. Found [%d]", value));
        } catch (NullPointerException e) {
            fail("Values iterator returns null. Iterable expected.");
        }

    }

    @Test
    public void getKey_notPresent_returnsNull() {

        sut = addInOrderSequence(sut, DEFAULT_TEST_SIZE, DEFAULT_START_KEY);

        String foundKey = sut.getKey((long) DEFAULT_TEST_SIZE);

        assertNull(foundKey);
    }

    @Test
    public void getKey_returnsCorrectKey() {

        int testSize = DEFAULT_TEST_SIZE >> 4;

        sut = addInOrderSequence(sut, testSize, DEFAULT_START_KEY);

        for (int curVal = 0; curVal < testSize; curVal++) {
            final String expectedKey = getFormattedKey(curVal);
            assertEquals(expectedKey, sut.getKey((long) curVal));
        }

    }

    @Test
    public void getKey_emptyStructure_returnsNull() {

        final long target = 310;

        String foundKey = sut.getKey(target);

        assertNull(foundKey);
    }

    @Test
    public void getKeys_allFound() {
        // setup the test
        List<String> testedKeys = new java.util.LinkedList<>();
        sut = addInOrderSequence(sut, DEFAULT_TEST_SIZE, DEFAULT_START_KEY);
        for (int i = 1; i <= 10; i++) {
            String keyToAdd = getFormattedKey(10 * i);
            sut.delete(keyToAdd);
            sut.add(keyToAdd, (long) DEFAULT_CHECK);
            testedKeys.add(keyToAdd);
        }

        // perform the action
        Iterable<String> found = sut.getKeys((long) DEFAULT_CHECK);

        // evaluate the results
        for (String key : found) {
            assertTrue(testedKeys.contains(key));
            testedKeys.remove(testedKeys.indexOf(key));
        }
        assertTrue(testedKeys.isEmpty());
    }

    @Test
    public void keyset_correctOrder() {
        // build the test
        List<Long> keySequence = getBalancedKeySequence(0, DEFAULT_TEST_SIZE);
        for (Long keyValue : keySequence)
            sut.add(getFormattedKey(keyValue), (long) DEFAULT_IGNORE);

        // perform the action
        Iterable<String> keys = sut.keyset();

        // verify correct order
        String lastKey = null;
        for (String key : keys) {
            if (lastKey != null) {
                int result = key.compareTo(lastKey);
                assertTrue(result >= 0);
            }
            lastKey = key;
        }
    }

    @Test
    public void keysetAndValues_orderMatches() {

        for (Long key : getBalancedKeySequence(DEFAULT_START_KEY,
                DEFAULT_TEST_SIZE)) {
            sut.add(getFormattedKey(key), key);
        }

        Iterator<String> keys = sut.keyset().iterator();
        Iterator<Long> values = sut.values().iterator();

        try {
            while (keys.hasNext()) {
                Long value = values.next();
                String key = keys.next();

                assertEquals(getFormattedKey(value), key);
            }
        } catch (Exception e) {
            fail("Key and Value iterators should have identical number of " +
                    "entries.");
        }
    }

    @Before
    public void setUp() {
        sut = new BalancedMap<>();
    }

    @Test
    public void defaultConstructor_initCorrectly() {
        checkInitializedCorrectly();
    }

    @Test
    public void mapConstructor_initCorrectly() {
        IMap<String, Long> original = addInOrderSequence(new BalancedMap<String,
                Long>(), DEFAULT_TEST_SIZE, 0);

        sut = new BalancedMap<>(original);

        assertEquals(DEFAULT_TEST_SIZE, sut.size());
    }

    @Test
    public void add_duplicateKeys_unchanged() {

        // build the test data
        sut = addInOrderSequence(sut, DEFAULT_TEST_SIZE, DEFAULT_START_KEY);

        // perform the action we're trying to test (adding duplicates)
        sut = addInOrderSequence(sut, DEFAULT_TEST_SIZE, DEFAULT_START_KEY);

        // verify it meets our expectations (correct contents)
        for (int testKey = DEFAULT_START_KEY; testKey < DEFAULT_START_KEY +
                DEFAULT_TEST_SIZE; testKey++) {
            String curKey = getFormattedKey(testKey);
            assertEquals(testKey, (long) sut.getValue(curKey));
        }
        // verify it meets our expectations (correct size)
        assertEquals(DEFAULT_TEST_SIZE, sut.size());
    }

    @Test
    public void add_largeBalancedSequence_allPresent() {
        List<Long> keysToUse = getBalancedKeySequence(0, DEFAULT_TEST_SIZE);

        for (Long key : keysToUse)
            sut.add(getFormattedKey(key), key);

        for (Long key : keysToUse) {
            assertTrue(sut.contains(getFormattedKey(key)));
            assertEquals(key, sut.getValue(getFormattedKey(key)));
        }
    }

    @Test
    public void delete_emptyStructure_nullReturned() {
        Long oldValue = sut.delete(Integer.toString(DEFAULT_IGNORE));
        assertNull(oldValue);
    }


    @Test
    public void delete_zeroChildRoot_initCorrectly() {

        // build the test
        sut.add("ROOT", (long) DEFAULT_CHECK);

        // perform the action
        sut.delete("ROOT");

        // verify the structure
        checkInitializedCorrectly();
    }

    @Test
    public void delete_oddKeys_contentsCorrect() {

        final int testSize = DEFAULT_TEST_SIZE;

        // build the test data
        sut = addInOrderSequence(sut, testSize, 0);

        // perform the action (delte odds)
        for (int keyCounter = 1; keyCounter < testSize; keyCounter += 2)
            sut.delete(getFormattedKey(keyCounter));

        // verify the even key is present and the odd key is absent
        for (int keyCounter = 0; keyCounter < testSize; keyCounter += 2) {
            // even item holds valid value
            assertEquals((long) keyCounter, (long) sut.getValue(getFormattedKey
                    (keyCounter)));
            // odd value not present
            assertFalse(sut.contains(getFormattedKey(keyCounter + 1)));
        }
    }

    @Test
    public void clear_initCorrectly() {
        sut = addInOrderSequence(sut, DEFAULT_TEST_SIZE, DEFAULT_START_KEY);
        sut.clear();
        checkInitializedCorrectly();
    }

    @Test
    public void contains_identicalValuesDifferentObjects_found() {

        // build the data
        IMap<TestObject, Integer> underTest = new BalancedMap<>();
        for (int curTestVal = 0; curTestVal < DEFAULT_TEST_SIZE; curTestVal++) {
            underTest.add(new TestObject(curTestVal), curTestVal);
        }

        // perform the action (verifying all keys in the map)
        List<Boolean> results = new ArrayList<>();
        for (int curTestVal = 0; curTestVal < DEFAULT_TEST_SIZE; curTestVal++) {
            results.add(underTest.contains(new TestObject(curTestVal)));
        }

        // check the results
        assertFalse("Failed to find a valid Comparable equal. Make " +
                "certain the data structure uses the .compareTo method and " +
                "NOT .equals() when checking keys", results.contains(false));
    }

    private class TestObject implements Comparable<TestObject> {

        final int savedValue;

        TestObject(int value) {
            savedValue = value;
        }

        @Override
        public int compareTo(TestObject testObject) {
            return savedValue - testObject.savedValue;
        }
    }

}