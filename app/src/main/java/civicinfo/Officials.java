package civicinfo;

import java.io.Serializable;
import java.util.ArrayList;

import officialsdata.Channel;

public class Officials implements Serializable {
    public String name , title;
    private String address;
    public String party;
    private String phones;
    private String urls;
    private String emails;
    public ArrayList<Channel> channels;
    public String photoUrl;

    public Officials(){
        name = "";
        title= "";
        address = "";
        party = "";
        phones = "";
        urls = "";
        emails = "";
        channels = new ArrayList<>();
        photoUrl = "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmails() {
        return emails;
    }

}
