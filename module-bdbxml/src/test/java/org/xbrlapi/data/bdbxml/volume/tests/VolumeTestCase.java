package org.xbrlapi.data.bdbxml.volume.tests;


import junit.framework.TestCase;

/**
 * Tests focused upon assisting with tuning of the 
 * Oracle Berkeley Database Configuration.
 * These tests demonstrate the payoff to regular syncing
 * of the database container to ensure that there is not
 * too much work when it comes time to finish up.
 */
public abstract class VolumeTestCase extends TestCase {
    
    private final String CONTAINER = "container";
    private final String LOCATION = "/home/geoff/experiment";

    private ExperimentalStore store = null;

    public VolumeTestCase(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        store = new ExperimentalStore(LOCATION, CONTAINER);
    }

    @Override
    protected void tearDown() throws Exception {
        store.delete();
        store.deleteContainer();
    }

    public final void test1000AdditionsWithoutSyncing() {
        runTestWithoutSyncing(1000);
    }
    public final void test10000AdditionsWithoutSyncing() {
        runTestWithoutSyncing(10000);
    }
    public final void test50000AdditionsWithoutSyncing() {
        runTestWithoutSyncing(50000);
    }

    public final void test1000AdditionsWithSyncing() {
        runTestWithSyncing(1000);
    }
    public final void test10000AdditionsWithSyncing() {
        runTestWithSyncing(10000);
    }
    public final void test50000AdditionsWithSyncing() {
        runTestWithSyncing(50000);
    }

    

    private final void runTestWithoutSyncing(int iterations) {
        System.out.println("# iterations without syncing = " + iterations);        
        try {
            for (int i=1; i<=iterations; i++) {
                long start = System.currentTimeMillis();
                store.storeDocument();
                long end = System.currentTimeMillis();
                long diff = end - start;
                assertTrue(diff < 10000);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database insertions failed.");
        }
    }
    
    private final void runTestWithSyncing(int iterations) {
        System.out.println("# iterations with syncing = " + iterations);        
        try {
            for (int i=1; i<=iterations; i++) {
                long start = System.currentTimeMillis();
                store.storeDocument();
                long end = System.currentTimeMillis();
                long diff = end - start;
                assertTrue(diff < 10000);
                if (iterations % 1000 == 0) {
                    store.sync();
                }
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database insertions failed.");
        }
    }    

}

