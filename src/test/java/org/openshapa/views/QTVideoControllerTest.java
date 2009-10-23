/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openshapa.views;

import junitx.util.PrivateAccessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openshapa.db.TimeStamp;
import org.openshapa.views.continuous.DataViewer;
import org.openshapa.views.QTVideoController.ShuttleDirection;

/**
 *
 */
public class QTVideoControllerTest {

    public QTVideoControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //--------------------------------------------------------------------------
    // tests
    //
    
    /**
     * Test of setCurrentLocation method, of class QTVideoController.
     */
////    @Test
//    public void testSetCurrentLocation() {
//        System.out.println("setCurrentLocation");
//        long milliseconds = 0L;
//        QTVideoController instance = null;
//        instance.setCurrentLocation(milliseconds);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCurrentLocation method, of class QTVideoController.
//     */
////    @Test
//    public void testGetCurrentLocation() {
//        System.out.println("getCurrentLocation");
//        QTVideoController instance = null;
//        TimeStamp expResult = null;
//        TimeStamp result = instance.getCurrentLocation();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of shutdown method, of class QTVideoController.
//     */
////    @Test
//    public void testShutdown() {
//        System.out.println("shutdown");
//        DataViewer viewer = null;
//        QTVideoController instance = null;
//        boolean expResult = false;
//        boolean result = instance.shutdown(viewer);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of syncCtrlAction method, of class QTVideoController.
//     */
////    @Test
//    public void testSyncCtrlAction() {
//        System.out.println("syncCtrlAction");
//        QTVideoController instance = null;
//        instance.syncCtrlAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of syncAction method, of class QTVideoController.
//     */
////    @Test
//    public void testSyncAction() {
//        System.out.println("syncAction");
//        QTVideoController instance = null;
//        instance.syncAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCellOnsetAction method, of class QTVideoController.
//     */
////    @Test
//    public void testSetCellOnsetAction() {
//        System.out.println("setCellOnsetAction");
//        QTVideoController instance = null;
//        instance.setCellOnsetAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCellOffsetAction method, of class QTVideoController.
//     */
////    @Test
//    public void testSetCellOffsetAction() {
//        System.out.println("setCellOffsetAction");
//        QTVideoController instance = null;
//        instance.setCellOffsetAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of playAction method, of class QTVideoController.
//     */
////    @Test
//    public void testPlayAction() {
//        System.out.println("playAction");
//        QTVideoController instance = null;
//        instance.playAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of forwardAction method, of class QTVideoController.
//     */
////    @Test
//    public void testForwardAction() {
//        System.out.println("forwardAction");
//        QTVideoController instance = null;
//        instance.forwardAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of rewindAction method, of class QTVideoController.
//     */
////    @Test
//    public void testRewindAction() {
//        System.out.println("rewindAction");
//        QTVideoController instance = null;
//        instance.rewindAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of pauseAction method, of class QTVideoController.
//     */
////    @Test
//    public void testPauseAction() {
//        System.out.println("pauseAction");
//        QTVideoController instance = null;
//        instance.pauseAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of stopAction method, of class QTVideoController.
//     */
////    @Test
//    public void testStopAction() {
//        System.out.println("stopAction");
//        QTVideoController instance = null;
//        instance.stopAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    @Test
    public void testShuttleInitialization() {
        System.out.println("shuttle funtionality");
        try {
            float[] srates = (float[]) PrivateAccessor.getField(
                    QTVideoController.class, "SHUTTLE_RATES"
                );

            assertEquals(11, srates.length);
            assertEquals(1f/32, srates[0]);
            assertEquals(1f, srates[5]);
            assertEquals(32f, srates[10] );

        } catch (Exception ex) {
            fail("Could not access private field");
        }
    }
    /**
     * Test of shuttleForwardAction method, of class QTVideoController.
     */
    @Test
    public void testShuttleForwardAction() {
        System.out.println("shuttleForwardAction");
        QTVideoController instance = new QTVideoController(null, true);

        try {
            assertEquals(
                    ShuttleDirection.UNDEFINED,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleForwardAction();
            assertEquals(
                    ShuttleDirection.FORWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleBackAction();
            assertEquals(
                    ShuttleDirection.BACKWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );


            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            assertEquals(
                    5,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            assertEquals(
                    10,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleForwardAction();
            assertEquals(
                    ShuttleDirection.FORWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    10,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleBackAction();
            assertEquals(
                    9,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

        } catch (Exception ex) {
            fail("Could not access private field");
        }
    }

    /**
     * Test of shuttleBackAction method, of class QTVideoController.
     */
    @Test
    public void testShuttleBackAction() {
        System.out.println("shuttleBackAction");
        QTVideoController instance = new QTVideoController(null, true);

        try {
            assertEquals(
                    ShuttleDirection.UNDEFINED,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleBackAction();
            assertEquals(
                    ShuttleDirection.BACKWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleForwardAction();
            assertEquals(
                    ShuttleDirection.FORWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    0,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            assertEquals(
                    4,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleForwardAction();
            instance.shuttleForwardAction();
            assertEquals(
                    2,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            assertEquals(
                    4,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            instance.shuttleBackAction();
            assertEquals(
                    9,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );
            instance.shuttleBackAction();
            assertEquals(
                    ShuttleDirection.BACKWARDS,
                    (ShuttleDirection) PrivateAccessor.getField(
                            instance, "shuttleDirection"
                        )
                );
            assertEquals(
                    10,
                    (Integer) PrivateAccessor.getField(instance, "shuttleRate")
                );

        } catch (Exception ex) {
            fail("Could not access private field");
        }
    }

//    /**
//     * Test of findAction method, of class QTVideoController.
//     */
////    @Test
//    public void testFindAction() {
//        System.out.println("findAction");
//        QTVideoController instance = null;
//        instance.findAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of goBackAction method, of class QTVideoController.
//     */
////    @Test
//    public void testGoBackAction() {
//        System.out.println("goBackAction");
//        QTVideoController instance = null;
//        instance.goBackAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of jogBackAction method, of class QTVideoController.
//     */
////    @Test
//    public void testJogBackAction() {
//        System.out.println("jogBackAction");
//        QTVideoController instance = null;
//        instance.jogBackAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of jogForwardAction method, of class QTVideoController.
//     */
////    @Test
//    public void testJogForwardAction() {
//        System.out.println("jogForwardAction");
//        QTVideoController instance = null;
//        instance.jogForwardAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createNewCellAction method, of class QTVideoController.
//     */
////    @Test
//    public void testCreateNewCellAction() {
//        System.out.println("createNewCellAction");
//        QTVideoController instance = null;
//        instance.createNewCellAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setNewCellStopTime method, of class QTVideoController.
//     */
////    @Test
//    public void testSetNewCellStopTime() {
//        System.out.println("setNewCellStopTime");
//        QTVideoController instance = null;
//        instance.setNewCellStopTime();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of syncVideoAction method, of class QTVideoController.
//     */
////    @Test
//    public void testSyncVideoAction() {
//        System.out.println("syncVideoAction");
//        QTVideoController instance = null;
//        instance.syncVideoAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}