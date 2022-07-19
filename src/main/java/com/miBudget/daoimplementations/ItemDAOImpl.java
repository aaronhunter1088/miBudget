package com.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.miBudget.main.MiBudgetState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.entities.Item;
import com.miBudget.entities.User;
import com.miBudget.entities.UserItemsObject;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public class ItemDAOImpl {
	
	private static Logger LOGGER =  LogManager.getLogger(ItemDAOImpl.class);
	private SessionFactory factory;

	@Autowired
	public ItemDAOImpl(SessionFactory factory) {
		this.factory = factory;
    }
	
	/**
	 * This method will return one bank item.
	 * @param itemTableId
	 * @return
	 */
	public Item getItem(int itemTableId) {
		Item item = new Item();
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to getItemFromUser query...");

			session = factory.openSession();
			t = session.beginTransaction();
			item = (Item) session
					.createNativeQuery("SELECT * FROM items " +
			                           "WHERE itemtableid = " + itemTableId)
					.addEntity(Item.class).getSingleResult();
			t.commit();
			session.close();
			LOGGER.info("Returning " + item);
			return item;
    	} catch (HibernateException e) {
    		LOGGER.error(e.getMessage());
    	}
    	return item;
	}
	
	public ArrayList<UserItemsObject> getAllUserItems(User user) {
		ArrayList<UserItemsObject> itemsList = new ArrayList<>();
		
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to getAllUserItems query...");

			session = factory.openSession();
			t = session.beginTransaction();
			itemsList = (ArrayList<UserItemsObject>) session
					.createNativeQuery("SELECT * FROM user_items WHERE user_id = " + user.getId())
					.addEntity(UserItemsObject.class)
					.getResultList();
			t.commit();
			session.close();
			LOGGER.info("Returning " + itemsList.size() + " UsersItemsObjects.");
			return itemsList; // TODO: if itemsList is empty, return an empty list
    	} catch (HibernateException e) {
    		LOGGER.error(e.getMessage());
    	}
    	return itemsList;
	}
    
	// TODO: change logic to incorporate user
	public Item getItemFromUser(String institutionId) {
		Item item = new Item();
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to get Item From User query...");

			session = factory.openSession();
			t = session.beginTransaction();
			item = (Item) session
					.createNativeQuery("SELECT * FROM items " +
			                           "WHERE institutionid = '" + institutionId + "'")
					.addEntity(Item.class).getSingleResult();
			t.commit();
			session.close();
			return item;
    	} catch (HibernateException e) {
    		LOGGER.error(e.getMessage());
    	}
    	return item;
	}
	
	public ArrayList<Item> getAllItems() {
    	ArrayList<Item> itemsList = new ArrayList<>();
    	
    	Session session = null;
    	Transaction t = null;
    	try {
			LOGGER.info("Attempting to execute getAllItems query...");

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
			LOGGER.info("4 Querys executed!");
			//t.commit();
			session.close();
			if (itemsList.isEmpty()) {
				LOGGER.info("There are no items created.");
				return itemsList; // which is an empty list
			} else {
				LOGGER.info("itemsList populated from ItemDAOImpl");
				return itemsList;
			}
		} catch (Exception e) {
			LOGGER.error("One of the queries has failed.");
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
		return itemsList;
    }
    
	public int getItemTableIdUsingInsId(String insId) {
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute get ItemTableId using the institutionId...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> singleItemTableIdList = session
    				.createNativeQuery("SELECT _id FROM items " +
    								   "WHERE institution_id = '" + insId + "'") // integer doesn't need quotes. string values do!
    				.getResultList();
    		LOGGER.info("singleItemTableIdList: " + singleItemTableIdList.get(0));
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
	
    public int getItemTableIdForItemId(String itemId) {
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute get ItemTableId For ItemId query...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> singleItemIdList = session
    				.createNativeQuery("SELECT _id FROM items " +
    								   "WHERE item_id = '" + itemId + "'") // integer doesn't need quotes. string values do!
    				.getResultList();
    		LOGGER.info("singleItemIdList: " + singleItemIdList.get(0));
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
    	
    	Session session = null;
    	Transaction t = null;
    	try {
			LOGGER.info("Attempting to execute getAllItemTableIds query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<?> itemIds = session
					   .createNativeQuery("SELECT item_id FROM items").getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			if (itemIds.size() == 0) {
				LOGGER.info("There are no items created.");
				return itemIdsList;
			} else {
				Iterator<?> iterator = itemIds.iterator();
				while (iterator.hasNext()) {
					String itemId = iterator.next().toString();
					itemIdsList.add(itemId);
					LOGGER.info("itemId: " + itemId);
				}
				LOGGER.info("itemTableitemIdIdList populated from ItemDAOImpl");
				return itemIdsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		} 
		return itemIdsList;
    }
    
    public List<Integer> getAllItemTableIds() {
    	List<Integer> itemTableIdsList = new ArrayList<>();
    	
    	Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute getAllItemTableIds query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<?> tableIds = session
					   .createNativeQuery("SELECT _id FROM items").getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			if (tableIds.size() == 0) {
				LOGGER.info("There are no items created.");
				return itemTableIdsList;
			} else {
				Iterator<?> iterator = tableIds.iterator();
				while (iterator.hasNext()) {
					Integer itemTableId = (Integer) iterator.next();
					itemTableIdsList.add(itemTableId);
					LOGGER.info("itemTableId: " + itemTableId);
				}
				LOGGER.info("itemTableIdList populated from ItemDAOImpl");
				return itemTableIdsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		} 
		return itemTableIdsList;
    }

	//TODO: Fix. Incorrect logic
    public ArrayList<String> getAccessTokensFromUser(User user) {
    	
    	Session session = null;
    	Transaction t = null;
    	ArrayList<String> accessTokens = new ArrayList<>();
    	try {
    		LOGGER.info("Attempting to execute getAccessTokens query");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		List<?> res = (List<?>) session.createNativeQuery("SELECT access_token " +
    											              "FROM items " +
    											              "WHERE userid = '" + user.getId() + "'").getResultList();
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
    	
    	Session session = null;
    	Transaction t = null;
    	String res = "";
    	try {
    		LOGGER.info("Attempting to execute getAccessToken query");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		res = session
    				.createNativeQuery("SELECT access_token " +
    						   		   "FROM items " +
    						   		   "WHERE institution_id = '" + institutionId + "'").getSingleResult().toString();
    	} catch (HibernateException e) {
    		t.rollback();
    	} finally {
    		session.close();
    	}
    	return res;
    }

    public int addItemToDatabase(Item item) {
    	
    	Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to insert item into the Items table...");

			session = factory.openSession();
			t = session.beginTransaction();
//			session.createNativeQuery("INSERT INTO items ('item_id', 'access_token') " +
//									  "VALUES (" + item.getItemId() + "), (" + item.getAccessToken() + ")");
			session.save(item);
			LOGGER.info("Item saved.");
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
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
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute deleteBankReferencesFromDatabase");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UsersInstitutionIdsObject " +
			    				"WHERE userId = " + user.getId() + " " +
								"AND institutionId = '" + item.getInstitutionId() + "'")
					.executeUpdate();
    		//org.hibernate.query.Query query = session.createQuery("DELETE users_institution_ids WHERE institution_id = :institution_id");
    		//query.setParameter("institution_id", item.getInsitutionId());
    		//int executeResult = query.executeUpdate();
    		LOGGER.info("... reference(s) deleted from users_institution_ids table...");
    		t.commit();
    		t = session.beginTransaction();
    		// Users_accounts table
    		// delete from users_accounts where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UserAccountObject " +
    							"WHERE userId = " + user.getId() + " " +
    							"AND item__id = " + item.get_id()).executeUpdate();
    		
    		LOGGER.info("... reference(s) deleted from users_accounts table...");
    		t.commit();
    		t = session.beginTransaction();
    		// Users_items table
    		// delete from users_items where item_table_id = 963;
    		session.createQuery("DELETE FROM UserItemsObject " +
			    				"WHERE item__id = " + item.get_id()).executeUpdate();
    		LOGGER.info("... reference(s) deleted from users_items table...");
//    		UsersItemsObject usersItemsObj = new UsersItemsObject(item.getItemTableId(), user.getId());
//    		session.delete(usersItemsObj);
    		t.commit();
    		t = session.beginTransaction();
    		// Items_accounts table
    		// delete from items_accounts where item_table_id = 963;
    		session.createQuery("DELETE FROM ItemAccountObject " + 
			    				"WHERE item__id = " + item.get_id()).executeUpdate();
    		LOGGER.info("... reference(s) deleted from items_accounts table...");
    		t.commit();
    		session.close();
    		LOGGER.info("4 delete queries executed.");
    		return 1;
    		
    	} catch (HibernateException e) {
    		LOGGER.error("Error performing hibernate action.");
    		LOGGER.error(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { LOGGER.error(err[i]); }
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
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
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute delete item query...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.delete(item);
    		session.getTransaction().commit();
    		session.close();
    		LOGGER.info("Item deleted from database.");
    		return 1;
    	} catch (HibernateException e) {
    		LOGGER.error("Error performing hibernate action.");
    		LOGGER.error(e.getMessage());
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
			return 0; // bad
    	}
    }
    
    
    public boolean deleteFromUsersInstitutionIds(Item item, User user) {
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to delete institution_id from Users_Institution_Ids table.");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UsersInstitutionIdsObject " +
			    				"WHERE userId = " + user.getId() + " " +
								"AND institutionId = '" + item.getInstitutionId() + "'").executeUpdate();
    		//org.hibernate.query.Query query = session.createQuery("DELETE users_institution_ids WHERE institution_id = :institution_id");
    		//query.setParameter("institution_id", item.getInsitutionId());
    		//int executeResult = query.executeUpdate();
    		LOGGER.info("... institution_id deleted from Users_Institution_Ids table...");
    		session.getTransaction().commit();
    		return true;
    	} catch (HibernateException e) {
    		LOGGER.error("Error performing hibernate action.");
    		LOGGER.error(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { LOGGER.error(err[i]); }
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	}
	}
    
    public boolean deleteFromUsersItems(Item item) {
    	
    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to delete UsersItemObject From UsersItems Table");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UserItemsObject " +
    				"WHERE item__id = " + item.get_id()).executeUpdate();
    		LOGGER.info("... reference deleted from UsersItems table...");
    		session.getTransaction().commit();
    		return true;
    	} catch (HibernateException e) {
    		LOGGER.error("Error performing hibernate action.");
    		LOGGER.error(e.getMessage());
    		StackTraceElement[] err = e.getStackTrace();
    		for(int i=0; i<err.length; i++) { LOGGER.error(err[i]); }
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	}
    }
    
    
    public Item createItemFromInstitutionId(String institutionId) {
    	try {
    		LOGGER.info("Attempting to get item using " + institutionId);
    		Item item = MiBudgetState.getMiBudgetDAOImpl().getItemFromDatabase(institutionId);
    		LOGGER.info("Item received");
    		return item;
    		
    	} catch (Exception e) {
    		LOGGER.error("Error creating the Item.");
			LOGGER.error(e.getMessage());
			return null; // bad
    	}
    }
    
    
    
    
    
    
    
    
    
}
