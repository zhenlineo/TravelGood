/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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

    /*   @Test
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

     ws.niceview.HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "2013-12-03", "2013-12-12");


     for (ws.niceview.HotelBooking hb : hotel.getHotels()) {
     boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());
     //System.out.println("adding "+ hb.getBookingNumber());
     //assertTrue(addsucc);
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

     ws.niceview.HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "2013-12-03", "2013-12-12");
     //assertEquals(i1, i2);

     for (ws.niceview.HotelBooking hb : hotel.getHotels()) {
     boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());
     //System.out.println("adding "+ hb.getBookingNumber());
     assertTrue(addsucc);
     }
        

     //Itinerary i3 = getItinerary(clientId, itineraryId);
     //System.out.println(i3.getId());
     }*/
    @Test
    public void BookItineraryTest() {
        String clientId = "client5";
        String itineraryId = "itinerary5";
        Itinerary i = createItinerary(clientId, itineraryId);
        String itId = i.getId();
        System.out.println(itId);

        ws.niceview.HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "2013-12-03", "2013-12-12");
        //assertEquals(i1, i2);

        for (ws.niceview.HotelBooking hb : hotel.getHotels()) {
            boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());
            //System.out.println("adding "+ hb.getBookingNumber());
            assertTrue(addsucc);
            System.out.println(hb.getBookingNumber());
            //System.out.println(hb.getHotelService());
            //System.out.println(hb.getPrice());
        }
        ExpirationDateType ed = new ExpirationDateType();
        CreditCardInfoType cc = new CreditCardInfoType();
        cc.setExpirationDate(ed);
        //ed.setMonth(5);
        //ed.setYear(9);
        //cc.setName("Thor-Jensen Claus");
        //cc.setNumber("50408825");
        ed.setMonth(7);
        ed.setYear(9);
        cc.setName("Bech Camilla");
        cc.setNumber("50408822");
        boolean ret = bookingItinerary(clientId, itineraryId, cc);


        i = getItinerary(clientId, itineraryId);
        List<ItineraryHotelEntry> bookings = i.getHotelBookings();
        System.out.println(i.getStatus());
        //System.out.println(i.getNumberOfHotels());

        for (ItineraryHotelEntry b : bookings) {
            System.out.println(b.getHotelbookings().getBookingNumber() + " "
                    + b.getStatus());
        }
        /*for (int c = 0; c < i.getNumberOfHotels(); c ++) {
         ItineraryHotelEntry b = bookings.get(c);
         System.out.println(b.getHotelbookings().getBookingNumber() + " "
         + b.getStatus());
         }*/

        assertFalse(ret);
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

    private static ws.niceview.HotelBookingList searchHotels(java.lang.String clientId, java.lang.String itineraryId, java.lang.String city, java.lang.String arrivalDate, java.lang.String departureDate) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.searchHotels(clientId, itineraryId, city, arrivalDate, departureDate);
    }

    private static boolean bookingItinerary(java.lang.String clientId, java.lang.String itineraryId, CreditCardInfoType creditcardInfo) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.bookingItinerary(clientId, itineraryId, creditcardInfo);
    }
}
