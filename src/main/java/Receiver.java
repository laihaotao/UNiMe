import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.language_translator.v2.util.Language;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.post;

/**
 * Author:  Eric(Haotao) Lai
 * Date:    2018-01-27
 * E-mail:  haotao.lai@gmail.com
 * Website: http://laihaotao.me
 */


public class Receiver {

    public static void main(String[] args) {

        post("receive-sms", (Request req, Response res) -> {

            // receive the client message, put them into a map
            String body = req.body();
            String[] bodies = body.split("&");
            Map<String, String> map = new HashMap<>();
            for (String str : bodies) {
                String[] tmp = str.split("=");
                if (tmp.length == 2) {
                    map.put(tmp[0], tmp[1]);
                }
            }

            int numMedia = Integer.parseInt(map.get("NumMedia"));

            // if it is an image
            if (numMedia > 0) {
                int counter = 0;
                while ((numMedia--) > 0) {
                    // get the real image url and parse it
                    String rawURL = map.get("MediaUrl" + counter++);
                    String url = parseURL(rawURL);

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
                    System.out.println(result);

                    // handle the result and give response to client
                }
            }
            // not an image
            else {
                String message = "";
                for(String string: map.keySet()){
                    if(string.equals("Body")){
                        message = map.get(string).replaceAll("\\+"," ");
                        break;
                    }
                }

                LanguageTranslator service = new LanguageTranslator();
                service.setUsernameAndPassword("3349f689-0b9b-45db-8520-e41c3ea5d6df","T4kqlsaqLEAE");

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(message);

                TranslateOptions translateOptions = new TranslateOptions.Builder()
                        .text(arrayList)
                        .source(Language.ENGLISH)
                        .target(Language.FRENCH)
                        .build();

                TranslationResult result = service.translate(translateOptions)
                        .execute();

                System.out.println(result);

//                System.out.println(message);
            }

            Message sms = new Message.Builder()
                    .body(new Body("hello world"))
                    .build();
            MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
            return twiml.toXml();

        });
    }

    private static String parseURL(String url) {
        String suffix = url.substring(14);
        return "http://" + suffix.replaceAll("%2F", "/");
    }


}
