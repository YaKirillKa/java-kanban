package org.kamenkov.java_kanban;

import org.kamenkov.java_kanban.exceptions.ManagerSaveException;
import org.kamenkov.java_kanban.servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String apiToken;
    private final URI url;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        apiToken = getToken(url);
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        URI putUrl = URI.create(url + KVServer.SAVE_URL + "/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder().uri(putUrl).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new ManagerSaveException("Manager was not saved. " + response.statusCode() + ": " + response.body());
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI loadUrl = URI.create(url + KVServer.LOAD_URL + "/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder().uri(loadUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        }
        return null;
    }

    private String getToken(URI url) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        URI registerUrl = URI.create(url + KVServer.REGISTER_URL);
        HttpRequest request = HttpRequest.newBuilder().uri(registerUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        }
        return null;
    }
}
