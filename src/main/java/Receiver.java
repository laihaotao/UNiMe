import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Translation;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.language_translator.v2.util.Language;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.*;
import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.post;

/**
 * ConUHacks 2017-2018
 * Author:  Haotao (Eric) Lai, Youwei Li, Qi Xia
 * Date:    2018-01-27
 */

public class Receiver {

    public static void main(String[] args) {

        post("receive-sms", (Request req, Response res) -> {

            // receive the client message, put them into a map
            String              body   = req.body();
            String[]            bodies = body.split("&");
            Map<String, String> map    = new HashMap<>();
            for (String str : bodies) {
                String[] tmp = str.split("=");
                if (tmp.length == 2) {
                    map.put(tmp[0], tmp[1]);
                }
            }

            int numMedia = Integer.parseInt(map.get("NumMedia"));
            String reply = null;

            // if it is an image
            if (numMedia > 0) {
                int counter = 0;
                while ((numMedia--) > 0) {
                    // get the real image url and parse it
                    String rawURL = map.get("MediaUrl" + counter++);
                    String url    = parseURL(rawURL);

                    // call IBM Watson API to classify
                    VisualRecognition service = new VisualRecognition(
                            VisualRecognition.VERSION_DATE_2016_05_20
                    );
                    service.setApiKey("715a825eb7f3119688b32a2baf18427346e0af53");

                    ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                            .parameters("{\"threshold\": \"0.6\","
                                        + "\"url\":\"" + url + "\""
                                        + "}")
                            .build();
                    ClassifiedImages result = service.classify(classifyOptions).execute();

                    //System.out.println(result);

                    // parse the json from the AI's response
                    List<ClassifiedImage>  classifiedImagList   = result.getImages();
                    ClassifiedImage        classifiedImage      = classifiedImagList.get(0);
                    List<ClassifierResult> classifierResultList = classifiedImage.getClassifiers();
                    ClassifierResult       classifierResult     = classifierResultList.get(0);
                    List<ClassResult>      classResultList      = classifierResult.getClasses();
                    classResultList.sort((o1, o2) -> {
                        if (o1.getScore() > o2.getScore()) return 1;
                        return -1;
                    });
                    // extract the result and return to the client
                    for (ClassResult cResult : classResultList) {
                        if (!cResult.getClassName().contains("color")) {
                            reply = cResult.getClassName();
                            break;
                        }
                    }
                }
            }

            // not an image, it must be a text message
            else {
                // get client input message
                String clientMsg = "";
                for (String string : map.keySet()) {
                    if (string.equals("Body")) {
                        clientMsg = map.get(string).replaceAll("\\+", " ");
                        break;
                    }
                }

                // determine translate which language to which language
                String[] indicator = clientMsg.split("_");
                String   source, target;
                if ("f".equals(indicator[0].toLowerCase())) {
                    source = Language.FRENCH;
                    target = Language.ENGLISH;
                } else {
                    source = Language.ENGLISH;
                    target = Language.FRENCH;
                }

                LanguageTranslator service = new LanguageTranslator();
                service.setUsernameAndPassword("3349f689-0b9b-45db-8520-e41c3ea5d6df",
                        "T4kqlsaqLEAE");

                ArrayList<String> arrayList = new ArrayList<>();
                if (clientMsg.contains("_")) {
                    arrayList.add(indicator[1]);
                } else {
                    arrayList.add(clientMsg);
                }

                TranslateOptions translateOptions = new TranslateOptions.Builder()
                        .text(arrayList)
                        .source(source)
                        .target(target)
                        .build();

                TranslationResult result = service.translate(translateOptions)
                        .execute();

                Translation transRes = result.getTranslations().get(0);
                reply = transRes.getTranslation();

//                System.out.println(reply);
            }

            // reply the message to the client
            if (reply != null) {
                Message sms = new Message.Builder()
                        .body(new Body(reply))
                        .build();
                MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
                return twiml.toXml();
            }

            return null;
        });
    }

    private static String parseURL(String url) {
        String suffix = url.substring(14);
        return "http://" + suffix.replaceAll("%2F", "/");
    }


}
