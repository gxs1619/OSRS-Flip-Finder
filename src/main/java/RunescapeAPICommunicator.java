
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Written by: Griffin Seibold
 * 1/30/2019
 * This class is in charge of interacting with the API I am getting
 * the data from and sending it to the SQLPopulater to insert into the
 * database of items. A key method in this class is ItemBuilder, which when
 * given an id of an item and an SQLPopulater object, can refresh the data
 * on any item in the database.
 */
public class RunescapeAPICommunicator {
    public final static String baseURL = "https://rsbuddy.com/exchange/summary.json";
    public static StringBuilder apiData = new StringBuilder();


    public void populateAll(SQLPopulater sql) throws MalformedURLException {
        URL url = new URL(baseURL);
        grabData(url);
        for (int i = 2; i <= 13190; i++) {
            ItemBuilder(i, sql);
        }
        System.out.println("done");
    }

    /**
     * This method pulls information out from the large string
     * of data so it can be put into the database of items.
     */
    public void ItemBuilder(int id, SQLPopulater sql) {
        String inputLine = apiData.toString();
        Item thisItem = new Item();
        if (inputLine.contains("\"id\":" + id + ",")) {
            String beggining = inputLine.substring(StringUtils.ordinalIndexOf(inputLine, "\"id\":" + id, 1));
            beggining = beggining.substring(0, beggining.indexOf("}"));
            thisItem.setId(id);
            thisItem.setName(beggining.substring(beggining.indexOf("name") + 7, StringUtils.ordinalIndexOf(beggining, "\"", 7) - 2));
            thisItem.setBuyPrice(beggining.substring(beggining.indexOf("buy_average") + 13, StringUtils.ordinalIndexOf(beggining, "\"", 13) - 1));
            thisItem.setBuyQuantity(beggining.substring(beggining.indexOf("buy_quantity") + 14, StringUtils.ordinalIndexOf(beggining, "\"", 15) - 1));
            thisItem.setSellPrice(beggining.substring(beggining.indexOf("sell_average") + 14, StringUtils.ordinalIndexOf(beggining, "\"", 17) - 1));
            thisItem.setSellQuantity(beggining.substring(beggining.indexOf("sell_quantity") + 15, StringUtils.ordinalIndexOf(beggining, "\"", 19) - 1));
            thisItem.setLastUpdated(LocalDateTime.now().toString());
            sql.insert(thisItem.getId(), thisItem.getName(), Integer.parseInt(thisItem.getSellPrice()), Integer.parseInt(thisItem.getSellQuantity()), Integer.parseInt(thisItem.getBuyPrice()), Integer.parseInt(thisItem.getBuyQuantity()), thisItem.getMargin(), thisItem.getLastUpdated());
        }
    }

    /**
     * Grab data is essential to saving time contacting the API, this is
     * in a separate method so that we only grab data once and then parse
     * it to find the information we are looking for. This is better than
     * contacting the api every time a new request comes in.
     */
    public void grabData(URL url) {
        BufferedReader in;
        StringBuilder jsonString = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonString.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error grabbing data from API.");
        }
        apiData = jsonString;
    }
}
