# UNiMe

We have designed and implemented a SMS automatic reply system:

1. Using **Twilio APIs** to implement message sending and replying;
2. Using **IBM Watson API** to implement visual recognition;
3. Using **IBM Watson API** to implement two-ways translation (French <-> English).

## Team Information

We are a three-member-team as listed below (the list follows the first character of the first name's order in the alphabet):

- [Grey Lee](https://github.com/kidd-4)
- [Haotao Lai](http://laihaotao.me/about/)
- [Qi Xia](https://github.com/sakichat)

## Related Techniques

1. The major system is built through using **Twilio SMS APIs**, we set up a phone number to act as the chatbot;
2. The AI functionality is built by using **IBM APIs**:  
  2.1 If you send an image, the system will recognize what it is and give back the result through SMS.  
  2.2 The translation function, can achieve two-way translation, between English and French. You can type "F_" before the text if you want to translate from French to English, otherwise it will translate from English to French.

## Examples

1. Image Recognize  
  By sending image to our number (514-700-5856), you will receive the result from our system.  
  
  ![](./img/image1.png)
  ![](./img/image2.png)
  ![](./img/image3.png)
  
2. Two-way Translation  
  By texting English or French (adding "F_" before French means translate French to English), you will receive the translation result from our system.  
  
  ![](./img/text1.png)
