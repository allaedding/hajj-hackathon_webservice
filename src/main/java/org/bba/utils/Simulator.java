package org.bba.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bba.vision.GCVisionHelper;

public class Simulator {
    private static final String[] PLACES_SET = new String[]{"jamarat_1, jamarat_2, jamarat_3, mina, haram, tawaf, saai, arafat,jeddah"};
    private static final String IMAGE_EXTENSION = ".jpg";
    private static String imageDirectory;
    private static int counter = 0;
    private static int maxCounter;

    public static void init(String dir) {

        imageDirectory = dir;
    }

    public static List<String> batchImageScheduler(List<String> places) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            result.add(Paths.get(imageDirectory, places.get(i), i + IMAGE_EXTENSION).toString());
        }
        return result;
    }

    //public static List<String> getDahsboardBatchAnalytics(List<String> place, String label) throws Exception {
        //for (imageDirectory)
        //maxCounter = (new File(Paths.get(imageDirectory, place).toString())).list().length;
      //  return ResultFormatter.getDashboardMoreJson(GCVisionHelper.detectLabels(imageScheduler(place), label));
    //}

    public static String imageScheduler(String place) {
        if (counter >= maxCounter) {
            counter = 0;
        }
        String result = Paths.get(imageDirectory, place, counter + IMAGE_EXTENSION).toString();
        counter++;
        return result;
    }

    public static String getPlaceAnalytics(String place, String label, String json) throws Exception {
        maxCounter = (new File(Paths.get(imageDirectory, place).toString())).list().length;
        ResultFormatter.getJsonString(GCVisionHelper.detectLabels(imageScheduler(place), label));
        return !json.equals("true") ?
                ResultFormatter.getSingleValue(GCVisionHelper.detectLabels(imageScheduler(place), label)) :
                ResultFormatter.getJsonString(GCVisionHelper.detectLabels(imageScheduler(place), label));
    }

    public static String getDahsboardPlaceAnalytics(String place, String label) throws Exception {
        maxCounter = (new File(Paths.get(imageDirectory, place).toString())).list().length;
        return ResultFormatter.getDashboardJson(GCVisionHelper.detectLabels(imageScheduler(place), label));
    }
}