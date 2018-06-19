package com.candidate.services;

import com.candidate.bringg_model.BringgCustomerDTO;
import com.candidate.bringg_model.BringgOrderDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;

@Service
public class BringgService {

    private final String create_user_api = "https://developer-api.bringg.com/partner_api/customers";

    private String key = "V_-es-3JD82YyiNdzot7";
    private String accessToken = "ZtWsDxzfTTkGnnsjp8yC";
    private long companyId = 11010;

    public boolean createOrder(BringgOrderDTO bringgOrderDTO){
        boolean success = false;

        return success;
    }

    public boolean createCustomer(BringgCustomerDTO bringgCustomerDTO) throws UnsupportedEncodingException {
        boolean success = false;

        bringgCustomerDTO.setCompany_id(companyId);
        bringgCustomerDTO.setAccess_token(accessToken);
        bringgCustomerDTO.setTimestamp(Instant.now().toEpochMilli());

        bringgCustomerDTO.setSignature(calcHmacSha1Signature(bringgCustomerDTO));

        HttpResponse<com.mashape.unirest.http.JsonNode> bringgCustomerResponseDTOHttpResponse = null;
        try {
            bringgCustomerResponseDTOHttpResponse =
                    Unirest.post(create_user_api)
                            .header("content-type", "application/json")
//                            .field("name", bringgCustomerDTO.getName())
//                            .field("company_id", bringgCustomerDTO.getCompany_id())
//                            .field("phone", bringgCustomerDTO.getPhone())
//                            .field("address", bringgCustomerDTO.getAddress())
//                            .field("timestamp", bringgCustomerDTO.getTimestamp())
//                            .field("access_token", bringgCustomerDTO.getAccess_token())
//                            .field("signature", bringgCustomerDTO.getSignature())
//                            .asObject(BringgCustomerResponseDTO.class);
                            .body(new Gson().toJson(bringgCustomerDTO))
//                            .asObject(BringgCustomerResponseDTO.class);
                            .asJson();

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (bringgCustomerResponseDTOHttpResponse != null){
//            success = bringgCustomerResponseDTOHttpResponse.getBody().isSuccess();
        }

        return success;
    }

    private String calcHmacSha1Signature(BringgCustomerDTO bringgCustomerDTO) throws UnsupportedEncodingException {
        String query_params = "";
//        query_params += "name=" + bringgCustomerDTO.getName();
//        query_params += "&company_id=" + bringgCustomerDTO.getCompany_id();
//        query_params += "&phone=" + bringgCustomerDTO.getPhone();
//        query_params += "&address=" + bringgCustomerDTO.getAddress();
//        query_params += "&timestamp=" + bringgCustomerDTO.getTimestamp();
//        query_params += "&access_token=" + bringgCustomerDTO.getAccess_token();

        JsonObject jsonObject = new Gson().toJsonTree(bringgCustomerDTO).getAsJsonObject();

        for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()){
            if (query_params.length() > 0){
                query_params += '&';
            }

            query_params += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().getAsString(), "UTF-8");
        }

        return signQueryParams(query_params);
    }

    private String signQueryParams(String query_params){
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec sha1Key = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(sha1Key);

            // compute the binary signature for the request
            byte[] sigBytes = mac.doFinal(query_params.getBytes());

            // base 64 encode the binary signature
            String signature = Base64.encodeBase64String(sigBytes);

            return signature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
