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
import ws.travelgood.BookingStatus;
import ws.travelgood.Itinerary;
import ws.travelgood.ItineraryFlightEntry;
import ws.travelgood.ItineraryHotelEntry;

/**
 *
 * @author zhenli
 */
public class TravelGoodBPELTestCases {

    public TravelGoodBPELTestCases() {
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

    // Test 1
    /*
     * 1. search & pick flight
     * 2. search & pick hotel
     * 3. 1.
     * 4. 1.
     * 5. 2
     * Check that items are unfirmed.
     * Book the itinerary and check statuses are confirmed.
     */
    @Test
    public void test_P1() {
        String clientId = "client";
        String itineraryId = "itinerary";
        Itinerary i = createItinerary(clientId, itineraryId);


        // 1. pick flights and hotels

        // pick first (and only)
        ws.lameduck.FlightBookingList flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
        ws.lameduck.FlightBooking fb = flights.getFlightBookings().get(0);
        System.out.println(fb.getBookingNumber());
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        ws.niceview.HotelBookingList hotels = searchHotels(clientId, itineraryId, "Singapour", "02-12-2013", "12-12-2013");
        ws.niceview.HotelBooking hb = hotels.getHotelBookings().get(0);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        flights = this.searchFlights(clientId, itineraryId, "Paris", "Copenhagen", "30-10-2013");
        fb = flights.getFlightBookings().get(0);
        System.out.println(fb.getBookingNumber());
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        flights = this.searchFlights(clientId, itineraryId, "Madrid", "Athens", "31-10-2013");
        fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        hotels = this.searchHotels(clientId, itineraryId, "Singapour", "16-12-2013", "17-12-2013");
        hb = hotels.getHotelBookings().get(1);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        // 2. check statuses of bookings

        i = this.getItinerary(clientId, itineraryId);

        List<ItineraryHotelEntry> bookings = i.getHotelBookings();

        assertEquals(i.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);

        for (ItineraryHotelEntry b : bookings) {
            assertEquals(b.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);
        }
        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);
        }

        // 3. confirm bookings

        CreditCardInfoType cc = this.generateCardEnoughMoney();
        this.bookingItinerary(clientId, itineraryId, cc);

        // 4. check statuses of bookings

        i = this.getItinerary(clientId, itineraryId);

        bookings = i.getHotelBookings();

        assertEquals(i.getStatus(), ws.travelgood.BookingStatus.CONFIRMED);

        for (ItineraryHotelEntry b : bookings) {
            assertEquals(b.getStatus(), ws.travelgood.BookingStatus.CONFIRMED);
        }
        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), ws.travelgood.BookingStatus.CONFIRMED);
        }

    }

    /*
     * Plan a trip and cancel it
     */
    @Test
    public void test_P2() {
        String clientId = "client";
        String itineraryId = "itinerary2";
        Itinerary i = createItinerary(clientId, itineraryId);

        // 1. pick a flight
        ws.lameduck.FlightBookingList flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
        ws.lameduck.FlightBooking fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        // 2. cancel plan
        CreditCardInfoType cc = generateCardEnoughMoney();
        // one-way call instead of boolean return? 
        // check itinerary -> should not either exist or is empty perhaps better? 
        boolean ret = this.cancelBooking(clientId, itineraryId, cc);
        assertTrue(ret);
        i = this.getItinerary(clientId, itineraryId);
        assertEquals(i.getStatus(), BookingStatus.CANCELLED);

    }

    /*
     * Plan 3 mixed hotels/flights.
     * Book the items, 2nd booking should fail.
     * Check statuses
     */
    @Test
    public void test_B() {
        String clientId = "client";
        String itineraryId = "itinerary3";
        Itinerary i = createItinerary(clientId, itineraryId);


        // 1. pick 3 bookings ; flights and hotels

        // pick first (and only)
        ws.niceview.HotelBookingList hotels = searchHotels(clientId, itineraryId, "Singapour", "02-12-2013", "12-12-2013");
        ws.niceview.HotelBooking hb = hotels.getHotelBookings().get(0);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        // booking of this one should fail -> make sure cc won't have enough credit
        ws.lameduck.FlightBookingList flights = this.searchFlights(clientId, itineraryId, "Paris", "Copenhagen", "30-10-2013");
        ws.lameduck.FlightBooking fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
        fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());



        // 2. check statuses of bookings

        i = this.getItinerary(clientId, itineraryId);

        List<ItineraryHotelEntry> bookings = i.getHotelBookings();

        assertEquals(i.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);

        for (ItineraryHotelEntry b : bookings) {
            assertEquals(b.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);
        }
        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);
        }

        // 3. confirm bookings

        CreditCardInfoType cc = this.generateCardNotEnoughMoney();
        this.bookingItinerary(clientId, itineraryId, cc);

        // 4. check statuses of bookings

        i = this.getItinerary(clientId, itineraryId);

        bookings = i.getHotelBookings();

        // SHOULD BE SOMETHING ELSE, EG FAILED
        assertEquals(i.getStatus(), ws.travelgood.BookingStatus.UNCONFIRMED);

        // contains the booking that caused fault
        for (ItineraryHotelEntry b : bookings) {
            assertEquals(b.getStatus(), ws.travelgood.BookingStatus.CANCELLED);
        }

        // first one cancelled, 3rd one unconfirmed
        List<ItineraryFlightEntry> f = i.getFlightBookings();
        assertEquals(f.get(0).getStatus(), BookingStatus.UNCONFIRMED);
        assertEquals(f.get(0).getStatus(), BookingStatus.UNCONFIRMED);

    }

    /*
     * Plan a trip of 3 mixed flights/hotels.
     * Book items and cancel them.
     * Check statuses
     */
    @Test
    public void test_C1() {
        String clientId = "client";
        String itineraryId = "itinerary4";
        Itinerary i = createItinerary(clientId, itineraryId);


        // 1. pick 3 bookings ; flights and hotels

        // pick first (and only)
        ws.lameduck.FlightBookingList flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
        ws.lameduck.FlightBooking fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        // booking of this one should fail -> make sure cc won't have enough credit
        ws.niceview.HotelBookingList hotels = searchHotels(clientId, itineraryId, "Singapour", "02-12-2013", "12-12-2013");
        ws.niceview.HotelBooking hb = hotels.getHotelBookings().get(0);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        flights = this.searchFlights(clientId, itineraryId, "Paris", "Copenhagen", "30-10-2013");
        fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        // 2. book itinerary
        CreditCardInfoType cc = generateCardEnoughMoney();
        this.bookingItinerary(clientId, itineraryId, cc);

        // 3. check statuses

        i = this.getItinerary(clientId, itineraryId);

        for (ItineraryHotelEntry h : i.getHotelBookings()) {
            assertEquals(h.getStatus(), BookingStatus.CONFIRMED);
        }

        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), BookingStatus.CONFIRMED);
        }

        assertEquals(i.getStatus(), BookingStatus.CONFIRMED);

        // 4. cancel itinerary
        this.cancelBooking(clientId, itineraryId, cc);

        // 5. check statuses
        i = this.getItinerary(clientId, itineraryId);

        for (ItineraryHotelEntry h : i.getHotelBookings()) {
            assertEquals(h.getStatus(), BookingStatus.CANCELLED);
        }

        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), BookingStatus.CANCELLED);
        }

        assertEquals(i.getStatus(), BookingStatus.CANCELLED);
    }

    /*
     * Plan and book a trip of 3 mixed flights/hotels
     * Cancel the trip, 2nd cancellation should fail.
     * Check statuses
     */
    @Test
    public void test_C2() {
        String clientId = "client";
        String itineraryId = "itinerary5";
        Itinerary i = createItinerary(clientId, itineraryId);


        // 1. pick 3 bookings ; flights and hotels

        // pick first (and only)
        ws.niceview.HotelBookingList hotels = searchHotels(clientId, itineraryId, "Singapour", "02-12-2013", "12-12-2013");
        ws.niceview.HotelBooking hb = hotels.getHotelBookings().get(0);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        // cancelling of this booking should fail!
        ws.lameduck.FlightBookingList flights = searchFlights(clientId, itineraryId, "Copenhagen", "Paris", "29-10-2013");
        ws.lameduck.FlightBooking fb = flights.getFlightBookings().get(0);
        this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        hb = hotels.getHotelBookings().get(0);
        this.addHotel(clientId, itineraryId, hb.getBookingNumber());

        //flights  = this.searchFlights(clientId, itineraryId, "Paris", "Copenhagen", "30-10-2013");
        //fb = flights.getFlightBookings().get(0);
        //this.addFlight(clientId, itineraryId, fb.getBookingNumber());

        // 2. book itinerary
        CreditCardInfoType cc = generateCardEnoughMoney();
        this.bookingItinerary(clientId, itineraryId, cc);

        // 3. check statuses

        i = this.getItinerary(clientId, itineraryId);

        for (ItineraryHotelEntry h : i.getHotelBookings()) {
            assertEquals(h.getStatus(), BookingStatus.CONFIRMED);
        }

        for (ItineraryFlightEntry f : i.getFlightBookings()) {
            assertEquals(f.getStatus(), BookingStatus.CONFIRMED);
        }

        assertEquals(i.getStatus(), BookingStatus.CONFIRMED);

        // 4. cancel itinerary
        this.cancelBooking(clientId, itineraryId, generateInvalidCard());

        // 5. check that 2nd cancellation has failed
        i = this.getItinerary(clientId, itineraryId);

        // Again, should be failed or something
        assertEquals(i.getStatus(), BookingStatus.CANCELLED);

        List<ItineraryFlightEntry> f = i.getFlightBookings();
        List<ItineraryHotelEntry> h = i.getHotelBookings();

        assertEquals(h.get(0).getStatus(), BookingStatus.CANCELLED);
        assertEquals(f.get(0).getStatus(), BookingStatus.CONFIRMED);
        assertEquals(h.get(1).getStatus(), BookingStatus.CANCELLED);

    }

    private CreditCardInfoType generateCardNotEnoughMoney() {
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

        return cc;
    }

    private CreditCardInfoType generateCardEnoughMoney() {
        ExpirationDateType ed = new ExpirationDateType();
        CreditCardInfoType cc = new CreditCardInfoType();
        cc.setExpirationDate(ed);
        ed.setMonth(5);
        ed.setYear(9);
        cc.setName("Thor-Jensen Claus");
        cc.setNumber("50408825");

        return cc;
    }

    private CreditCardInfoType generateInvalidCard() {
        ExpirationDateType ed = new ExpirationDateType();
        CreditCardInfoType cc = new CreditCardInfoType();
        cc.setExpirationDate(ed);
        ed.setMonth(5);
        ed.setYear(9);
        cc.setName("Thor-Jensen Claus");
        cc.setNumber("uuu");

        return cc;
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

    private static FlightBookingList searchFlights(java.lang.String clientId, java.lang.String itineraryId, java.lang.String from, java.lang.String to, java.lang.String date) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.searchFlights(clientId, itineraryId, from, to, date);
    }

    private static boolean addFlight(java.lang.String clientId, java.lang.String itineraryId, java.lang.String bookingNum) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addFlight(clientId, itineraryId, bookingNum);
    }

    private static boolean cancelBooking(java.lang.String clientId, java.lang.String itineraryId, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo) {
        ws.travelgood.TravelGoodService service = new ws.travelgood.TravelGoodService();
        ws.travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.cancelBooking(clientId, itineraryId, creditcardInfo);
    }
}
