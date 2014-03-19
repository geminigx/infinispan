package org.infinispan.offheap;

import net.openhft.lang.model.constraints.MaxSize;
import org.infinispan.offheap.container.OffHeapDataContainer;
import org.infinispan.offheap.container.OffHeapDefaultDataContainer;
import org.infinispan.offheap.container.OffHeapInternalEntryFactoryImpl;
import org.infinispan.offheap.container.entries.*;
import org.infinispan.offheap.metadata.OffHeapEmbeddedMetadata;
import org.infinispan.offheap.metadata.OffHeapMetadata;
import org.infinispan.offheap.util.OffHeapCoreImmutables;
import org.infinispan.test.AbstractInfinispanTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;


/**
 * @author ben.cotton@jpmorgan.com
 * @author dmitry.gordeev@jpmorgan.com
 *
 * modeled from RedHat's original SimpleDataContainerTest.java
 */


@Test(groups = "unit", testName = "offheap.OffHeapDataContainerTest")
public class OffHeapDefaultDataContainerTest extends AbstractInfinispanTest {
    OffHeapDataContainer dc;


    @BeforeMethod
    public void setUp() throws InterruptedException {

        Thread.sleep(2000);
        System.out.println("ISPN7 JCACHE DataContainer view of OpenHFT SHM is being created");
        this.dc = createContainer();
        Thread.sleep(2000);
        System.out.println("ISPN7 JCACHE DataContainer created dc=["+dc.toString()+"]");
        Thread.sleep(2000);
    }

    @AfterMethod
    public void tearDown() {
        this.dc = null;
    }



    protected OffHeapDataContainer createContainer() {
        this.dc = new OffHeapDefaultDataContainer(
                           String.class,
                           BondVOInterface.class,
                           "BondVoOperand",
                           512,
                           256
                        );
        OffHeapInternalEntryFactoryImpl internalEntryFactory = new OffHeapInternalEntryFactoryImpl();
        internalEntryFactory.injectTimeService(TIME_SERVICE);
        dc.initialize(null, null, internalEntryFactory, null, null, TIME_SERVICE);
        return dc;
    }


    public void testOpenHFTasOffHeapOperandProvider() throws InterruptedException {
        //TODO: build a join to OpenHFT MetaData
        BondVOInterface bondV = new BondVOInterface() {
            public OffHeapMetadata _metaData;
            public boolean _doUndelete;
            public boolean _skipLookup;
            public boolean _valid;
            public boolean _evicted;
            public boolean _removed;
            public boolean _loaded;
            public OffHeapInternalCacheValue toInternalCacheValue;
            public String _symbol = "CUSIP1234";
            public long _expiryTime;
            public double _coupon;
            public boolean _created;
            public long _maturityDate;
            public long _issueDate;

            @Override public void busyLockEntry() throws InterruptedException {}
            @Override public void unlockEntry() { /* ben? */ }

            @Override public long getIssueDate() { return this._issueDate; }
            @Override public void setIssueDate(long issueDate) { this._issueDate = issueDate; }

            @Override public long getMaturityDate() { return this._maturityDate;}
            @Override public void setMaturityDate(long maturityDate) {this._maturityDate = maturityDate;}
            @Override public long addAtomicMaturityDate(long toAdd) { return 0;}

            @Override public double getCoupon() { return this._coupon; }
            @Override public void setCoupon(double coupon) {this._coupon = coupon;}
            @Override public double addAtomicCoupon(double toAdd) { /* ben? */return 0; }

            @Override public void setSymbol(@MaxSize(20) String symbol) {this._symbol=symbol;}
            @Override public String getSymbol() {return this._symbol; }

            @Override public void setMarketPxIntraDayHistoryAt(@MaxSize(7) int tradingDayHour, MarketPx mPx) { }
            @Override public MarketPx getMarketPxIntraDayHistoryAt(int tradingDayHour) {return null; }

            @Override public boolean isExpired(long now) {return false;}
            @Override public boolean isExpired() { return false;}
            @Override public boolean canExpire() {return false; }

            @Override public long getCreated() {return 0;}
            @Override public long getLastUsed() {return 0;}
            @Override public long getExpiryTime() {return this._expiryTime;}

            @Override public void touch() { /* ben ?? */}
            @Override public void touch(long currentTimeMillis) { /* ben ?? */}
            @Override public void reincarnate() { /* ben ?? */}
            @Override public void reincarnate(long now) { /* ben ?? */ }
            @Override public OffHeapInternalCacheValue toInternalCacheValue() { return this.toInternalCacheValue; }

            @Override
            public OffHeapInternalCacheEntry clone() {
                try {
                    return (OffHeapInternalCacheEntry) super.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override public boolean isNull() {return false;}
            @Override public boolean isChanged() { return false;}
            @Override public boolean isCreated() {return false; }
            @Override public boolean isRemoved() {return false;}
            @Override public boolean isEvicted() {return false;}
            @Override public boolean isValid() {return false;}
            @Override public boolean isLoaded() {return false; }
            @Override public Object getKey() { return null;}
            @Override public Object getValue() {return null;}
            @Override public long getLifespan() {return 0;}
            @Override public long getMaxIdle() {return 0; }
            @Override public boolean skipLookup() {return false; }
            @Override public Object setValue(Object value) { return null;}
            @Override public void commit(OffHeapDataContainer container, OffHeapMetadata metadata) { }
            @Override public void rollback() { }
            @Override public void setChanged(boolean changed) { }
            @Override public void setCreated(boolean created) { this._created = created; }
            @Override public void setRemoved(boolean removed) { this._removed = removed; }
            @Override public void setEvicted(boolean evicted) { this._evicted = evicted;}
            @Override public void setValid(boolean valid) { this._valid = valid; }
            @Override public void setLoaded(boolean loaded) { this._loaded = loaded; }
            @Override public void setSkipLookup(boolean skipLookup) { this._skipLookup = skipLookup; }
            @Override public boolean undelete(boolean doUndelete) {return this._doUndelete; }

            @Override public OffHeapMetadata getMetadata() { return this._metaData;  }
            @Override public void setMetadata(OffHeapMetadata metadata) {  this._metaData = metadata; }
        };

        Thread.sleep(2000);
        System.out.println("Using ISPN 7 JCACHE to put() BondVOInterface (IBMHY2044) --> DataContainer (bondV=["+
                bondV+
                "])");

        Thread.sleep(2000);
        this.dc.put("IBMHY2044",bondV,null); //NPE
        this.dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        Thread.sleep(2000);
        System.out.println("ISPN7 JCACHE put() the BondVOInterface (IBMHY2044) into DataContainer bondV=["+bondV+"]");

        Thread.sleep(2000);
        System.out.println("Using ISPN7 JCACHE to get(IBMHT2044) BondVOInterface <--  DataContainer (bondV=["+bondV+"])");

        Thread.sleep(2000);
        OffHeapInternalCacheEntry bondEntry = dc.get("IBMHY2044");

        Thread.sleep(2000);
        System.out.println("ISPN7 JCACHE got the (IBMHT2044) BondVOInterface from  DataContainer (entry.getSymbol()=["+
                ((BondVOInterface) bondEntry).getSymbol() +
                "])");

        Thread.sleep(2000);
        assert bondEntry.getClass().equals(transienttype());
        assert bondEntry.getLastUsed() <= System.currentTimeMillis();
        long entryLastUsed = bondEntry.getLastUsed();
        Thread.sleep(2000);
        bondEntry = dc.get("IBMHY2044");
        assert bondEntry.getLastUsed() > entryLastUsed;
        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(0, TimeUnit.MINUTES).build());
        dc.purgeExpired();

        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        Thread.sleep(2000);
        assert dc.size() == 1;

        bondEntry= dc.get("IBMHY2044");
        assert bondEntry != null : "Entry should not be null!";
        assert bondEntry.getClass().equals(mortaltype()) : "Expected "+mortaltype()+", was " + bondEntry.getClass().getSimpleName();
        assert bondEntry.getCreated() <= System.currentTimeMillis();

        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(0, TimeUnit.MINUTES).build());
        Thread.sleep(10);
        assert dc.get("k") == null;
        assert dc.size() == 0;

        dc.put("IBMHY2044", "v", new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(0, TimeUnit.MINUTES).build());
        Thread.sleep(100);
        assert dc.size() == 1;
        dc.purgeExpired();
        assert dc.size() == 0;
    }


    public void testResetOfCreationTime() throws Exception {
        long now = System.currentTimeMillis();
        BondVOInterface bondV = null;
        if (1 == 1) return;
        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(1000, TimeUnit.SECONDS).build());
        long created1 = dc.get("k").getCreated();
        assert created1 >= now;
        Thread.sleep(100);
        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(1000, TimeUnit.SECONDS).build());
        long created2 = dc.get("k").getCreated();
        assert created2 > created1 : "Expected " + created2 + " to be greater than " + created1;
    }


    public void testUpdatingLastUsed() throws Exception {
        long idle = 600000;
        BondVOInterface bondV = null;
        if (1 == 1) return; 
        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        OffHeapInternalCacheEntry ice = dc.get("k");
        assert ice.getClass().equals(immortaltype());
        assert ice.toInternalCacheValue().getExpiryTime() == -1;
        assert ice.getMaxIdle() == -1;
        assert ice.getLifespan() == -1;
        dc.put("IBMHY2044", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(idle, TimeUnit.MILLISECONDS).build());
        long oldTime = System.currentTimeMillis();
        Thread.sleep(100); // for time calc granularity
        ice = dc.get("IBMHY2044");
        assert ice.getClass().equals(transienttype());
        assert ice.toInternalCacheValue().getExpiryTime() > -1;
        assert ice.getLastUsed() > oldTime;
        Thread.sleep(100); // for time calc granularity
        assert ice.getLastUsed() < System.currentTimeMillis();
        assert ice.getMaxIdle() == idle;
        assert ice.getLifespan() == -1;

        oldTime = System.currentTimeMillis();
        Thread.sleep(100); // for time calc granularity
        assert dc.get("IBMHY2044") != null;

        // check that the last used stamp has been updated on a get
        assert ice.getLastUsed() > oldTime;
        Thread.sleep(100); // for time calc granularity
        assert ice.getLastUsed() < System.currentTimeMillis();
    }

    protected Class<? extends OffHeapInternalCacheEntry> mortaltype() {
        return OffHeapMortalCacheEntry.class;
    }

    protected Class<? extends OffHeapInternalCacheEntry> immortaltype() {
        return OffHeapImmortalCacheEntry.class;
    }

    protected Class<? extends OffHeapInternalCacheEntry> transienttype() {
        return OffHeapTransientCacheEntry.class;
    }

    protected Class<? extends OffHeapInternalCacheEntry> transientmortaltype() {
        return OffHeapTransientMortalCacheEntry.class;
    }


    public void testExpirableToImmortalAndBack() {
        if (1 == 1) return;

        String value = "v";
        dc.put("IBMHY2044", value, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        assertContainerEntry(this.mortaltype(), value);

        value = "v2";
        dc.put("k", value, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        assertContainerEntry(this.immortaltype(), value);

        value = "v3";
        dc.put("k", value, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        assertContainerEntry(this.transienttype(), value);

        value = "v4";
        dc.put("k", value, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .lifespan(100, TimeUnit.MINUTES).maxIdle(100, TimeUnit.MINUTES).build());
        assertContainerEntry(this.transientmortaltype(), value);

        value = "v41";
        dc.put("k", value, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .lifespan(100, TimeUnit.MINUTES).maxIdle(100, TimeUnit.MINUTES).build());
        assertContainerEntry(this.transientmortaltype(), value);

        value = "v5";
        dc.put("k", value, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        assertContainerEntry(this.mortaltype(), value);
    }


    private void assertContainerEntry(
                                        Class<? extends OffHeapInternalCacheEntry> type,
                                        String expectedValue
                                    ) {
        assert dc.containsKey("IBMHY2044");
        OffHeapInternalCacheEntry entry = dc.get("IBMHY2044");
        assertEquals(type, entry.getClass());
        assertEquals(expectedValue, entry.getValue());
    }


    public void testKeySet() {
        if (1 == 1) return;
        BondVOInterface bondV = null;
        dc.put("k1", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        dc.put("k2", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        dc.put("k3", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        dc.put("k4", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .maxIdle(100, TimeUnit.MINUTES)
                .lifespan(100, TimeUnit.MINUTES)
                .build());

        Set<String> expected = new HashSet<String>();
        expected.add("k1");
        expected.add("k2");
        expected.add("k3");
        expected.add("k4");

        for (Object o : dc.keySet()) {
            assert expected.remove(o);
        }

        assert expected.isEmpty() : "Did not see keys " + expected + " in iterator!";
    }


    public void testContainerIteration() {

        BondVOInterface bondV = null;
        if (1 == 1) return;

        bondV.setMaturityDate(20440315L);
        bondV.setCoupon(5.0/100.0);
        bondV.setSymbol("IBM_HY_2044");
        System.out.println("bondV.getSymbol=["+bondV.getSymbol()+"]");
        dc.put("CUSIP1234", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        dc.put("CUSIP4321", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        dc.put("1234CUSIP", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        dc.put("4321CUSIP", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                                    .maxIdle(100, TimeUnit.MINUTES)
                                    .lifespan(100, TimeUnit.MINUTES)
                                    .build());

        Set<String> expected = new HashSet<String>();
        expected.add("k1");
        expected.add("k2");
        expected.add("k3");
        expected.add("k4");

        for (OffHeapInternalCacheEntry ice : dc) {
            assert expected.remove(ice.getKey());
        }

        assert expected.isEmpty() : "Did not see keys " + expected + " in iterator!";
    }


    public void testKeys() {
        BondVOInterface bondV = null;
        if (1 == 1) return;


        dc.put("k1", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        dc.put("k2", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        dc.put("k3", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        dc.put("k4", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .maxIdle(100, TimeUnit.MINUTES).lifespan(100, TimeUnit.MINUTES).build());

        Set<String> expected = new HashSet<String>();
        expected.add("k1");
        expected.add("k2");
        expected.add("k3");
        expected.add("k4");

        for (Object o : dc.keySet()) assert expected.remove(o);

        assert expected.isEmpty() : "Did not see keys " + expected + " in iterator!";
    }


    public void testValues() {
        BondVOInterface bondV = null;
        if (1 == 1) return;


        dc.put("k1", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        dc.put("k2", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        dc.put("k3", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        dc.put("k4", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .maxIdle(100, TimeUnit.MINUTES).lifespan(100, TimeUnit.MINUTES).build());

        Set<String> expected = new HashSet<String>();
        expected.add("v1");
        expected.add("v2");
        expected.add("v3");
        expected.add("v4");

        for (Object o : dc.values()) assert expected.remove(o);

        assert expected.isEmpty() : "Did not see keys " + expected + " in iterator!";
    }


    public void testEntrySet() {
        BondVOInterface bondV = null;
        if (1 == 1) return;


        dc.put("k1", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().lifespan(100, TimeUnit.MINUTES).build());
        dc.put("k2", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());
        dc.put("k3", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().maxIdle(100, TimeUnit.MINUTES).build());
        dc.put("k4", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder()
                .maxIdle(100, TimeUnit.MINUTES).lifespan(100, TimeUnit.MINUTES).build());

        Set<OffHeapInternalCacheEntry> expected = new HashSet<OffHeapInternalCacheEntry>();
        expected.add(OffHeapCoreImmutables.immutableInternalCacheEntry(dc.get("k1")));
        expected.add(OffHeapCoreImmutables.immutableInternalCacheEntry(dc.get("k2")));
        expected.add(OffHeapCoreImmutables.immutableInternalCacheEntry(dc.get("k3")));
        expected.add(OffHeapCoreImmutables.immutableInternalCacheEntry(dc.get("k4")));

        Set<Map.Entry<Object,Object>> actual = new HashSet<Map.Entry<Object, Object>>();
        for (Map.Entry<Object, Object> o : dc.entrySet()) actual.add(o);

        assert actual.equals(expected) : "Expected to see keys " + expected + " but only saw " + actual;
    }


    public void testGetDuringKeySetLoop() {
        BondVOInterface bondV = null;
        if (1 == 1) return;


        for (int i = 0; i < 10; i++) dc.put(i+"", bondV, new OffHeapEmbeddedMetadata.OffHeapBuilder().build());

        int i = 0;
        for (Object key : dc.keySet()) {
            dc.peek(key); // calling get in this situations will result on corruption the iteration.
            i++;
        }

        assert i == 10 : "Expected the loop to run 10 times, only ran " + i;
    }
}
