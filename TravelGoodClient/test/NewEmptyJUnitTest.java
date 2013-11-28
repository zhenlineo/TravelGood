/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.Vector;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.travelgood.HotelBooking;
import ws.travelgood.HotelBookingList;
import ws.travelgood.Itinerary;
import ws.travelgood.ItineraryHotelEntry;

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
    public void getItineraryTest() {
        String clientId = "client2";
        String itineraryId = "itinerary2";
        Itinerary i = createItinerary(clientId, itineraryId);
        String itId = i.getId();
        System.out.println(itId);
        Itinerary i2 = getItinerary(clientId, itineraryId);
        //assertEquals(i1, i2);      
        System.out.println(i2.getId());

        Itinerary i3 = getItinerary(clientId, itineraryId);
        System.out.println(i3.getId());
    }

    @Test
    public void addHotelTest() {
        String clientId = "client3";
        String itineraryId = "itinerary3";
        Itinerary i = createItinerary(clientId, itineraryId);
        String itId = i.getId();
        System.out.println(itId);

        HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "2013-12-03", "2013-12-12");


        for (HotelBooking hb : hotel.getHotelBookingList()) {
            boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());
            //System.out.println("adding "+ hb.getBookingNumber());
            assertTrue(addsucc);
        }

        i = getItinerary(clientId, itineraryId);
        List<ItineraryHotelEntry> bookings = i.getHotelBookings();
        //System.out.println(bookings.size());
        //System.out.println(i.getNumberOfHotels());
        
        for (ItineraryHotelEntry b : bookings) {
            System.out.println(b.getHotelbookings().getBookingNumber() + " "
                    + b.getStatus());
        }
    }

    @Test
    public void searchHotelsTest() {
        String clientId = "client4";
        String itineraryId = "itinerary4";
        Itinerary i = createItinerary(clientId, itineraryId);
        String itId = i.getId();
        System.out.println(itId);

        HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "2013-12-03", "2013-12-12");
        //assertEquals(i1, i2);

        for (HotelBooking hb : hotel.getHotelBookingList()) {

            System.out.println(hb.getBookingNumber());
            System.out.println(hb.getHotelService());
            System.out.println(hb.getPrice());
        }

        //Itinerary i3 = getItinerary(clientId, itineraryId);
        //System.out.println(i3.getId());
    }

    private static ws.travelgood.Itinerary createItinerary(java.lang.String clientId, java.lang.String itineraryId) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.createItinerary(clientId, itineraryId);
    }

    private static ws.travelgood.Itinerary getItinerary(java.lang.String clientId, java.lang.String itineraryId) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.getItinerary(clientId, itineraryId);
    }

    private static boolean addHotel(java.lang.String clientId, java.lang.String itineraryId, java.lang.String bookingNum) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addHotel(clientId, itineraryId, bookingNum);
    }

    private static HotelBookingList searchHotels(java.lang.String clientId, java.lang.String itineraryId, java.lang.String city, java.lang.String arrivalDate, java.lang.String departureDate) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.searchHotels(clientId, itineraryId, city, arrivalDate, departureDate);
    }
}
