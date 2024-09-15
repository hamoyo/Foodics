package SmokeTests;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import APIs.*;
import io.restassured.*;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;

import org.testng.log4testng.Logger;

public class APIs {
    Profile profile = new Profile();
    @Test(priority = 1)
    public void TC1_Ensure_User_IsCreated_Successfully() {
        User userApi = new User();
        Reporter.log("Initiating CreateNewUser API.", true);
        JSONObject response = userApi.CreateUser(profile);
        Reporter.log("Verifying API response.", true);
        Assert.assertEquals(
                response.getInt("code"), 201
                , "CreatingUserAPI returns an error: \n" + response.getString("errorMessage"));
        Reporter.log("Validate new user details are saved successfully.", true);
        Assert.assertEquals(response.getString("id"), profile.id,"User ID is not correct.");
        Assert.assertEquals(response.getString("first_name"), profile.first_name,"User FirstName is not correct.");
        Assert.assertEquals(response.getString("last_name"), profile.last_name,"User LastName is not correct.");
        Assert.assertEquals(response.getString("email"), profile.email,"User Email is not correct.");
        Assert.assertEquals(response.getString("avatar"), "profile.avatar","User Avatar is not correct.");
    }

    @Test(priority = 2)
    public void TC2_Ensure_Retrieving_User() {
        User userApi = new User();
        Reporter.log("Initiating RetrievingUser API.", true);
        JSONObject response = userApi.GetUser(profile.id);
        Reporter.log("Verifying API response.", true);
        Assert.assertEquals(
                response.getInt("code"), 200
                , "RetrieveUserAPI returns an error: \n" + response.getString("errorMessage"));
        Reporter.log("Validate user details are retrieved correctly.", true);
        Assert.assertEquals(response.getJSONObject("data").getString("email"), "george.bluth@reqres.in");
    }

    @Test(priority = 3)
    public void TC3_Updating_User() {
        User userApi = new User();
        profile.first_name = "Haytham";
        profile.last_name = "Yousif";
        Reporter.log("Initiating UpdateUser API.", true);
        JSONObject response = userApi.UpdateUser(profile);
        System.out.println(response);
        Reporter.log("Verifying API response.", true);
        Assert.assertEquals(
                response.getInt("code"), 200
                , "UpdatingUserAPI returns an error: \n" + response.getString("errorMessage"));
        Reporter.log("Validate user details are updated successfully.", true);
        Assert.assertEquals(response.getString("first_name"), profile.first_name);
        Assert.assertEquals(response.getString("last_name"), profile.last_name);
        Assert.assertEquals(response.getString("email"), profile.email);
    }

    @Test(priority = 4)
    public void TC4_Deleting_User() {
        User userApi = new User();
        Reporter.log("Initiating DeleteUser API.", true);
        JSONObject response = userApi.DeleteUser(profile.id);
        Reporter.log("Verifying API response.", true);
        Reporter.log("Validate user is deleted successfully.", true);
        Assert.assertEquals(
                response.getInt("code"), 204
                , "DeletingUserAPI returns an error: \n" + response.getString("errorMessage"));
    }

    @Test(priority = 5)
    public void TC5_List_Users() {
        User userApi = new User();
        Reporter.log("Initiating ListAllUsers API.", true);
        JSONObject response = userApi.ListUsers();
        System.out.println(response);
        Reporter.log("Verifying API response.", true);
        Assert.assertEquals(
                response.getInt("code"), 200
                , "ListingUsersAPI returns an error: \n" + response.getString("errorMessage"));
        Reporter.log("Validate users list is paginated.", true);
        Assert.assertEquals(response.getJSONArray("data").length()
                , response.getInt("per_page"));
    }
}
