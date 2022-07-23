package com.miBudget.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.miBudget.entities.Location;
import com.miBudget.entities.Transaction;
 
/**
 * The purpose of this class is to take a json object and convert it to
 * a String or likewise.
 * @author michaelball
 *
 */
public class ResponseParser {

	private static Logger LOGGER = null;
	private static Scanner input = new Scanner(System.in);
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(ResponseParser.class);
	}
	public static void main(String[] args) {
		String jsonInput = null;
		try {
			jsonInput = args[0];
		} catch (Exception e) {
			System.out.println("Enter the string you wish to parse");
			jsonInput = input.nextLine();
			input.nextLine();
		}
		
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		try { jsonArray = (JSONArray) jsonParser.parse(jsonInput); }
		catch (Exception e) { e.printStackTrace(System.out); }
		ArrayList<Transaction> productList = new ArrayList<>();
		JSONObject jsonObject = null;
		for(int i = 0; i < jsonArray.size(); i++) {
			try {
				String accountId = (String) jsonObject.get("account_id");
				String name = (String) jsonObject.get("name");
				int amount = (Integer) jsonObject.get("amount");
				List<String> categories = (ArrayList<String>) jsonObject.get("category"); // where category is a List of Strings
				// TODO: parse location to create location object
				Location location = (Location) jsonObject.get("location"); // this can be a Plaid object location
				
				jsonObject = (JSONObject) jsonParser.parse(jsonArray.get(i).toString());
				Transaction t = new Transaction(accountId, name, amount, location, categories);
				productList.add(t);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace(System.out);
			}
		}
		productList.forEach(t -> {
			LOGGER.info(t);
		});
	}

}

/*
{
    "account_id": "NovaPypokoFavDn7wZ5diO0NgamjNYcR1OPzQ",
    "account_owner": null,
    "amount": 6.26,
    "category": [
        "Food and Drink",
        "Restaurants"
    ],
    "category_id": "13005000",
    "date": "2019-05-18",
    "location": {
        "address": null,
        "city": null,
        "lat": null,
        "lon": null,
        "state": null,
        "store_number": null,
        "zip": null
    },
    "name": "POS DEBIT CORPDRIVE CAFE PLANO TX 0NN0 0501",
    "payment_meta": {
        "by_order_of": null,
        "payee": null,
        "payer": null,
        "payment_method": null,
        "payment_processor": null,
        "ppd_id": null,
        "reason": null,
        "reference_number": null
    },
    "pending": true,
    "pending_transaction_id": null,
    "transaction_id": "YQjE1JeQqQc6LbNEkRV9cRboJJpawmHQ5Akn7",
    "transaction_type": "place"
}
*/