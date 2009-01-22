package au.com.nicta.openshapa.db;

import au.com.nicta.openshapa.db.FormalArgument.fArgType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cfreeman
 */
public abstract class DataValueTest extends DBElementTest {

    public abstract DataValue getInstance();

    public DataValueTest() {
    }

    /**
     * Test of updateForFargChange method, of class DataValue.
     */
    @Test
    public abstract void testUpdateForFargChange() throws Exception;

    /**
     * Test of updateSubRange method, of class DataValue.
     */
    @Test
    public abstract void testUpdateSubRange() throws Exception;

    /**
     * Test of getItsFargID method, of class DataValue.
     */
    @Test
    public void testGetItsFargID() {
        DataValue instance = getInstance();
        assertEquals(instance.getItsFargID(), 0);
    }

    /**
     * Test of getItsFargType method, of class DataValue.
     */
    @Test
    public void testGetItsFargType() {
        DataValue instance = getInstance();
        assertEquals(instance.getItsFargType(), fArgType.UNDEFINED);
    }

    /**
     * Test of getSubRange method, of class DataValue.
     */
    @Test
    public void testGetSubRange() {
        DataValue instance = getInstance();
        assertFalse(instance.getSubRange());
    }

    /**
     * Test of setItsCellID method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testSetItsCellID() throws Exception {
        DataValue instance = getInstance();
        final long ID = 5;
        instance.setItsCellID(ID);
    }

    /**
     * Test of setItsFargID method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testSetItsFargID() throws Exception {
        DataValue instance = getInstance();
        instance.setItsFargID(DBIndex.INVALID_ID);
    }

    /**
     * Test of setItsPredID method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testSetItsPredID() throws Exception {
        DataValue instance = getInstance();
        instance.setItsPredID(DBIndex.INVALID_ID);
    }

    /**
     * Test of insertInIndex method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testInsertInIndex() throws Exception {
        DataValue instance = getInstance();
        final long ID = 5;
        instance.insertInIndex(ID);
    }

    /**
     * Test of removeFromIndex method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testRemoveFromIndex() throws Exception {
        DataValue instance = getInstance();
        final long ID = 5;
        instance.removeFromIndex(ID);
    }

    /**
     * Test of replaceInIndex method, of class DataValue.
     */
    @Test (expected = SystemErrorException.class)
    public void testReplaceInIndex() throws Exception {
        DataValue original = getInstance();
        original.replaceInIndex(original, 5, false, false, 5, false, false, 5);
    }

    /**
     * Verify that the supplied instance of DataValue has been correctly
     * initialized by a one argument constructor.
     *
     * @param db Database
     * @param dv DataValue
     */
    static void verify1ArgInitialization(final Database db,
                                                final DataValue dv) {
        assertNotNull(db);
        assertNotNull(dv);
        assertEquals(dv.getDB(), db);
        assertEquals(dv.getDB(), db);
        assertEquals(dv.getID(), DBIndex.INVALID_ID);
        assertEquals(dv.itsCellID, DBIndex.INVALID_ID);
        assertEquals(dv.itsFargID, DBIndex.INVALID_ID);
        assertEquals(dv.itsFargType, FormalArgument.fArgType.UNDEFINED);
        assertEquals(dv.getLastModUID(), DBIndex.INVALID_ID);
        assertFalse(dv.subRange);
    }

}