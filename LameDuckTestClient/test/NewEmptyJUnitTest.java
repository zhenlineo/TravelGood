/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.lameduck.FlightBookingList;

/**
 *
 * @author zhenli
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() {
     FlightBookingList list = getFlights("Copenhagen", "Paris", "2013-12-10");
     assertEquals(list.getCount(), 2);
     }

    private static FlightBookingList getFlights(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        ws.lameduck.LameDuckService_Service service = new ws.lameduck.LameDuckService_Service();
        ws.lameduck.LameDuckService port = service.getLameDuckServicePort();
        return port.getFlights(arg0, arg1, arg2);
    }
}
