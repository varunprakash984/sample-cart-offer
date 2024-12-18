package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import com.springboot.controller.SegmentResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        clearOffers();
    }
    
    private void clearOffers() throws Exception {
        
    }

    @Test
    public void checkFlatXForOneSegment() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
        boolean result = addOffer(offerRequest);
        Assert.assertTrue(result); // able to add offer

        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
        applyOfferRequest.setCart_value(200);
        applyOfferRequest.setRestaurant_id(1);
        applyOfferRequest.setUser_id(1);

        ApplyOfferResponse applyOfferResponse = applyOffer(applyOfferRequest);
        Assert.assertEquals(190, applyOfferResponse.getCart_value());
    }

    @Test
    public void checkFlatXForMultipleSegments() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        segments.add("p2");
        OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
        boolean result = addOffer(offerRequest);
        Assert.assertTrue(result); // able to add offer

        // Test for p1 segment
        ApplyOfferRequest applyOfferRequestP1 = new ApplyOfferRequest();
        applyOfferRequestP1.setCart_value(200);
        applyOfferRequestP1.setRestaurant_id(1);
        applyOfferRequestP1.setUser_id(1);
        ApplyOfferResponse applyOfferResponseP1 = applyOffer(applyOfferRequestP1);
        Assert.assertEquals(190, applyOfferResponseP1.getCart_value());

        // Test for p2 segment
        ApplyOfferRequest applyOfferRequestP2 = new ApplyOfferRequest();
        applyOfferRequestP2.setCart_value(200);
        applyOfferRequestP2.setRestaurant_id(1);
        applyOfferRequestP2.setUser_id(2);
        ApplyOfferResponse applyOfferResponseP2 = applyOffer(applyOfferRequestP2);
        Assert.assertEquals(190, applyOfferResponseP2.getCart_value());
    }
    
    @Test
    public void checkFlatPercentageForOneSegment() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(1, "FLAT_PERCENTAGE", 10, segments);
        boolean result = addOffer(offerRequest);
        Assert.assertTrue(result);

        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
        applyOfferRequest.setCart_value(200);
        applyOfferRequest.setRestaurant_id(1);
        applyOfferRequest.setUser_id(1);

        ApplyOfferResponse applyOfferResponse = applyOffer(applyOfferRequest);
        Assert.assertEquals(180, applyOfferResponse.getCart_value());
    }
    
    @Test
    public void checkFlatPercentageForMultipleSegments() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        segments.add("p3");
        OfferRequest offerRequest = new OfferRequest(1, "FLAT_PERCENTAGE", 50, segments);
        boolean result = addOffer(offerRequest);
        Assert.assertTrue(result);

        // Test for p1 segment
        ApplyOfferRequest applyOfferRequestP1 = new ApplyOfferRequest();
        applyOfferRequestP1.setCart_value(300);
        applyOfferRequestP1.setRestaurant_id(1);
        applyOfferRequestP1.setUser_id(1);
        ApplyOfferResponse applyOfferResponseP1 = applyOffer(applyOfferRequestP1);
        Assert.assertEquals(150, applyOfferResponseP1.getCart_value());
        
        // Test for p3 segment
        ApplyOfferRequest applyOfferRequestP3 = new ApplyOfferRequest();
        applyOfferRequestP3.setCart_value(500);
        applyOfferRequestP3.setRestaurant_id(1);
        applyOfferRequestP3.setUser_id(3);
        ApplyOfferResponse applyOfferResponseP3 = applyOffer(applyOfferRequestP3);
        Assert.assertEquals(250, applyOfferResponseP3.getCart_value());
    }
    
    @Test
    public void checkNoOfferForUserSegment() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
        applyOfferRequest.setCart_value(200);
        applyOfferRequest.setRestaurant_id(1);
        applyOfferRequest.setUser_id(4);

        ApplyOfferResponse applyOfferResponse = applyOffer(applyOfferRequest);
        Assert.assertEquals(200, applyOfferResponse.getCart_value());
    }
    
    @Test
    public void checkNoOfferForRestaurant() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
        boolean result = addOffer(offerRequest);
        Assert.assertTrue(result);
        
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
        applyOfferRequest.setCart_value(200);
        applyOfferRequest.setRestaurant_id(2);
        applyOfferRequest.setUser_id(1);

        ApplyOfferResponse applyOfferResponse = applyOffer(applyOfferRequest);
        Assert.assertEquals(200, applyOfferResponse.getCart_value());
    }

    @Test
    public void checkMultipleOffersForRestaurantAndSegment() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest1 = new OfferRequest(1, "FLATX", 10, segments);
        addOffer(offerRequest1);
        OfferRequest offerRequest2 = new OfferRequest(1, "FLAT_PERCENTAGE", 20, segments);
        addOffer(offerRequest2);

        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
        applyOfferRequest.setCart_value(200);
        applyOfferRequest.setRestaurant_id(1);
        applyOfferRequest.setUser_id(1);

        ApplyOfferResponse applyOfferResponse = applyOffer(applyOfferRequest);
        Assert.assertEquals(190, applyOfferResponse.getCart_value());
    }

    public boolean addOffer(OfferRequest offerRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        String POST_PARAMS = mapper.writeValueAsString(offerRequest);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
            return true;
        } else {
            System.out.println("POST request did not work.");
            return false;
        }
    }

    public ApplyOfferResponse applyOffer(ApplyOfferRequest applyOfferRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/cart/apply_offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        String POST_PARAMS = mapper.writeValueAsString(applyOfferRequest);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);
            int cartValue = new org.json.JSONObject(response.toString()).getInt("cart_value");
            return new ApplyOfferResponse(cartValue);
        } else {
            System.out.println("POST request did not work. Response code: " + responseCode);
            return null;
        }
    }
 }
