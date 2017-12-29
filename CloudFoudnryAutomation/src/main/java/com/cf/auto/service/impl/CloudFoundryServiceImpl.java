package com.cf.auto.service.impl;

import com.cf.auto.service.ICloudFoundryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dipen on 12/27/2017.
 */
@Service
public class CloudFoundryServiceImpl implements ICloudFoundryService {

    private String UaaURL;
    private String poolURl;
    @Value(value = "$(spring.userLogin.username)")
    private String usename;
    @Value(value = "${spring.userLogin.password}")
    private String password;


    @Override
    public void setUaaURL(String uaaURL) {
        UaaURL = uaaURL;
    }

    @Override
    public void setPoolUrl(String poolUrl) {
        poolURl = poolUrl;
    }

    @Override
    public String checkServiceByName(String serviceName) throws IOException {
        String token = getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String path = "/v2/spaces/"+spaceGuid+"service_instance";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(poolURl+path)
                .queryParam("return_user_provided_service_instances","true")
                .queryParam("q","name:"+serviceName)
                .queryParam("inline-relations-depth",1);

        String url = uriBuilder.build().encode().toUri().toString();
        String serviceInstance = invokeCfApi(token,url,HttpMethod.GET);

        JSONObject jsonObject = new JSONObject(serviceName);
        JSONArray jsonArray = jsonObject.getJSONArray("resources");
        if(jsonArray == null || jsonArray.length() < 1)
        {
            return "no";
        }else{
            jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            return jsonObject.get("guid").toString();
        }
    }

    private String getSpaceGuid(String token) {
        String path = "/v2/spaces";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(poolURl+path);
        String url = uriBuilder.build().encode().toUri().toString();

        String space = invokeCfApi(token,url,HttpMethod.GET);
        JSONObject jsonObject = new JSONObject(space);
        JSONArray jsonArray = jsonObject.getJSONArray("resources");
        jsonObject = jsonArray.getJSONObject(0);
        jsonObject = jsonObject.getJSONObject("metadata");

        return jsonObject.get("guid").toString();
    }

    @Override
    public void deleteServiceIfExist(String serviceName) throws IOException {
        String serviceGuid = checkServiceByName(serviceName);
        if(!serviceGuid.equals("no")){
            deleteServiceInstacne(getAuthTokenOnly(),serviceGuid);
        }
    }

    public String deleteServiceInstacne(String token, String serviceGuid) {
        String path = "/v2/service_instances/"+serviceGuid;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(poolURl + path)
                .queryParam("accept_incomplete","true")
                .queryParam("async",true);
        String url = uriBuilder.build().encode().toUri().toString();
        return invokeCfApi(token,url,HttpMethod.DELETE);
    }

    public String auth(){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Baisc Y2Y6");
        headers.add("Cache-Control","no-cache");
        headers.add("Content-Type", "application/json");
        headers.add("Accept-Charset","utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL)
                .queryParam("garnt_type","password")
                .queryParam("usename","<username>")
                .queryParam("password","<password>");
        String url = uriBuilder.build().encode().toUri().toString();
        ResponseEntity<String> response = rt.exchange(url,HttpMethod.POST,entity,String.class);
        return response.getBody();

    }

    public String getAuthTokenOnly() throws IOException {
        String token = auth();
        Map<String, String> authMap =  null;
        ObjectMapper mapper = new ObjectMapper();

        authMap = mapper.readValue(token, new TypeReference<Map<String, String>>(){});
        return authMap.get("token_type")+ " " + authMap.get("access_token");
    }

    public String invokeCfApi(String token, String url, HttpMethod method){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Cookie","");
        headers.add("Content-Type", "application/json");
        headers.add("Accept-Charset","utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(url,method,entity,String.class);
        return response.toString();

    }

    public String invokeCfApi(String token, String url, HttpMethod method, String payload){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Cookie","");
        headers.add("Content-Type", "application/json");
        headers.add("Accept-Charset","utf-8");
        HttpEntity<?> entity = new HttpEntity<>(payload,headers);

        ResponseEntity<String> response = rt.exchange(url,method,entity,String.class);
        return response.toString();
    }

    private String getServicePlans(String token, String serviceGuid) {
        String path = "/v2/service_plans?q=service_guid:"+serviceGuid;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(poolURl+path);
        String url =uriBuilder.build().encode().toUri().toString();
        String servicePlanGuid = invokeCfApi(token,url,HttpMethod.GET);
        JSONObject jsonObject = new JSONObject(servicePlanGuid);
        JSONArray jsonArray = jsonObject.getJSONArray("resources");

        return jsonArray.toString();
    }

    private String getServiceGuid(String token, String spaceGuid, String serviceLabel) {
        String path = "/v2/spaces/"+spaceGuid+"/services?q=label:"+serviceLabel;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(poolURl+path);
        String url =uriBuilder.build().encode().toUri().toString();
        String serviceGuid = invokeCfApi(token,url,HttpMethod.GET);

        JSONObject jsonObject = new JSONObject(serviceGuid);
        JSONArray jsonArray = jsonObject.getJSONArray("resources");
        jsonObject = jsonArray.getJSONObject(0);
        jsonObject = jsonObject.getJSONObject("metadata");

        return jsonObject.get("guid").toString();
    }


    @Override
    public String createUserProvidedService(String serviceName, String parameters) throws IOException, ParseException {
        String token = getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String path = "/v2/user_provided_service_instance";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path);
        String url = uriBuilder.build().encode().toUri().toString();

        JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);
        JSONObject serviceJson = new JSONObject();
        serviceJson.put("name",serviceName);
        serviceJson.put("space_guid",spaceGuid);
        serviceJson.put("credentials",dependency.get("credentials"));
        serviceJson.put("syslog_drain_url", dependency.get("syslog_drain_url"));
        serviceJson.put("route_service_url",dependency.get("route_service_url"));
        return invokeCfApi(token,url,HttpMethod.POST, serviceJson.toString());
    }


    @Override
    public String createConfigServer(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("standard".equals(servicePlan)){
            String path = "/v2/service_instances";

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }
        return response;
    }

    @Override
    public String createExternalDependency(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("unstructured".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }
        return response;
    }

    @Override
    public String createCircuitBreaker(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("standard".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());

        }
        return response;
    }

    @Override
    public String createDynatrace(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("v6_1".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());

        }
        return response;
    }

    @Override
    public String createAppdynamics(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("v4_x".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());

        }
        return response;
    }

    @Override
    public String createGaiaStorageService(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("default".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }
        return response;
    }

    @Override
    public String createMariaDbMultiTenant(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        String path = "/v2/service_instances";
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        jsonObject = jsonObject.getJSONObject("metadata");
        String servicePlanGuid = jsonObject.get("guid").toString();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                .queryParam("accepts_incomplete","true");
        String url = uriBuilder.build().encode().toUri().toString();

        JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

        JSONObject serviceJson = new JSONObject();
        serviceJson.put("name",serviceName);
        serviceJson.put("space_guid",spaceGuid);
        serviceJson.put("service_plan_guid",servicePlanGuid);
        serviceJson.put("parameters",dependency);
        response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());

        return response;
    }

    @Override
    public String createWilyIntroscope(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("v10_x".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }
        return response;
    }

    @Override
    public String createServiceRegister(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);

        if("standard".equals(servicePlan)){
            String path = "/v2/service_instances";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }
        return response;
    }

    @Override
    public String createSSO(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);
        String path = "/v2/service_instances";

        if("siteminder-default".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else if("janus".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(1);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else if("siteminder-custom".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(1);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else{
            response = "service unavailable";
        }
        return response;
    }

    @Override
    public String createQPid(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException {
        String token= getAuthTokenOnly();
        String spaceGuid = getSpaceGuid(token);
        String response = "";
        String result = getServicePlans(token,getServiceGuid(token,spaceGuid,serviceLabel));
        JSONArray jsonArray = new JSONArray(result);
        String path = "/v2/service_instances";

        if("VirtualHost - Multi-tenant".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else if("VirtualHost - Dedicated VM".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(1);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else if("HA Single Pool - Dedicated Cluster".equals(servicePlan)){
            JSONObject jsonObject = jsonArray.getJSONObject(2);
            jsonObject = jsonObject.getJSONObject("metadata");
            String servicePlanGuid = jsonObject.get("guid").toString();

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(UaaURL + path)
                    .queryParam("accepts_incomplete","true");
            String url = uriBuilder.build().encode().toUri().toString();

            JSONObject dependency = (JSONObject) new JSONParser().parse(parameters);

            JSONObject serviceJson = new JSONObject();
            serviceJson.put("name",serviceName);
            serviceJson.put("space_guid",spaceGuid);
            serviceJson.put("service_plan_guid",servicePlanGuid);
            serviceJson.put("parameters",dependency);
            response =  invokeCfApi(token,url,HttpMethod.POST,serviceJson.toString());
        }else{
            response = "service unavailable";
        }
        return response;
    }


}
