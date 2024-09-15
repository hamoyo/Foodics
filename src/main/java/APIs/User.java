package APIs;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.util.Properties;
public class User {
    String userAPI = "";
public User(){
    Properties properties = new Properties();
    try {
        properties.load(this.getClass().getResourceAsStream("/project.properties"));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    userAPI = properties.getProperty("apiURL") + "/api/users/";
}

    String UserAPIBody(Profile profile) {
        JSONObject myRequestBody = new JSONObject();
        try {
            myRequestBody.put("id", profile.id);
            myRequestBody.put("first_name", profile.first_name);
            myRequestBody.put("last_name", profile.last_name);
            myRequestBody.put("email", profile.email);
            myRequestBody.put("avatar", profile.avatar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myRequestBody.toString();
    }

    public JSONObject CreateUser(Profile profile) {
        return getJSONResponse(userAPI, UserAPIBody(profile), "POST");
    }
    public JSONObject GetUser(String id) {
        return getJSONResponse(userAPI + id, null, "GET");
    }
    public JSONObject ListUsers() {
        return getJSONResponse(userAPI, null, "GET");
    }
    public JSONObject UpdateUser(Profile profile) {
        return getJSONResponse(userAPI + profile.id, UserAPIBody(profile), "UPDATE");
    }
    public JSONObject DeleteUser(String id) {
        return getJSONResponse(userAPI + id, null, "DELETE");
    }




    JSONObject getJSONResponse(String API, String RequestBody, String accessMethod) {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        String responseData = null;
        JSONObject jsonResponse = null;
            try {
                if(accessMethod == "POST")
                    response = client.newCall(setPostRequestHeaders(API, setAPIBody(RequestBody))).execute();
                else if(accessMethod == "GET")
                    response = client.newCall(setGetRequestHeaders(API)).execute();
                else if(accessMethod == "UPDATE")
                    response = client.newCall(setUpdateRequestHeaders(API, setAPIBody(RequestBody))).execute();
                else if(accessMethod == "DELETE")
                    response = client.newCall(setDeleteRequestHeaders(API)).execute();

                responseData = response.body().string();
                if(responseData == "") responseData = "{}";
                jsonResponse = new JSONObject(responseData);
            } catch (IOException e) {
                Logger.getLogger(this.getClass()).error(e.getMessage());
                e.printStackTrace();
            }
        jsonResponse.put("code", response.code());
        jsonResponse.put("errorMessage", response.message());
        return jsonResponse;
    }

    RequestBody setAPIBody(String APIbody) {
        return RequestBody.create(MediaType.parse("application/json"), APIbody);
    }

    Request setPostRequestHeaders(String APIURL, RequestBody body) {

        return new Request.Builder()
                .url(APIURL)
                .post(body)

                .addHeader("Content-Type", "application/json")
                .build();
    }

    Request setGetRequestHeaders(String APIURL) {
        return new Request.Builder()
                .url(APIURL)
                .get()

                .addHeader("Content-Type", "application/json")
                .build();
    }

    Request setUpdateRequestHeaders(String APIURL, RequestBody body) {
        return new Request.Builder()
                .url(APIURL)
                .put(body)

                .addHeader("Content-Type", "application/json")
                .build();
    }

    Request setDeleteRequestHeaders(String APIURL) {
        return new Request.Builder()
                .url(APIURL)
                .delete()

                .addHeader("Content-Type", "application/json")
                .build();
    }
}
