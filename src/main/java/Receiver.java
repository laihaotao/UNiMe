import com.twilio.twiml.Message;
import com.twilio.twiml.Body;
import com.twilio.twiml.MessagingResponse;
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

            Message sms = new Message.Builder()
                    .body(new Body("hello world"))
                    .build();
            MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
            return twiml.toXml();

        });
    }
}
