package shinaider.elton.conversor.moedas;

import java.util.Map;

public class ExchangeRate {

    private String result;
    private String baseCode;
    private Map<String, Double> conversionRates;
    private String errorType;

    // Getters
    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public String getErrorType() {
        return errorType;
    }
}