package com.v1.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.User;
import com.v1.miBudget.utilities.HibernateUtilities;

public class ItemDAOImpl {
	private SessionFactory factory = HibernateUtilities.getSessionFactory();
	private MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
	
	public ItemDAOImpl() {
    }
    
    public List<Item> getAllItems() {
    	List<Item> itemsList = new ArrayList<>();
    	try {
			System.out.println("\nAttempting to execute getAllItems query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> itemTableIds = session
					   .createNativeQuery("SELECT item_table_id FROM items").getResultList();
			List<?> institutionIds = session
					   .createNativeQuery("SELECT institution_id FROM items").getResultList();
			List<?> itemIds = session
					   .createNativeQuery("SELECT item_id FROM items").getResultList();
			List<?> accessTokens = session
					   .createNativeQuery("SELECT access_token FROM items").getResultList();
			System.out.println("4 Querys executed!");
			//t.commit();
			session.close();
			if (itemTableIds.isEmpty()) {
				System.out.println("There are no items created.");
				return itemsList; // which is an empty list. so are the other two lists
			} else {
				Iterator<?> itemTableIdsIter = itemTableIds.iterator();
				Iterator<?> institutionIdsIter = institutionIds.iterator();
				Iterator<?> itemIdsIter = itemIds.iterator();
				Iterator<?> accessTokensIter = accessTokens.iterator();
				while (itemTableIdsIter.hasNext()) {
					Item item = new Item(Integer.parseInt(itemTableIdsIter.next().toString()), institutionIdsIter.next().toString(), 
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
    
    public int getItemTableIdForItemId(String item_id) {
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
    
    public List<String> getAllItemIds() {
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
    
    public List<Integer> getAllItemTableIds() {
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
    
    public int addItemToDatabase(Item item) {
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
    
    public int deleteBankReferencesFromDatabase(Item item, User user) {
    	try {
    		System.out.println("\nAttempting to execute deleteBankReferencesFromDatabase");
    		
    		Session session = factory.openSession();
    		session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM users_institution_ids " +
    						    "WHERE institution_id = '" + item.getInsitutionId() + "'").executeUpdate();
    		//org.hibernate.query.Query query = session.createQuery("DELETE users_institution_ids WHERE institution_id = :institution_id");
    		//query.setParameter("institution_id", item.getInsitutionId());
    		//int executeResult = query.executeUpdate();
    		System.out.println("... reference(s) deleted from users_institution_ids table...");
    		session.getTransaction().commit();
    		session.beginTransaction();
    		// Users_accounts table
    		// delete from users_accounts where user_id = 20 and institution_id = 'ins_3';
    		session.createNativeQuery("DELETE FROM users_accounts " +
    							"WHERE insitution_id = '" + item.getInsitutionId() + "'").executeUpdate();
    		System.out.println("... reference(s) deleted from users_accounts table...");
    		session.getTransaction().commit();
    		session.beginTransaction();
    		// Users_items table
    		// delete from users_items where item_table_id = 963;
    		session.createNativeQuery("DELETE FROM users_items " + 
    							"WHERE item_table_id = " + item.getItemId()).executeUpdate();
    		System.out.println("... reference(s) deleted from users_items table...");
    		session.getTransaction().commit();
    		session.beginTransaction();
    		// Items_accounts table
    		// delete from items_accounts where item_table_id = 963;
    		session.createNativeQuery("DELETE FROM items_accounts " + 
    							"WHERE item_table_id = " + item.getItemId()).executeUpdate();
    		System.out.println("... reference(s) deleted from items_accounts table...");
    		session.getTransaction().commit();
    		session.close();
    		System.out.println("4 delete queries executed.");
    		return 1;
    		
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { System.out.println(err[i]); }
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return 0; // bad
    	}
    }
    
    /**
     * This method will delete the Bank Item and all references of the accounts
     * from the database and all tables where applicable.
     * @param item
     * @return
     */
    public int deleteItemFromDatabase(Item item) {
    	try {
    		System.out.println("\nAttempting to execute delete item query...");
    		Session session = factory.openSession();
    		Transaction t;
    		t = session.beginTransaction();
    		Item itemFromSession = miBudgetDAOImpl.getItemFromDatabase(item.getInsitutionId());
    		System.out.println("itemFromSession: " + itemFromSession.toString());
    		System.out.println("itemPassedIn: " + item.toString());
    		session.delete(itemFromSession);
    		session.getTransaction().commit();
    		
    		return 1;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return 0; // bad
    	}
    }
    
    public Item createItemFromInstitutionId(String institutionId) {
    	try {
    		System.out.println("\nAttempting to get item using " + institutionId);
    		Item item = miBudgetDAOImpl.getItemFromDatabase(institutionId);
    		System.out.println("Item received");
    		return item;
    		
    	} catch (Exception e) {
    		System.out.println("Error creating the Item.");
			System.out.println(e.getMessage());
			return null; // bad
    	}
    }
    
    
    
    
    
    
    
    
    
}
