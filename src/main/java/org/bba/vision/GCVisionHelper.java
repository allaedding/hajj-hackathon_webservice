package org.bba.vision;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class GCVisionHelper {

    public static String txtMin;
    public static double scoreMin;

    public static Map<String, String> detectLabels(String filePath, String label) throws Exception {

        HashMap<String, String> result = new HashMap();

        List<AnnotateImageRequest> requests = new ArrayList();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    //result.put("error", res.getError().getMessage());
                    result.put(label, "0");
                    return result;
                }
                List<EntityAnnotation> filtered = res.getLabelAnnotationsList().stream()
                        .filter(ann -> ann.getDescription().equals(label))
                        .collect(Collectors.toList());
                result.put(label, String.valueOf(filtered.get(0).getScore()));
            }
        }
        System.out.println(result.toString());
        return result;
    }

    public static String detectLabels(String filePath, String lieu[]) throws Exception {

        StringBuilder out = new StringBuilder();
        out.append("{  \"graph\" : {\"title\" : \" p \",\"datasequences\": [ {\"title\":\"p\",\"datapoints\" : [");
        int i = 1;
        for (String s : lieu) {
            List<AnnotateImageRequest> requests = new ArrayList<>();
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(Paths.get(filePath, s, "1.jpg").toString()));
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);
            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        return "Error: %s\n" + res.getError().getMessage();
                    }
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        if (annotation.getDescription().equals("crowd")) {
                            out.append(" { \"title\" : \"");
                            out.append(s);

                            out.append("\", \"value\" : ");

                            out.append(annotation.getScore());

                            out.append("}");
                            if (i < lieu.length)
                                out.append(",");
                        }
                    }
                }
            }
            i++;

        }
        out.append("]}]}}");
        return out.toString();
    }
    public static String detectLabelsMinScore(String filePath, String lieu[]) throws Exception {
        scoreMin = 100;
        for (String s : lieu) {
            List<AnnotateImageRequest> requests = new ArrayList<>();
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(Paths.get(filePath, s, "1.jpg").toString()));
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);
            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        return "Error: %s\n" + res.getError().getMessage();
                    }
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        if (annotation.getDescription().equals("crowd")) {
                            if (annotation.getScore()<scoreMin)  {
                                txtMin = s;
                                scoreMin = annotation.getScore();
                            }
                        }
                    }

            }
        }
    }
        return txtMin;

    }

    public static String detectLabelsScore(String filePath, String lieu) throws Exception {
            List<AnnotateImageRequest> requests = new ArrayList<>();
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(Paths.get(filePath,lieu, "1.jpg").toString()));
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);
            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        return "0";
                    }
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        if (annotation.getDescription().equals("crowd")) {
                                return annotation.getScore()+"";

                        }
                    }

                }
            }

        return "0";
    }
}
