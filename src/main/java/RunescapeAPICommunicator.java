
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class RunescapeAPICommunicator {
    public final static String baseURL = "https://rsbuddy.com/exchange/summary.json";

    public static void main(String[] args) {

            try {        // Create a URL and open a connection
                int id = Integer.parseInt(args[0]);
                String url = baseURL;
                URL runescapeAPI = new URL(url);
                ItemBuilder(runescapeAPI, id);


            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void ItemBuilder(URL url, int id){
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            StringBuilder jsonString = new StringBuilder();
            Item thisItem = new Item();
            while((inputLine = in.readLine()) != null) {
                jsonString.append(inputLine);
                if(inputLine.contains("\"id\":" + id)){
                   String beggining = inputLine.substring(StringUtils.ordinalIndexOf(inputLine,"\"id\":" + id,1));
                   beggining = beggining.substring(0,beggining.indexOf("}"));
                   System.out.println(beggining);
                   thisItem.setName(beggining.substring(beggining.indexOf("name")+7,StringUtils.ordinalIndexOf(beggining,"\"",7)-2));
                   thisItem.setBuyPrice(beggining.substring(beggining.indexOf("buy_average")+13,StringUtils.ordinalIndexOf(beggining,"\"",13)-1));
                   thisItem.setBuyQuantity(beggining.substring(beggining.indexOf("buy_quantity")+14,StringUtils.ordinalIndexOf(beggining,"\"",15)-1));
                   thisItem.setSellPrice(beggining.substring(beggining.indexOf("sell_average")+14,StringUtils.ordinalIndexOf(beggining,"\"",17)-1));
                   thisItem.setSellQuantity(beggining.substring(beggining.indexOf("sell_quantity")+15,StringUtils.ordinalIndexOf(beggining,"\"",19)-1));
                }
            }
            thisItem.setId(id);
            in.close();
            System.out.println("Name: " + thisItem.getName());
            System.out.println("Buy Price: " + thisItem.getBuyPrice());
            System.out.println("Buy Quantity: " + thisItem.getBuyQuantity());
            System.out.println("Sell Price: " + thisItem.getSellPrice());
            System.out.println("Sell Quantity: " + thisItem.getSellQuantity());
            System.out.println("Margin: " + thisItem.getMargin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
