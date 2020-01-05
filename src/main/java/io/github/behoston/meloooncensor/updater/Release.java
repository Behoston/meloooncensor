package io.github.behoston.meloooncensor.updater;

import org.json.JSONArray;
import org.json.JSONObject;

public class Release {

    String version;
    String description;
    String summary;
    String downloadUrl;
    String releaseUrl;
    boolean isPreRelease;

    private Release () {}

    public String getVersion () {
        return version;
    }

    public String getDescription () {
        return description;
    }

    public String getSummary () {
        return summary;
    }

    public boolean hasSummary () {
        return summary != null && summary.length() > 0;
    }

    public String getDownloadUrl () {
        return downloadUrl;
    }

    public String getReleaseUrl () {
        return releaseUrl;
    }

    public boolean isPreRelease () {
        return isPreRelease;
    }

    public static Release from (JSONObject json) {
        Release release = new Release();
        release.version = json.getString("tag_name");
        release.description = json.getString("body");
        release.summary = json.getString("name");
        JSONArray assets = json.getJSONArray("assets");
        JSONObject jar = assets.getJSONObject(0);
        release.downloadUrl = jar.getString("browser_download_url");
        release.releaseUrl = json.getString("html_url");
        release.isPreRelease = json.getBoolean("prerelease");
        return release;
    }

}
