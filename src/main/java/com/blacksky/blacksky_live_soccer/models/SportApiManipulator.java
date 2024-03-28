package com.blacksky.blacksky_live_soccer.models;
import java.net.URI;
import java.net.http.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.json.*;

public class SportApiManipulator {
    private final String API_KEY = "cb61f3ef114f790f3fd62860abdaf2e1";
    private HttpRequest request;

    private int selectedLeagueApiId;
    private int selectedFixtureApiId;
    private String leagueName;

    private JSONArray listOfFixtureData = null;

    public SportApiManipulator() {
    }

    public int getSelectedLeagueApiId() {
        return selectedLeagueApiId;
    }

    public void setSelectedLeagueApiId(int selectedLeagueApiId) {
        this.selectedLeagueApiId = selectedLeagueApiId;
    }

    public int getSelectedFixtureApiId() {
        return selectedFixtureApiId;
    }

    public void setSelectedFixtureApiId(int selectedFixtureApiId) {
        this.selectedFixtureApiId = selectedFixtureApiId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getApiStatusPercent() {
        int apiStatusPercent = 404;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v3.football.api-sports.io/status"))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            apiStatusPercent = jsonObject.getJSONObject("response").getJSONObject("requests").getInt("current");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return apiStatusPercent;
    }

    public void fetchListOfFixtureData() {
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v3.football.api-sports.io/fixtures?league="+selectedLeagueApiId+"&season=2022&from=2023-05-06&to=2023-05-07&timezone=Asia/Yangon"))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            listOfFixtureData = jsonObject.getJSONArray("response");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public int getTotalFixture() {
        return listOfFixtureData.length();
    }

    public boolean isMatchLive(String matchShortStatus) {
        return matchShortStatus.equals("1H") || matchShortStatus.equals("HT" )|| matchShortStatus.equals("2H") || matchShortStatus.equals("ET") ||
                matchShortStatus.equals("BT") || matchShortStatus.equals("P") || matchShortStatus.equals("SUSP") || matchShortStatus.equals("INT");
    }

    private boolean isMatchFinished(String matchShortStatus) {
        return matchShortStatus.equals("FT") || matchShortStatus.equals("AET") || matchShortStatus.equals("PEN");
    }

    public Map<String,String> getFixtureDataFromArray(int index) {
        Map<String,String> fixtureDataMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject(listOfFixtureData.get(index).toString());
        long timeStampCode = jsonObject.getJSONObject("fixture").getLong("timestamp");
        Timestamp timestamp = new Timestamp(timeStampCode);
        Date date = new Date(timestamp.getTime()*1000);
        SimpleDateFormat sampleDate = new SimpleDateFormat("yyyy MMMM dd");
        SimpleDateFormat sampleTime = new SimpleDateFormat("hh : mm a");
        sampleDate.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Yangon")));
        sampleTime.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Yangon")));
        String formattedDate = sampleDate.format(date);
        String formattedTime = sampleTime.format(date);
        String matchShortStatus = jsonObject.getJSONObject("fixture").getJSONObject("status").getString("short");

        if ( isMatchLive(matchShortStatus) || isMatchFinished(matchShortStatus) ) {
            int homeTeamGoal = jsonObject.getJSONObject("goals").getInt("home");
            int awayTeamGoal = jsonObject.getJSONObject("goals").getInt("away");
            fixtureDataMap.put("homeTeamGoal",String.valueOf(homeTeamGoal));
            fixtureDataMap.put("awayTeamGoal",String.valueOf(awayTeamGoal));
            formattedTime = String.valueOf(homeTeamGoal)+" - "+String.valueOf(awayTeamGoal);
        }

        fixtureDataMap.put("fixtureId",String.valueOf(jsonObject.getJSONObject("fixture").getInt("id")));
        fixtureDataMap.put("matchDate",formattedDate);
        fixtureDataMap.put("matchTime",formattedTime);
        fixtureDataMap.put("matchShortStatus",matchShortStatus);
        fixtureDataMap.put("homeTeamId",String.valueOf(jsonObject.getJSONObject("teams").getJSONObject("home").getInt("id")));
        fixtureDataMap.put("homeTeamName",jsonObject.getJSONObject("teams").getJSONObject("home").getString("name"));
        fixtureDataMap.put("awayTeamId",String.valueOf(jsonObject.getJSONObject("teams").getJSONObject("away").getInt("id")));
        fixtureDataMap.put("awayTeamName",jsonObject.getJSONObject("teams").getJSONObject("away").getString("name"));

        return fixtureDataMap;
    }
}
