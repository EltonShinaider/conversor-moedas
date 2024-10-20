package shinaider.elton.conversor.moedas;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class ApiImportarMoedas {

    // Substitua por sua chave de API válida
    private static final String API_KEY = "93f27fba7b2537bc4c8f131a";

    // Obtém a taxa de câmbio com base na moeda base
    public static ExchangeRate getExchangeRate(String baseCode) {
        baseCode = baseCode.toUpperCase();

        // Endpoint para obter as taxas de câmbio mais recentes em relação à moeda base
        String address = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + baseCode;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(address)).build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Não foi possível fazer a requisição à API", e);
        }

        String json = response.body();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // Mapeia o JSON para o objeto ExchangeRate
        ExchangeRate exchangeRate = gson.fromJson(json, ExchangeRate.class);

        if (Objects.equals(exchangeRate.getResult(), "success")) {
            return exchangeRate;
        } else {
            throw new RuntimeException("A requisição à API falhou: " + exchangeRate.getErrorType());
        }
    }
}