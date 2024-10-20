package shinaider.elton.conversor.main;

import shinaider.elton.conversor.moedas.ApiImportarMoedas;
import shinaider.elton.conversor.moedas.ExchangeRate;

import java.text.Normalizer;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String[] MOEDAS = {
            "AED", "ARS", "BRL", "CNY", "EUR", "JPY", "USD"
    };

    private static final Map<String, String> NOME_MOEDAS = Map.of(
            "AED", "Dirham",
            "ARS", "Peso Argentino",
            "BRL", "Real",
            "CNY", "Yuan",
            "EUR", "Euro",
            "JPY", "Iene",
            "USD", "Dólar Americano"
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-Vindo ao conversor de moedas!");
        exibirMoedas();

        while (true) {
            String moedaBase = solicitarMoeda(scanner, "Digite a moeda de origem (ex: USD): ");
            String moedaDestino = solicitarMoeda(scanner, "Digite a moeda para conversão (ex: BRL): ");
            double valorParaConverter = solicitarValor(scanner);

            try {
                ExchangeRate exchangeRate = ApiImportarMoedas.getExchangeRate(moedaBase);
                realizarConversao(exchangeRate, moedaDestino, valorParaConverter);
            } catch (Exception e) {
                System.out.println("Erro ao processar a requisição: " + e.getMessage());
            }

            if (!continuarConversao(scanner)) {
                break;
            }
        }

        System.out.println("Obrigado! Aplicação concluída.");
        scanner.close();
    }

    private static void exibirMoedas() {
        System.out.println("Estas são as moedas suportadas:");
        for (String sigla : MOEDAS) {
            System.out.println(sigla + " - " + NOME_MOEDAS.get(sigla));
        }
    }

    private static String solicitarMoeda(Scanner scanner, String mensagem) {
        String moeda;
        while (true) {
            System.out.print(mensagem);
            moeda = scanner.nextLine().toUpperCase();
            if (isMoedaValida(moeda)) {
                return moeda;
            } else {
                System.out.println("Moeda inválida. Por favor, tente novamente.");
            }
        }
    }

    private static double solicitarValor(Scanner scanner) {
        while (true) {
            System.out.print("Digite o valor que deseja converter: ");
            if (scanner.hasNextDouble()) {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("Valor inválido. Por favor, tente novamente.");
                scanner.nextLine();
            }
        }
    }

    private static void realizarConversao(ExchangeRate exchangeRate, String moedaDestino, double valorParaConverter) {
        Map<String, Double> taxasConversao = exchangeRate.getConversionRates();

        if (taxasConversao.containsKey(moedaDestino)) {
            double taxaConversao = taxasConversao.get(moedaDestino);
            double valorConvertido = valorParaConverter * taxaConversao;
            System.out.println("A taxa de conversão de " + exchangeRate.getBaseCode() + " para " + moedaDestino + " é: " + String.format("%.2f", taxaConversao).replace('.', ','));
            System.out.println("O valor convertido é: " + String.format("%.2f", valorConvertido).replace('.', ','));
        } else {
            System.out.println("A moeda para conversão não é válida.");
        }
    }

    private static boolean continuarConversao(Scanner scanner) {
        String resposta;
        do {
            System.out.print("Deseja realizar outra conversão? Digite 'sim' ou 'não': ");
            resposta = scanner.nextLine().trim().toLowerCase();

            // Remover acentos da resposta
            resposta = Normalizer.normalize(resposta, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

            if (!resposta.equals("sim") && !resposta.equals("nao")) {
                System.out.println("Resposta inválida. Por favor, digite 'sim' ou 'não'.");
            }
        } while (!resposta.equals("sim") && !resposta.equals("nao"));

        return resposta.equals("sim");
    }

    private static boolean isMoedaValida(String moeda) {
        for (String moedaSuportada : MOEDAS) {
            if (moedaSuportada.equals(moeda)) {
                return true;
            }
        }
        return false;
    }
}