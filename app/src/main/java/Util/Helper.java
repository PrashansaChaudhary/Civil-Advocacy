package Util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import civicinfo.CivilAdvocacy;
import civicinfo.NormalizedInput;
import civicinfo.Offices;
import civicinfo.Officials;
import officialsdata.Channel;

public class Helper {

    final static String API_KEY = "AIzaSyDnuDGjhv_n3vxq7n86mXvH0wrO81dtYuU";
    private static final String TAG = "Helper";

    // For Checking Network Connection is Available or Not
    public static Boolean isNetworkConnected(Activity activity) {
        ConnectivityManager connectivityManager = activity.getSystemService(ConnectivityManager.class);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network == null && network.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static String buildURL(String url, String address) {
        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("key", API_KEY);
        buildURL.appendQueryParameter("address", address);
        String urlToUse = buildURL.build().toString();
        return urlToUse;
    }

    public static NormalizedInput normalizedInput(String data) {
        try {
            JSONObject normalizedInput = new JSONObject(data);
            normalizedInput = normalizedInput.getJSONObject("normalizedInput");
            String line1 = normalizedInput.getString("line1");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");

            String locationText = (city.equals("") ? "" : city + ", ") + (zip.equals("") ? state : state + ", ") + (zip.equals("") ? "" : zip);

            return new NormalizedInput(line1, city, state, zip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Parsing Data from API
    public static ArrayList<Officials> parseJSON(String data) {
        ArrayList<Officials> tempList = new ArrayList<>();
        Officials official;
        try {
            JSONObject temp = new JSONObject(data);
            JSONArray offices = (JSONArray) temp.get("offices");
            JSONArray officials = (JSONArray) temp.get("officials");
            Log.d(TAG, "parseJSON: bp: Length of Array: " + offices.length());

            for (int i = 0; i < offices.length(); i++) {
                JSONObject office = (JSONObject) offices.get(i);
                JSONObject officialIndices = (JSONObject) offices.get(i);
                JSONArray index = officialIndices.getJSONArray("officialIndices");

                for (int j = 0; j < index.length(); j++) {
                    Officials official_intermediate;
                    JSONObject officialData_JSON = (JSONObject) officials.get(index.getInt(j));
                    official_intermediate = fetchOfficialDetails(officialData_JSON);
                    official = official_intermediate;
                    official.setTitle(office.getString("name"));                    // Setting Title here because the above statement would nake the title field of official object to null string
                    tempList.add(official);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | parseJSON: bp: " + e);
        }
        return tempList;
    }

    private static Officials fetchOfficialDetails(JSONObject officialData_JSON) {
        Officials temp = new Officials();
        temp.setName(getNameFromData(officialData_JSON));
        temp.setParty(getPartyFromData(officialData_JSON));
        temp.setAddress(getAddressFromData(officialData_JSON));
        temp.setUrls(getURLFromData(officialData_JSON));
        temp.setEmails(getEmailFromData(officialData_JSON));
        temp.setPhones(getPhoneFromData(officialData_JSON));
        temp.setPhotoUrl(getPhotoURLfromData(officialData_JSON));
        temp.setChannels(getChannelsFromData(officialData_JSON));
        return temp;
    }

    private static String getNameFromData(JSONObject officialData_json) {
        String name = "";
        try {
            name = officialData_json.getString("name");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | fetchOfficialDetails: bp: " + e);
        }

        return name;
    }

    private static String getAddressFromData(JSONObject officialData_json) {
        String finalAddress = "";
        try {
            JSONArray addresses = (JSONArray) officialData_json.get("address");
            JSONObject address = (JSONObject) addresses.get(0);
            String line1 = getLine1FromData(address);
            String line2 = getLine2FromData(address);
            String city = getCityFromData(address);
            String state = getStateFromData(address);
            String zip = getZIPFromData(address);
            finalAddress = line1 + ", " + (line2.equals("") ? line2 + "" : line2 + ", ") + city + ", " + state + ", " + zip;
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getAddressFromData: " + e);
        }

        return finalAddress;
    }

    private static String getLine1FromData(JSONObject address) {
        String line1 = "";
        try {
            line1 = address.getString("line1");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getLine1FromData: " + e);
        }
        return line1;
    }

    private static String getLine2FromData(JSONObject address) {
        String line2 = "";
        try {
            line2 = address.getString("line2");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getLine2FromData: " + e);
        }
        return line2;
    }

    private static String getCityFromData(JSONObject address) {
        String city = "";
        try {
            city = address.getString("city");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getCityFromData: " + e);
        }
        return city;
    }

    private static String getStateFromData(JSONObject address) {
        String state = "";
        try {
            state = address.getString("state");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getStateFromData: " + e);
        }
        return state;
    }

    private static String getZIPFromData(JSONObject address) {
        String zip = "";
        try {
            zip = address.getString("zip");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getZIPFromData: " + e);
        }
        return zip;
    }

    private static String getURLFromData(JSONObject officialData_json) {
        String URL = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("urls");
            URL = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getURLFromData: " + e);
        }

        return URL;
    }

    private static String getEmailFromData(JSONObject officialData_json) {
        String email = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("emails");
            email = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getEmailFromData: " + e);
        }

        return email.toLowerCase();
    }

    private static String getPhoneFromData(JSONObject officialData_json) {
        String phone = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("phones");
            phone = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getPhoneFromData: " + e);
        }

        return phone;
    }

    private static String getPartyFromData(JSONObject officialData_json) {
        String party = "";
        try {
            party = officialData_json.getString("party");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | fetchOfficialDetails: bp: " + e);
        }

        return " (" + party +")";
    }

    private static ArrayList<Channel> getChannelsFromData(JSONObject officialData_json) {
        ArrayList<Channel> tempList = new ArrayList<>();
        Channel temp;

        try {
            JSONArray channels = (JSONArray) officialData_json.get("channels");
            for (int i = 0; i < channels.length(); i++) {
                JSONObject channel = (JSONObject) channels.get(i);
                temp = new Channel(channel.getString("type"), channel.getString("id"));
                tempList.add(temp);
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getAddressFromData: " + e);
        }
        return tempList;
    }

    private static String getPhotoURLfromData(JSONObject officialData_json) {
        String photoURL = "";

        try {
            photoURL = officialData_json.getString("photoUrl");
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION | getEmailFromData: " + e);
        }

        return photoURL;
    }

}