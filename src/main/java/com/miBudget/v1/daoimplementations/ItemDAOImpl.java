package com.miBudget.v1.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UsersItemsObject;
import com.miBudget.v1.utilities.HibernateUtilities;

@SuppressWarnings("unchecked")
public class ItemDAOImpl {
	
	public ItemDAOImpl() {
    }
	
	/**
	 * This method will return one bank item.
	 * @param itemTableId
	 * @return
	 */
	public Item getItem(int itemTableId) {
		Item item = new Item();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to getItemFromUser query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			item = (Item) session
					.createNativeQuery("SELECT * FROM items " +
			                           "WHERE item_table_id = " + itemTableId)
					.addEntity(Item.class).getSingleResult();
			t.commit();
			session.close();
			System.out.println("Returning " + item);
			return item;
    	} catch (HibernateException e) {
    		System.out.println(e.getMessage());
    	}
    	return item;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UsersItemsObject> getAllUserItems(User user) {
		ArrayList<UsersItemsObject> itemsList = new ArrayList<>();
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to getAllUserItems query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			itemsList = (ArrayList<UsersItemsObject>) session
					.createNativeQuery("SELECT * FROM users_items WHERE user_id = " + user.getId())
					.addEntity(UsersItemsObject.class).getResultList();
			t.commit();
			session.close();
			System.out.println("Returning " + itemsList.size() + " UsersItemsObjects.");
			return itemsList;
    	} catch (HibernateException e) {
    		System.out.println(e.getMessage());
    	}
    	return itemsList;
	}
    
	// TODO: change logic to incorporate user
	public Item getItemFromUser(String institutionId) {
		Item item = new Item();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to get Item From User query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			item = (Item) session
					.createNativeQuery("SELECT * FROM items " +
			                           "WHERE institution_id = '" + institutionId + "'")
					.addEntity(Item.class).getSingleResult();
			t.commit();
			session.close();
			return item;
    	} catch (HibernateException e) {
    		System.out.println(e.getMessage());
    	}
    	return item;
	}
	
	public ArrayList<Item> getAllItems() {
    	ArrayList<Item> itemsList = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute getAllItems query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
//			List<?> itemTableIds = session
//					   .createNativeQuery("SELECT item_table_id FROM items").getResultList();
//			List<?> institutionIds = session
//					   .createNativeQuery("SELECT institution_id FROM items").getResultList();
//			List<?> itemIds = session
//					   .createNativeQuery("SELECT item_id FROM items").getResultList();
//			List<?> accessTokens = session
//					   .createNativeQuery("SELECT access_token FROM items").getResultList();
			itemsList = (ArrayList<Item>) session.createNativeQuery("SELECT * FROM items")
												 .addEntity(Item.class).getResultList();
			t.commit();
			System.out.println("4 Querys executed!");
			//t.commit();
			session.close();
			if (itemsList.isEmpty()) {
				System.out.println("There are no items created.");
				return itemsList; // which is an empty list
			} else {
				System.out.println("itemsList populated from ItemDAOImpl\n");
				return itemsList;
			}
		} catch (Exception e) {
			System.out.println("One of the queries has failed.");
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
		return itemsList;
    }
    
	public static int getItemTableIdUsingInsId(String insId) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute get ItemTableId using the institutionId...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> singleItemTableIdList = session
    				.createNativeQuery("SELECT item_table_id FROM items " +
    								   "WHERE institution_id = '" + insId + "'") // integer doesn't need quotes. string values do!
    				.getResultList();
    		System.out.println("singleItemTableIdList: " + singleItemTableIdList.get(0));
    		t.commit();
    		session.close();
    		return (Integer) singleItemTableIdList.get(0);
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    		t.rollback();
    		session.close();
    	}
    	return 0;
    }
	
    public int getItemTableIdForItemId(String item_id) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute get ItemTableId For ItemId query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> singleItemIdList = session
    				.createNativeQuery("SELECT item_table_id FROM items " +
    								   "WHERE item_id = '" + item_id + "'") // integer doesn't need quotes. string values do!
    				.getResultList();
    		System.out.println("singleItemIdList: " + singleItemIdList.get(0));
    		t.commit();
    		session.close();
    		return (Integer) singleItemIdList.get(0);
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    		t.rollback();
    		session.close();
    	}
    	return 0;
    }
    
    public List<String> getAllItemIds() {
    	List<String> itemIdsList = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute getAllItemTableIds query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
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
			t.rollback();
			session.close();
		} 
		return itemIdsList;
    }
    
    public List<Integer> getAllItemTableIds() {
    	List<Integer> itemTableIdsList = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute getAllItemTableIds query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
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
			t.rollback();
			session.close();
		} 
		return itemTableIdsList;
    }
    
    public ArrayList<String> getAccessTokensFromUser(User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	ArrayList<String> accessTokens = new ArrayList<>();
    	try {
    		System.out.println("\nAttempting to execute getAccessTokens query");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> res = (List<?>) session.createNativeQuery("SELECT access_token " +
    											              "FROM items " +
    											              "WHERE user_id = '" + user.getId() + "'").getResultList();
    		t.commit();
    		session.close();
    		res.forEach(str -> {
    			accessTokens.add((String) str);
    		});
    		return accessTokens;
    	} catch (HibernateException e) {
    		
    	}
    	return accessTokens;
    }
    
    public String getAccessToken(String institutionId) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAccessToken query");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		String res = (String) session.createNativeQuery("SELECT access_token " +
    											   "FROM items " +
    											   "WHERE institution_id = '" + institutionId + "'").getSingleResult();
    		return res;
    	} catch (HibernateException e) {
    		
    	}
    	return "";
    }

    public int addItemToDatabase(Item item) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to insert item into the Items table...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
//			session.createNativeQuery("INSERT INTO items ('item_id', 'access_token') " +
//									  "VALUES (" + item.getItemId() + "), (" + item.getAccessToken() + ")");
			session.save(item);
			System.out.println("Item saved.");
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return 0; // bad
	}
    
    /**
     * Currently deletes from :
     * User_Account
     * User_items
     * Items_Accounts
     * Users_InstitutionIds
     * @param item
     * @param user
     * @return
     */
    // TODO: Note: Keep this todo as reference that when we maybe add a new table for whatever logic and 
    // we need to delete a reference to an Item, we need to add that logic here. 
    public int deleteBankReferencesFromDatabase(Item item, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute deleteBankReferencesFromDatabase");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UsersInstitutionIdsObject " +
			    				"WHERE user_id = " + user.getId() + " " +
								"AND institution_id = '" + item.getInstitutionId() + "'").executeUpdate();
    		//org.hibernate.query.Query query = session.createQuery("DELETE users_institution_ids WHERE institution_id = :institution_id");
    		//query.setParameter("institution_id", item.getInsitutionId());
    		//int executeResult = query.executeUpdate();
    		System.out.println("... reference(s) deleted from users_institution_ids table...");
    		t.commit();
    		t = session.beginTransaction();
    		// Users_accounts table
    		// delete from users_accounts where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UserAccountObject " +
    							"WHERE user_id = " + user.getId() + " " +
    							"AND item_table_id = " + item.getItemTableId()).executeUpdate();
    		
    		System.out.println("... reference(s) deleted from users_accounts table...");
    		t.commit();
    		t = session.beginTransaction();
    		// Users_items table
    		// delete from users_items where item_table_id = 963;
    		session.createQuery("DELETE FROM UsersItemsObject " + 
			    				"WHERE item_table_id = " + item.getItemTableId()).executeUpdate();
    		System.out.println("... reference(s) deleted from users_items table...");
//    		UsersItemsObject usersItemsObj = new UsersItemsObject(item.getItemTableId(), user.getId());
//    		session.delete(usersItemsObj);
    		t.commit();
    		t = session.beginTransaction();
    		// Items_accounts table
    		// delete from items_accounts where item_table_id = 963;
    		session.createQuery("DELETE FROM ItemAccountObject " + 
			    				"WHERE item_table_id = " + item.getItemTableId()).executeUpdate();
    		System.out.println("... reference(s) deleted from items_accounts table...");
    		t.commit();
    		session.close();
    		System.out.println("4 delete queries executed.");
    		return 1;
    		
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { System.out.println(err[i]); }
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
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
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete item query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.delete(item);
    		session.getTransaction().commit();
    		session.close();
    		System.out.println("Item deleted from database.");
    		return 1;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return 0; // bad
    	}
    }
    
    
    public boolean deleteFromUsersInstitutionIds(Item item, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to delete institution_id from Users_Institution_Ids table.");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM users_institution_ids " +
			    				"WHERE user_id = " + user.getId() + " " +
								"AND institution_id = '" + item.getInstitutionId() + "'").executeUpdate();
    		//org.hibernate.query.Query query = session.createQuery("DELETE users_institution_ids WHERE institution_id = :institution_id");
    		//query.setParameter("institution_id", item.getInsitutionId());
    		//int executeResult = query.executeUpdate();
    		System.out.println("... institution_id deleted from Users_Institution_Ids table...");
    		session.getTransaction().commit();
    		return true;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { System.out.println(err[i]); }
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	}
	}
    
    public boolean deleteFromUsersItems(Item item) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to delete UsersItemObject From UsersItems Table");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UsersItemsObject " + 
    				"WHERE item_table_id = " + item.getItemTableId()).executeUpdate();
    		System.out.println("... reference deleted from UsersItems table...");
    		session.getTransaction().commit();
    		return true;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { System.out.println(err[i]); }
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	}
    }
    
    
    public Item createItemFromInstitutionId(String institutionId) {
    	try {
    		System.out.println("\nAttempting to get item using " + institutionId);
    		MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl();
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
