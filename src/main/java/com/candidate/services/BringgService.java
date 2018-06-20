package com.candidate.services;

import com.candidate.bringg_model.BringgCustomerDTO;
import com.candidate.bringg_model.BringgCustomerResponseDTO;
import com.candidate.bringg_model.BringgOrderDTO;
import com.candidate.bringg_model.BringgOrderResponseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

@Service
public class BringgService {

    private final String customers_api = "https://developer-api.bringg.com/partner_api/customers";
    private final String orders_api = "https://developer-api.bringg.com/partner_api/tasks";

    private String key = "V_-es-3JD82YyiNdzot7";
    private String accessToken = "ZtWsDxzfTTkGnnsjp8yC";
    private int companyId = 11010;

    public BringgOrderResponseDTO createOrder(BringgOrderDTO bringgOrderDTO) throws UnsupportedEncodingException, UnirestException {
        BringgOrderResponseDTO bringgOrderResponseDTO = null;

        bringgOrderDTO.setCompany_id(companyId);
        bringgOrderDTO.setAccess_token(accessToken);
        bringgOrderDTO.setTimestamp(""+Instant.now().toEpochMilli());

        bringgOrderDTO.setSignature(calcHmacSha1Signature(new Gson().toJsonTree(bringgOrderDTO).getAsJsonObject()));

        HttpResponse<JsonNode> bringgCreateOrderApiResponse =
                Unirest.post(orders_api)
                        .header("content-type", "application/json")
                        .body(new Gson().toJson(bringgOrderDTO))
                        .asJson();

        bringgOrderResponseDTO = new Gson().fromJson(bringgCreateOrderApiResponse.getBody().toString(), BringgOrderResponseDTO.class);

        return bringgOrderResponseDTO;
    }

    public List<BringgOrderDTO> getOrders(int page) throws UnirestException, UnsupportedEncodingException {
        Map<String,Object> query_params = new HashMap<String,Object>(){{
            put("access_token", accessToken);
            put("timestamp", ""+Instant.now().toEpochMilli());
            put("company_id", companyId);
            put("page", page);
        }};

        String signature = calcHmacSha1Signature(query_params);
        query_params.put("signature", signature);

        HttpResponse<JsonNode> bringgListOrdersApiResponse =
                Unirest.get(orders_api)
                        .header("content-type", "application/json")
                        .queryString(query_params)
                        .asJson();

        Type listType = new TypeToken<ArrayList<BringgOrderDTO>>(){}.getType();
        List<BringgOrderDTO> bringgOrderDTOList = new Gson().fromJson(bringgListOrdersApiResponse.getBody().toString(), listType);

        return bringgOrderDTOList;
    }

    public BringgCustomerResponseDTO createCustomer(BringgCustomerDTO bringgCustomerDTO) throws UnsupportedEncodingException, UnirestException {
        BringgCustomerResponseDTO bringgCustomerResponseDTO = null;

        bringgCustomerDTO.setCompany_id(companyId);
        bringgCustomerDTO.setAccess_token(accessToken);
        bringgCustomerDTO.setTimestamp("" + Instant.now().toEpochMilli());

        bringgCustomerDTO.setSignature(calcHmacSha1Signature(new Gson().toJsonTree(bringgCustomerDTO).getAsJsonObject()));

        HttpResponse<JsonNode> bringgCustomerApiResponse =
                Unirest.post(customers_api)
                        .header("content-type", "application/json")
                        .body(new Gson().toJson(bringgCustomerDTO))
                        .asJson();

        bringgCustomerResponseDTO = new Gson().fromJson(bringgCustomerApiResponse.getBody().toString(), BringgCustomerResponseDTO.class);

        return bringgCustomerResponseDTO;
    }

    private String calcHmacSha1Signature(JsonObject jsonObject) throws UnsupportedEncodingException {
        String query_params = "";

        for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()){
            if (query_params.length() > 0){
                query_params += '&';
            }

            query_params += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().getAsString(), "UTF-8");
        }

        return signQueryParams(query_params);
    }

    private String calcHmacSha1Signature(Map<String,Object> queryParamMap) throws UnsupportedEncodingException {
        String query_params = "";

        for (Map.Entry<String,Object> entry : queryParamMap.entrySet()){
            if (query_params.length() > 0){
                query_params += '&';
            }

            query_params += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8");
        }

        return signQueryParams(query_params);
    }

    private String signQueryParams(String queryParams){
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec sha1Key = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(sha1Key);

            // compute the binary signature for the request
            byte[] sigBytes = mac.doFinal(queryParams.getBytes());

            String signature = toHexString(sigBytes);

            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }


}
