import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * ConUHacks 2017-2018
 * Author:  Haotao (Eric) Lai, Youwei Li, Qi Xia
 * Date:    2018-01-27
 */

public class Sender {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                // receiver phone number
                new PhoneNumber("+15145539923"),
                // sender phone number
                new PhoneNumber("+15147005856"),
                // message body
                "hello I'm Eric").create();
        System.out.println(message.getSid());
    }
}
