package com.v1.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.v1.miBudget.entities.Item;
import com.v1.miBudget.utilities.HibernateUtilities;

public class ItemDAOImpl {
private static SessionFactory factory = HibernateUtilities.getSessionFactory();
	
	public ItemDAOImpl() {
    }
 
    public ItemDAOImpl(SessionFactory factory) {
    	ItemDAOImpl.factory = factory;
    }
    
    public static List<Item> getAllItems() {
    	List<Item> itemsList = new ArrayList<>();
    	try {
			System.out.println("\nAttempting to execute getAllItems query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> itemTableIds = session
					   .createNativeQuery("SELECT item_table_id FROM items").getResultList();
			List<?> itemIds = session
					   .createNativeQuery("SELECT item_id FROM items").getResultList();
			List<?> accessTokens = session
					   .createNativeQuery("SELECT access_token FROM items").getResultList();
			System.out.println("3 Querys executed!");
			//t.commit();
			session.close();
			if (itemTableIds.isEmpty()) {
				System.out.println("There are no items created.");
				return itemsList; // which is an empty list. so are the other two lists
			} else {
				Iterator<?> itemTableIdsIter = itemTableIds.iterator();
				Iterator<?> itemIdsIter = itemIds.iterator();
				Iterator<?> accessTokensIter = accessTokens.iterator();
				while (itemTableIdsIter.hasNext()) {
					Item item = new Item(Integer.parseInt(itemTableIdsIter.next().toString()), 
							itemIdsIter.next().toString(), accessTokensIter.next().toString());
					itemsList.add(item);
					System.out.println("item populted: " + item);
				}
				System.out.println("itemsList populated from ItemDAOImpl\n");
				return itemsList;
			}
		} catch (Exception e) {
			System.out.println("One of the queries has failed.");
			e.printStackTrace(System.out);
			
		} 
		return itemsList;
    }
    
    public static int getItemTableIdForItemId(String item_id) {
    	int tableId = 0; // bad
    	try {
    		System.out.println("\nAttempting to execute getItemTableIdForItemId query...");
    		Session session = factory.openSession();
    		Transaction t;
    		t = session.beginTransaction();
    		List<?> singleItemIdList = session
    				.createNativeQuery("SELECT item_table_id FROM items " +
    								   "WHERE item_id = '" + item_id + "'") // integer doesn't need quotes. string values do!
    				.getResultList();
    		System.out.println("singleItemIdList: " + singleItemIdList.get(0));
    		return (Integer) singleItemIdList.get(0);
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    	}
    	return 0;
    }
    
    public static List<String> getAllItemIds() {
    	List<String> itemIdsList = new ArrayList<>();
    	try {
			System.out.println("\nAttempting to execute getAllItemTableIds query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> itemIds = session
					   .createNativeQuery("SELECT item_id FROM items").getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (itemIds.size() == 0) {
				System.out.println("There are no items created.");
				return itemIdsList;
			} else {
				Iterator<?> iterator = itemIds.iterator();
				while (iterator.hasNext()) {
					String itemId = iterator.next().toString();
					itemIdsList.add(itemId);
					System.out.println("itemId: " + itemId);
				}
				System.out.println("itemTableitemIdIdList populated from ItemDAOImpl\n");
				return itemIdsList;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			
		} 
		return itemIdsList;
    }
    
    public static List<Integer> getAllItemTableIds() {
    	List<Integer> itemTableIdsList = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAllItemTableIds query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> tableIds = session
					   .createNativeQuery("SELECT id FROM items").getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (tableIds.size() == 0) {
				System.out.println("There are no items created.");
				return itemTableIdsList;
			} else {
				Iterator<?> iterator = tableIds.iterator();
				while (iterator.hasNext()) {
					Integer itemTableId = (Integer) iterator.next();
					itemTableIdsList.add(itemTableId);
					System.out.println("itemTableId: " + itemTableId);
				}
				System.out.println("itemTableIdList populated from ItemDAOImpl\n");
				return itemTableIdsList;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			
		} 
		return itemTableIdsList;
    }
    
    public static int addItemToDatabase(Item item) {
		try {
			System.out.println("\nAttempting to execute insert query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
//			session.createNativeQuery("INSERT INTO items ('item_id', 'access_token') " +
//									  "VALUES (" + item.getItemId() + "), (" + item.getAccessToken() + ")");
			session.save(item);
			System.out.println("Item saved using session.save(item)");
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return 0; // bad
		} finally {
		}
		
	}
}
