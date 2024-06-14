package student.examples;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.internal.annotations.IListeners;

import java.util.Map;

public class CalculationServiceTestListener implements ITestListener {
    @Override
    public void onFinish(ITestContext context) {

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.mail.ru", 465 , "sobakapavlova65@mail.ru", "xZvr4ArbVyfepnNiM5T6")
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer();

        context.getPassedTests().getAllResults().forEach((result) -> {
            Map<String, Object> sample = (Map<String, Object>) result.getParameters()[0];
            Email email = EmailBuilder.startingBlank()
                    .from("From", "sobakapavlova65@mail.ru")
                    .to("1 st Receiver", (String) sample.get("email"))
                    .withSubject("Test Results")
                    .withPlainText("PATANI HAI LA PIVA")

                    .buildEmail();
            mailer.sendMail(email);
        });
        //System.out.println("Test finished" + context.getPassedTests().getAllResults());
    }
}