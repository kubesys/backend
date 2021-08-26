package com.github.kubesys.mcmf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kubesys.backend.services.features.McmfService;

public class McmfTest {
    public static void main(String[] args) throws Exception {
        McmfService ms = new McmfService();
        String inputJson = "{\n" +
                "\t\"apiVersion\": \"cloudplus.io/v1alpha3\",\n" +
                "\t\"kind\": \"MCMF\",\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"additionalProperties\": {},\n" +
                "\t\t\"finalizers\": [],\n" +
                "\t\t\"managedFields\": [],\n" +
                "\t\t\"name\": \"simpleparameter\",\n" +
                "\t\t\"ownerReferences\": []\n" +
                "\t},\n" +
                "\t\"spec\": {\n" +
                "\t\t\"data\": {\n" +
                "\t\t\t\"json\": {\n" +
                "\t\t\t\t\"tasks\": [{\n" +
                "\t\t\t\t\t\t\"index\": 1,\n" +
                "\t\t\t\t\t\t\"cpu\": 4,\n" +
                "\t\t\t\t\t\t\"cost\": 10,\n" +
                "\t\t\t\t\t\t\"application\": 6\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 2,\n" +
                "\t\t\t\t\t\t\"cpu\": 2,\n" +
                "\t\t\t\t\t\t\"cost\": 5,\n" +
                "\t\t\t\t\t\t\"application\": 6\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 3,\n" +
                "\t\t\t\t\t\t\"cpu\": 2,\n" +
                "\t\t\t\t\t\t\"cost\": 3,\n" +
                "\t\t\t\t\t\t\"application\": 6\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 4,\n" +
                "\t\t\t\t\t\t\"cpu\": 3,\n" +
                "\t\t\t\t\t\t\"cost\": 1,\n" +
                "\t\t\t\t\t\t\"application\": 7\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 5,\n" +
                "\t\t\t\t\t\t\"cpu\": 5,\n" +
                "\t\t\t\t\t\t\"cost\": 1,\n" +
                "\t\t\t\t\t\t\"application\": 7\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"applications\": [{\n" +
                "\t\t\t\t\t\t\"index\": 6,\n" +
                "\t\t\t\t\t\t\"cost\": 1\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 7,\n" +
                "\t\t\t\t\t\t\"cost\": 1\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"machines\": [{\n" +
                "\t\t\t\t\t\t\"index\": 8,\n" +
                "\t\t\t\t\t\t\"cpu\": 8,\n" +
                "\t\t\t\t\t\t\"cost\": 0\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 9,\n" +
                "\t\t\t\t\t\t\"cpu\": 8,\n" +
                "\t\t\t\t\t\t\"cost\": 0\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"index\": 10,\n" +
                "\t\t\t\t\t\t\"cpu\": 8,\n" +
                "\t\t\t\t\t\t\"cost\": 0\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        System.out.println(inputJson);
        try {
            JsonNode jn = (new ObjectMapper()).readTree(inputJson).get("spec").get("data").get("json");
            System.out.println(jn);
            ms.solveBase(jn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
