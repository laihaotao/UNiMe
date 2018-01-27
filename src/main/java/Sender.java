import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Author:  Eric(Haotao) Lai
 * Date:    2018-01-27
 * E-mail:  haotao.lai@gmail.com
 * Website: http://laihaotao.me
 */

public class Sender {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "AC6f210f3460c42ad7a298d9d0b604b7d3";
    public static final String AUTH_TOKEN = "6ffec0140a8a0d58bac5f14df170e32f";

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
