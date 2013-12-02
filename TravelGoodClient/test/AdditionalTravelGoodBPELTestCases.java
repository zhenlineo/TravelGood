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
import ws.lameduck.FlightBookingList;
import ws.niceview.HotelBookingList;
import ws.travelgood.Itinerary;
import ws.travelgood.ItineraryFlightEntry;
import ws.travelgood.ItineraryHotelEntry;

/**
 *
 * @author zhenli
 */
public class AdditionalTravelGoodBPELTestCases {
    
    public AdditionalTravelGoodBPELTestCases() {
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
    // @Test
    // public void hello() {}
     @Test
     public void BookItineraryTest() {
     String clientId = "client5";
     String itineraryId = "itinerary5";
     Itinerary i = createItinerary(clientId, itineraryId);
     String itId = i.getId();
     System.out.println(itId);

     ws.niceview.HotelBookingList hotels = searchHotels(clientId, itineraryId, "Singapour", "03-12-2013", "12-12-2013");
     ws.lameduck.FlightBookingList flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
     //assertEquals(i1, i2);
     //System.out.println(flights.getCount());

     for (ws.lameduck.FlightBooking fb : flights.getFlightBookings()) {
     System.out.println(fb.getBookingNumber());
     boolean addSucc = addFlight(clientId, itineraryId, fb.getBookingNumber());
     assertTrue(addSucc);
     }

     for (ws.niceview.HotelBooking hb : hotels.getHotelBookings()) {
     System.out.println(hb.getBookingNumber());
     boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());
     //System.out.println("adding "+ hb.getBookingNumber());
     assertTrue(addsucc);
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
     //boolean cancelSuccess = cancelBooking(clientId, itineraryId, cc);


     i = getItinerary(clientId, itineraryId);
     List<ItineraryHotelEntry> bookings = i.getHotelBookings();
     System.out.println(i.getStatus());
     //System.out.println(i.getNumberOfHotels());

     for (ItineraryHotelEntry b : bookings) {
     System.out.println(b.getAhotelbooking().getBookingNumber() + " "
     + b.getStatus());
     }
     for (ItineraryFlightEntry f : i.getFlightBookings()) {
     System.out.println(f.getAflightbooking().getBookingNumber() + " " + f.getStatus());
     }


     assertFalse(ret);
     }

    @Test
     public void cancelBookings() {
     String clientId = "client6";
     String itineraryId = "itinerary6";
     Itinerary i = createItinerary(clientId, itineraryId);
     String itId = i.getId();
     System.out.println(itId);

     ws.niceview.HotelBookingList hotel = searchHotels(clientId, itineraryId, "Singapour", "03-12-2013", "12-12-2013");
     //assertEquals(i1, i2);
     ws.niceview.HotelBooking hb = hotel.getHotelBookings().get(0);
     boolean addsucc = addHotel(clientId, itineraryId, hb.getBookingNumber());

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

     boolean cancelSuccess = cancelBooking(clientId, itineraryId, cc);
     i = getItinerary(clientId, itineraryId);
     List<ItineraryHotelEntry> bookings = i.getHotelBookings();
     System.out.println(i.getStatus());
     //System.out.println(i.getNumberOfHotels());

     for (ItineraryHotelEntry b : bookings) {
     System.out.println(b.getAhotelbooking().getBookingNumber() + " "
     + b.getStatus());
     }

     assertTrue(cancelSuccess);

     }

    private static boolean addHotel(java.lang.String clientId, java.lang.String itineraryId, java.lang.String bookingNum) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addHotel(clientId, itineraryId, bookingNum);
    }

    private static boolean addFlight(java.lang.String clientId, java.lang.String itineraryId, java.lang.String bookingNum) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addFlight(clientId, itineraryId, bookingNum);
    }

    private static boolean bookingItinerary(java.lang.String clientId, java.lang.String itineraryId, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.bookingItinerary(clientId, itineraryId, creditcardInfo);
    }

    private static boolean cancelBooking(java.lang.String clientId, java.lang.String itineraryId, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.cancelBooking(clientId, itineraryId, creditcardInfo);
    }

    private static Itinerary createItinerary(java.lang.String clientId, java.lang.String itineraryId) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.createItinerary(clientId, itineraryId);
    }

    private static Itinerary getItinerary(java.lang.String clientId, java.lang.String itineraryId) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.getItinerary(clientId, itineraryId);
    }

    private static FlightBookingList searchFlights(java.lang.String clientId, java.lang.String itineraryId, java.lang.String from, java.lang.String to, java.lang.String date) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.searchFlights(clientId, itineraryId, from, to, date);
    }

    private static HotelBookingList searchHotels(java.lang.String clientId, java.lang.String itineraryId, java.lang.String city, java.lang.String arrivalDate, java.lang.String departureDate) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.searchHotels(clientId, itineraryId, city, arrivalDate, departureDate);
    }
    
    
}
