import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        // URL de la API

        String apiUrl = "https://v6.exchangerate-api.com/v6/15a1f3d7aad1c2f1a29f71a3/latest/USD";

        // Crear HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Crear la solicitud GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar el estado HTTP
            if (response.statusCode() == 200) {
                // Procesar la respuesta JSON con Gson
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                // Extraer el objeto conversion_rates
                JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

                // Monedas a filtrar (con MXN)
                List<String> selectedCurrencies = List.of("ARS", "BOB", "BRL", "CLP", "COP", "USD", "MXN");

                // Crear un mapa para las tasas seleccionadas
                Map<String, Double> filteredRates = new HashMap<>();
                selectedCurrencies.forEach(currency -> {
                    if (conversionRates.has(currency)) {
                        filteredRates.put(currency, conversionRates.get(currency).getAsDouble());
                    }
                });

                // Crear una instancia de la clase CurrencyConverter
                CurrencyConverter converter = new CurrencyConverter(filteredRates);


                // Menú interactivo con ciclo while
                Scanner scanner = new Scanner(System.in);
                boolean running = true;

                while (running) {
                    // Mostrar el menú sin la opción para MXN
                    System.out.println("\nSeleccione la moneda de origen para convertir a MXN:");
                    System.out.println("1. ARS - Peso argentino");
                    System.out.println("2. BOB - Boliviano");
                    System.out.println("3. BRL - Real brasileño");
                    System.out.println("4. CLP - Peso chileno");
                    System.out.println("5. COP - Peso colombiano");
                    System.out.println("6. USD - Dólar estadounidense");
                    System.out.println("7. Salir");

                    // Leer la opción del usuario
                    System.out.print("Ingrese el número de la opción: ");
                    int option = scanner.nextInt();

                    // Procesar la opción seleccionada
                    switch (option) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            String fromCurrency = selectedCurrencies.get(option - 1); // Moneda de origen

                            // Usar try-catch para asegurarse de que el valor ingresado es numérico
                            double amount = 0.0;
                            boolean validInput = false;
                            while (!validInput) {
                                System.out.print("Ingrese la cantidad a convertir: ");
                                try {
                                    amount = scanner.nextDouble();  // Intentar leer el valor
                                    validInput = true;  // Si no ocurre excepción, la entrada es válida
                                } catch (Exception e) {
                                    System.out.println("Por favor, ingrese un número válido.");
                                    scanner.next();  // Limpiar el buffer de entrada para seguir pidiendo el valor
                                }
                            }

                            // Convertir a MXN
                            double convertedAmount = converter.convert(amount, fromCurrency, "MXN");
                            System.out.printf("\n%.2f %s equivalen a %.2f MXN%n", amount, fromCurrency, convertedAmount);
                            break;
                        case 7:
                            System.out.println("Saliendo del programa...");
                            running = false; // Salir del ciclo
                            break;
                        default:
                            System.out.println("Opción inválida, por favor seleccione una opción válida.");
                            break;
                    }
                }

            } else {
                System.out.println("Error: HTTP " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
