package autotest.entity;

import java.util.HashMap;

public class AirlinesData {

    public HashMap<String, String> getAirlines() {
        HashMap<String, String> airlines = new HashMap<>();
        airlines.put("UIA", "PS");
        airlines.put("Belavia - Belarusian Airlines", "B2");
        airlines.put("LOT Polish Airlines", "LO");
        airlines.put("Air Baltic", "BT");
        airlines.put("Air Fiji", "PC");
        airlines.put("FlyWoosh", "W2");
        airlines.put("Turkish Airlines", "TK");
        airlines.put("Ellinair", "EL");
        airlines.put("CSA Czech Airlines", "OK");
        airlines.put("Deutsche Lufthansa", "");
        airlines.put("Azerbaijan Airlines", "J2");
        airlines.put("Hahn Air Systems", "H1");
        return airlines;
    }
}
