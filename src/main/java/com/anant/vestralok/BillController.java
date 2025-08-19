package com.anant.vestralok;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BillController {

    @GetMapping("/bill")
    public String billForm(Model model) {
        model.addAttribute("message", "");
        return "bill";
    }

    @PostMapping("/generateBill")
    public String generateBill(
            @RequestParam("customerName") String customerName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("amount") double amount,
            Model model) {

        // Generate bill details
        String billDetails = generateBillDetails(customerName, amount);

        // Send SMS
        boolean smsSent = sendSMS(phoneNumber, billDetails);

        if (smsSent) {
            model.addAttribute("message", "Bill generated and sent successfully to " + phoneNumber);
            model.addAttribute("billDetails", billDetails);
        } else {
            model.addAttribute("message", "Failed to send SMS. Bill generated but not sent.");
            model.addAttribute("billDetails", billDetails);
        }

        return "bill";
    }

   private String generateBillDetails(String customerName, double amount) {
        return "Bill for " + customerName +
                "\nTotal Price: =₹ " + String.format("%.2f", amount + (amount * 0.10)) +
                "\nDiscount:    =₹ " + String.format("%.2f", amount * 0.10) +
                "\nFinal Price: =₹ " + String.format("%.2f", amount) +
                "\n\nThank you for Visiting!\nAnant Vestralok, Maksi";
    }

    private boolean sendSMS(String phoneNumber, String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authorization", System.getenv("FAST2SMS_API_KEY"));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("route", "q");
            requestBody.put("message", message);
            requestBody.put("language", "english");
            requestBody.put("numbers", phoneNumber);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://www.fast2sms.com/dev/bulkV2",
                    entity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
