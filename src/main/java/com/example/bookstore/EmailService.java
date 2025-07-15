package com.example.bookstore;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendBookCreationEmail(Book book) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("sumah8050@gmail.com");
            message.setSubject("New Book Added: " + book.getTitle());
            message.setText("Book added: " + book.getAuthor());

            mailSender.send(message);

            System.out.println("✅ Email sent successfully to sumah8050@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }

    public void sendSimpleMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

//    public void sendPurchaseConfirmation(String recipient, Book book, int purchasedQuantity, int remainingQuantity) {
public void sendPurchaseConfirmation(String recipient, Purchase purchase, int remainingQuantity){
    SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("📚 Book Purchase Confirmation");

    message.setText("Thank you for your purchase!\n\n" +
            "Book: " + purchase.getBookTitle() + "\n" +
            "Quantity Purchased: " + purchase.getQuantityPurchased() + "\n" +
            "Quantity Remaining in Store: " + remainingQuantity + "\n" +
            "Total Price: ₹" + purchase.getPrice() + "\n\n" +
            "📬 Happy Reading!\n\n" + "Keep visiting the store :)");

    mailSender.send(message);
    }

}