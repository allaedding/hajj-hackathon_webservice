package org.bba.rest;

import static org.bba.vision.GCVisionHelper.detectLabels;
import static org.bba.vision.GCVisionHelper.detectLabelsScore;
import static spark.Spark.get;

import org.bba.utils.Simulator;
import org.bba.vision.GCVisionHelper;

import com.google.auth.oauth2.GoogleCredentials;

import spark.Spark;

public class Endpoint {
    private static int counter = 0;

    public Endpoint() {
    }

    public static void main(String[] args) throws Exception {
        String imageUrl = "https://twasul.info/wp-content/uploads/2017/08/196168.jpg";
        String localImage = args[0];
        System.out.println("Using input directory: " + localImage);
        Simulator.init(localImage);
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        System.out.println("Launching Winning System");
        System.out.println("Listening on port 4567");

        String sZem[]={"Z1","Z2","Z3","Z4"};
        get("/Z", (req, res) -> GCVisionHelper.detectLabels(localImage, sZem));
        get("/minZ", (req, res) -> GCVisionHelper.detectLabelsMinScore(localImage, sZem));

        String sWod[]={"W1","W2","W3","W4"};
        get("/W", (req, res) -> GCVisionHelper.detectLabels(localImage, sWod));
        get("/minW", (req, res) -> GCVisionHelper.detectLabelsMinScore(localImage, sWod));

        String sJ[]={"J1","J2","J3","J4"};
        get("/J", (req, res) -> GCVisionHelper.detectLabels(localImage, sJ));
        get("/minJ", (req, res) -> GCVisionHelper.detectLabelsMinScore(localImage, sJ));


        get("/Z1", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "Z1"));
        get("/Z2", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "Z2"));
        get("/Z3", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "Z3"));
        get("/Z4", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "Z4"));

        get("/W1", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "W1"));
        get("/W2", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "W2"));
        get("/W3", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "W3"));
        get("/W4", (req, res) -> GCVisionHelper.detectLabelsScore(localImage, "W4"));


        get("/hello", (req, res) -> {
            return "Hello World";
        });
        get("/vision", (req, res) -> {
            res.type("application/json");
            return Simulator.getPlaceAnalytics(req.queryParams("place"), req.queryParams("label"), req.queryParams("json"));
        });
        get("/dashboard", (req, res) -> {
            res.type("application/json");
            return Simulator.getDahsboardPlaceAnalytics(req.queryParams("place"), req.queryParams("label"));
        });
    }
}