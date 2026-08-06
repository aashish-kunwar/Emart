package helper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EsewaUtil {

    public static final String PRODUCT_CODE = "EPAYTEST";

    /*
     * eSewa sandbox secret key.
     * Replace this with your live merchant secret key in production.
     */
    private static final String SECRET_KEY = "8gBm/:&EnhH.1/q";

    /*
     * eSewa sandbox payment URL.
     */
    public static final String PAYMENT_URL =
            "https://rc-epay.esewa.com.np/api/epay/main/v2/form";

    public static String generateSignature(
            String totalAmount,
            String transactionUuid) {

        try {

            String message =
                    "total_amount=" + totalAmount
                    + ",transaction_uuid=" + transactionUuid
                    + ",product_code=" + PRODUCT_CODE;

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(
                            SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(secretKeySpec);

            byte[] hash =
                    mac.doFinal(
                            message.getBytes(StandardCharsets.UTF_8)
                    );

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate eSewa signature",
                    e
            );
        }
    }
}