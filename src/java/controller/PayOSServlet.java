package controller;

import com.google.gson.Gson;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/payos") // ✅ Annotation chỉ định đường dẫn truy cập servlet
public class PayOSServlet extends HttpServlet {

    private static final String CLIENT_ID = "109555ab-95c7-44ef-b5fd-f2380c2b6f7d";
    private static final String API_KEY = "315aec97-6158-4eb4-85b3-53f5657b44ec";
    private static final String CHECKSUM_KEY = "f4196047bd74578150447f6eea1d0e643f7a7806a";
    private static final String API_URL = "https://api-merchant.payos.vn/payment-requests";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("PayOSServlet is running");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doPost called");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String totalAmountStr = request.getParameter("totalAmount");
        if (totalAmountStr == null || totalAmountStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing totalAmount");
            return;
        }
        int totalAmount = Integer.parseInt(totalAmountStr);

        int orderCode = new Random().nextInt(1000000);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("orderCode", orderCode);
        body.put("amount", totalAmount);
        body.put("description", "Thanh toán đơn hàng cho " + name);
        body.put("returnUrl", "https://yourdomain.com/order-success");  // có thể để tạm
        body.put("cancelUrl", "https://yourdomain.com/order-cancel");  // có thể để tạm

        Gson gson = new Gson();
        String jsonBody = gson.toJson(body);
        String signature = createHmacSignature(jsonBody, CHECKSUM_KEY);

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("x-client-id", CLIENT_ID);
        conn.setRequestProperty("x-api-key", API_KEY);
        conn.setRequestProperty("x-checksum", signature);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        InputStream is;
        int status = conn.getResponseCode();
        if (status >= 200 && status < 300) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        StringBuilder responseText = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            responseText.append(line.trim());
        }
        System.out.println("Response from PayOS API: " + responseText.toString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseText.toString());
    }

    private String createHmacSignature(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo signature: " + e.getMessage());
        }
    }
}
