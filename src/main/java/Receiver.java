import com.twilio.twiml.Message;
import com.twilio.twiml.Body;
import com.twilio.twiml.MessagingResponse;

import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static spark.Spark.post;

/**
 * Author:  Eric(Haotao) Lai
 * Date:    2018-01-27
 * E-mail:  haotao.lai@gmail.com
 * Website: http://laihaotao.me
 */


public class Receiver {

    public static void main(String[] args) {

        post("receive-sms", (req, res) -> {

            int len = req.contentLength();
            System.out.println(len);

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
//
//            Set<String> headers = req.headers();
//            for (String h : headers) {
//                System.out.println(h + ": " + req.headers(h));
//            }

            // if it is an image
            if (numMedia > 0) {
                int counter = 0;
                while ((numMedia--) > 0) {
                    String rawURL = map.get("MediaUrl" + counter++);
                    // download image from that url
                    System.out.println(parseURL(rawURL));
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
                System.out.println(message);
            }

            Message sms = new Message.Builder()
                    .body(new Body("hello world"))
                    .build();
            MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
            return twiml.toXml();

        });
    }

    public static String parseURL(String url) {
        String suffix = url.substring(14);
        return "http://" + suffix.replaceAll("%2F", "/");
    }


}
