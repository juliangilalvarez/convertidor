import java.util.Map;

public class CurrencyConverter {
    private final Map<String, Double> rates;

    /**
     * Constructor para inicializar las tasas de cambio.
     *
     * @param rates Mapa de tasas de cambio.
     */
    public CurrencyConverter(Map<String, Double> rates) {
        this.rates = rates;
    }

    /**
     * Convierte un monto de una moneda a otra.
     *
     * @param amount Cantidad a convertir.
     * @param from   Código de moneda de origen.
     * @param to     Código de moneda de destino.
     * @return Monto convertido.
     */
    public double convert(double amount, String from, String to) {
        // Verificar si las monedas están disponibles en el mapa de tasas
        if (!rates.containsKey(from) || !rates.containsKey(to)) {
            throw new IllegalArgumentException("Tasas de cambio no disponibles para las monedas especificadas.");
        }

        // Obtener la tasa de la moneda de origen y destino
        double fromRate = rates.get(from); // Tasa de la moneda origen
        double toRate = rates.get(to);     // Tasa de la moneda destino

        // Si la moneda de origen no es USD, primero convertimos a USD
        double amountInUsd = amount;
        if (!from.equals("USD")) {
            amountInUsd = amount / fromRate;  // Convertimos a USD

        }

        // Luego, si la moneda de destino no es USD, convertimos de USD a la moneda de destino
        double convertedAmount = amountInUsd;
        if (!to.equals("USD")) {
            convertedAmount = amountInUsd * toRate;  // Convertimos a la moneda de destino
        }

        return convertedAmount;
    }
}
