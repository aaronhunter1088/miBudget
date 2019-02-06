package com.miBudget.v1.daoimplementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Category;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UserAccountObject;
import com.miBudget.v1.entities.UsersItemsObject;
import com.miBudget.v1.utilities.HibernateUtilities;


public class MiBudgetDAOImpl {
	
	public MiBudgetDAOImpl() {
    }
	
	// TODO: implement logic to return all categories saved
	/**
	 * This method returns all the categories that have been saved to MiBudget
	 * @return
	 */
	public ArrayList<Category> getAllCategories() {
		return null;
	}
	
	/**
	 * This method returns all the categories saved for a particular user
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Category> getAllCategories(User user) {
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		ArrayList<Category> categoriesFromDB = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAllCategories for " + user.getFirstName() + " " + user.getLastName());
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			categoriesFromDB = (ArrayList<Category>) session
					.createNativeQuery("SELECT * FROM users_categories "
					   				 + "WHERE user_id = " + user.getId())
					.addEntity(Category.class).getResultList();
			System.out.println("Query executed. categories list populated from MiBudgetDAOImpl.");
			System.out.println("Returning " + categoriesFromDB.size() + " categories for " + user.getFirstName() + " " + user.getLastName());
			session.getTransaction().commit();
			session.close();
			if (categoriesFromDB.size() > 0) {
				for ( Object category : categoriesFromDB) {
					System.out.println(category);
				}
			} else {
				System.out.println(user.getFirstName() + " " + user.getLastName() + " hasn't saved any categories yet. Getting default list");
				categoriesFromDB = user.getCategories();
			}
			return categoriesFromDB;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return categoriesFromDB;
	}
	
	/**
	 * This method returns the accounts and its institution id.
	 * TODO: Note that this uses UserAccountObject. This is correct. Change implementations
	 * elsewhere to use UserAccountObject and not the Account object.
	 * @param user
	 * @return
	 */
	public HashMap<String, ArrayList<UserAccountObject>> getAcctsAndInstutionIdMap(User user) {
		
		HashMap<String, ArrayList<UserAccountObject>> mapToReturn = new HashMap<>();
		ArrayList<String> insIdsList = new ArrayList<>();
		ArrayList<UserAccountObject> acctsList = new ArrayList<>();
		
		try {
			insIdsList = getInstitutionIdsFromUser(user);
			for (String id : insIdsList) {
				int itemTableId = ItemDAOImpl.getItemTableIdUsingInsId(id);
				acctsList = AccountDAOImpl.getAllUserAccountObjectsFromUserAndItemTableId(user, itemTableId);
				mapToReturn.put(id, acctsList);
				System.out.println("id");
				acctsList.forEach(acct -> {
					System.out.println(acct);
				});
				System.out.println();
			}
			
		} catch (Exception e) {
    		System.out.println("An exception occurred!");
			System.out.println(e.getMessage());
		}
		return mapToReturn;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getInstitutionIdsFromUser(User user) {
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		ArrayList<String> institutionIds = new ArrayList<>();
		try {
			System.out.println("\nAttempting to get all the institution_ids for " + user.getFirstName() + " " + user.getLastName());
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			institutionIds = (ArrayList<String>) session
					   .createNativeQuery("SELECT institution_id FROM users_institution_ids "
					   					+ "WHERE user_id = " + user.getId()).getResultList();
			System.out.println("Query executed. institutionIds list populated from MiBudgetDAOImpl.");
			System.out.println("Returning " + institutionIds.size() + " institution ids for " + user.getFirstName() + " " + user.getLastName());
			session.getTransaction().commit();
			session.close();
			for ( String id : institutionIds) {
				System.out.println("institution_id: " + id);
			}
			return institutionIds;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return institutionIds;
	}
    
    @SuppressWarnings("unchecked")
	public ArrayList<String> getAllInstitutionIdsFromUser(User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		ArrayList<String> institutionIds = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAllInstitutionIds query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			institutionIds = (ArrayList<String>) session
					   .createNativeQuery("SELECT institution_id FROM users_institution_ids "
					   					+ "WHERE user_id = " + user.getId()).getResultList();
			System.out.println("Query executed. institutionIds list populated from MiBudgetDAOImpl.");
			System.out.println("Returning " + institutionIds.size() + " institution ids for " + user.getFirstName() + " " + user.getLastName());
			session.getTransaction().commit();
			session.close();
			for ( String id : institutionIds) {
				System.out.println("institution_id: " + id);
			}
			return institutionIds;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return institutionIds;
    }
	
    // SQL Implementation
    public int addInstitutionIdToDatabase(String ins_id, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute insert institution_id query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			session.createNativeQuery("INSERT INTO users_institution_ids (institution_id, user_id) " +
									  "VALUES ('" + ins_id + "', " + user.getId() + ")").executeUpdate(); // recall Strings need quotes around them. numbers don't
			t.commit();
			System.out.println("Institution saved"); // "Institution save using session.save(item)"
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
    
    // POJO Implementation
    public int addUserToDatabase(User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.save(user);
    		t.commit();
    		for (Category c : user.getCategories()) {
    			t = session.beginTransaction();
    			session.save(c);
    			session.getTransaction().commit();
    		}
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
    
	public List<String> getAllCellphones() {
		List<String> cellphones = new ArrayList<>();
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> cellphonesFromDB = session
					   .createNativeQuery("SELECT Cellphone FROM Users")
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			Iterator<?> iterator = cellphonesFromDB.iterator();
			while (iterator.hasNext()) {
				String cell = iterator.next().toString();
				cellphones.add(cell);
				System.out.println("cellphone: " + cell);
			}
			System.out.println("cellphones list populated from MiBudgetDAOImpl\n");
			return cellphones;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
		return cellphones;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		List<User> usersNoAccounts = new ArrayList<>();
		SessionFactory factory = null;
		Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllUsers query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			usersNoAccounts = (List<User>) session.createNativeQuery("SELECT * FROM users")
										.addEntity(User.class).getResultList();
			session.getTransaction().commit();
			session.close();
			System.out.println("Query executed! " + usersNoAccounts.size() + " users retrieved.");
			int size = usersNoAccounts.size();
			AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
			// Populate users accountId's if they have any
			for (User user : usersNoAccounts) {
				ArrayList<String> accountIds = (ArrayList<String>) accountDAOImpl.getAccountIdsFromUser(user);
				user.setAccountIds(accountIds);
				user.createCategories();
				System.out.println("userFromDB: " + user);
				users.add(user);
			}
		} catch (HibernateException e) {
    		System.out.println("Error during retrieval of next account or adding account to accounts list.");
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			t.rollback();
			session.close();
    	} 
    	return users;
	}

//	public List<User> getAllUsers() {
//		List<User> users = new ArrayList<>();
//		SessionFactory factory = null;
//    	Session session = null;
//    	Transaction t = null;
//    	AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
//		try {
//			System.out.println("\nAttempting to execute getAllUsers query...");
//			factory = HibernateUtilities.getSessionFactory();
//			session = factory.openSession();
//			t = session.beginTransaction();
//			List<?> idsFromDB = session
//					   				.createNativeQuery("SELECT id FROM users")
//					   				.getResultList();
//			List<?> firstnamesFromDB = session
//											.createNativeQuery("SELECT first_name FROM users")
//											.getResultList();
//			List<?> lastnamesFromDB = session
//										   .createNativeQuery("SELECT last_name FROM users")
//										   .getResultList();
//			List<?> cellphonesFromDB = session
//									  .createNativeQuery("SELECT cellphone FROM users")
//									  .getResultList();
//			List<?> passwordsFromDB = session
//					  					   .createNativeQuery("SELECT password FROM users")
//					  					   .getResultList();
//			List<?> emailsFromDB = session
//										.createNativeQuery("SELECT email FROM users")
//										.getResultList();
//			System.out.println("6 Queries executed!");
//			session.getTransaction().commit();
//			int size = idsFromDB.size();
//			for (int i = 0; i < size; i++) {
//				ArrayList<String> accountIds = (ArrayList<String>) accountDAOImpl.getAccountIdsFromUser((Integer)idsFromDB.get(i));
//				User user = new User((Integer)idsFromDB.get(i),
//									 firstnamesFromDB.get(i).toString(),
//									 lastnamesFromDB.get(i).toString(),
//									 cellphonesFromDB.get(i).toString(),
//									 passwordsFromDB.get(i).toString(),
//									 emailsFromDB.get(i).toString(),
//									 accountIds );
//				System.out.println("userFromDB: " + user);
//				users.add(user);
//			}
//			
//			System.out.println("all users from miBudget retrieved.");
//			session.close();
//		} catch (HibernateException e) { 
//			System.out.println("Error opening a session using the factory.");
//			System.out.println(e.getMessage());
//			StackTraceElement[] ste = e.getStackTrace();
//			for (int i=0; i<ste.length; i++) { System.out.println(ste[i]); }
//			System.out.println("Returning null.");
//			t.rollback();
//			session.close();
//		} catch (Exception e) {
//			System.out.println("Error connecting to DB");
//			System.out.println(e.getMessage());
//			StackTraceElement[] ste = e.getStackTrace();
//			for (int i=0; i<ste.length; i++) { System.out.println(ste[i]); }
//			System.out.println("Returning null.");
//			t.rollback();
//			session.close();
//		}
//		return users;
//	}

	
	
	// Logic for Items we create
	
	public int addItemToUsersItemsTable(int itemTableId, User user) {
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute addItemToUsersItemsTable query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			int user_id = user.getId();
			t = session.beginTransaction();
			UsersItemsObject uiObj = new UsersItemsObject(itemTableId, user_id);
//			session.createNativeQuery("INSERT INTO users_items ('item_id', 'user_id') " +
//									  "VALUES (" + item_id + ", " + user_id + ")").executeUpdate();
//			session.createNativeQuery("UPDATE users " + 
//									  "SET Items = '" + item_id + "' " +
//									  "WHERE Id = " + user.getId()).executeUpdate();
//			update users set Items = "works" where Id = 2;
			
//			session.saveOrUpdate(user);
			System.out.println(uiObj);
			session.save(uiObj);
			t.commit();
			session.close();
			System.out.println(uiObj + " added to UsersItems table for: " + user.getFirstName() + ", Id: " + user.getId());
			return 1;
		} catch (NullPointerException e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
		return 0;
	}
	
	public List<Item> getAllItemIds() {
		List<Item> items = new ArrayList<>();
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> idsFromDB = session
					   				.createNativeQuery("SELECT item_id FROM items")
					   				.getResultList();
			List<?> institutionIdsFromDB = session
								    .createNativeQuery("SELECT institution_id FROM items")
								    .getResultList();
			List<?> accessTokensFromDB = session
									.createNativeQuery("SELECT access_token FROM items")
									.getResultList();
			
			System.out.println("3 Queries executed!");
			t.commit();
			session.close();
			int size = idsFromDB.size();
			for (int i = 0; i < size; i++) {
				Item item = new Item(idsFromDB.get(i).toString(), accessTokensFromDB.get(i).toString(), institutionIdsFromDB.get(i).toString());
				System.out.println("item: " + item);
				items.add(item);
			}
			
			System.out.println("all items from MiBudgetDAOImpl");
			return items;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
		return null;
	}

	public List<String> getAllItemIdsFromUser(User user) {
		List<String> itemIds = new ArrayList<>();
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute query getAllItemsFromUser...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> item_idsFromDB = session
					   .createNativeQuery("SELECT item_id FROM users_items " +
							   			  "WHERE user_id = " + user.getId())
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			//System.out.println("the results string: " + item_idsFromDB);
			if (item_idsFromDB.size() == 0) {
				System.out.println("no item_ids for this user yet...");
				return itemIds;
			}
			if (item_idsFromDB.size() > 0 || !item_idsFromDB.equals(null)) {
				
				Iterator<?> iterator = item_idsFromDB.iterator();
				while (iterator.hasNext()) {
					String itemID = iterator.next().toString();
					itemIds.add(itemID);
					System.out.println("item_id: " + itemID);
				}
//				String[] item_idsFromDBArr = item_idsFromDB.split(",");
//				for(String s : item_idsFromDBArr) {
//					String itemID = s;
//					itemIds.add(itemID);
//					System.out.println("item_id: " + itemID);
//				}
				System.out.println("item_ids list populated from MiBudgetDAOImpl");
				return itemIds;
			} 
		} catch (NullPointerException e) {
			System.out.println("Error connecting to DB");
			System.out.println(e);
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
		return null;
	}

	/**
	 * TODO: need to update this method. it is not good. in the sense that when i have two users in the program
	 * it will create Items for the first institution_id it finds. this will be wrong if we have multiple users.
	 * Hence, the addition of user specific tables. I may need to get all items from the item table, but only the 
	 * ones that have this institution_id 
	 * @param institution_id
	 * @return
	 */
	public Item getItemFromDatabase(String institution_id) {
		System.out.println("\nAttempting to getItemFromDatabase using institutionId query...");
		Item item = null;
		SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> itemTableId = session.createNativeQuery("SELECT item_table_id " +
													   "FROM items " +
													   "WHERE institution_id = '" + institution_id + "'").getResultList();
			List<?> itemId = session.createNativeQuery("SELECT item_id " + 
													   "FROM items " +
													   "WHERE institution_id = '" + institution_id + "'").getResultList();
			List<?> accessToken = session.createNativeQuery("SELECT access_token " + 
															"FROM items " +
															"WHERE institution_id = '" + institution_id + "'").getResultList();
//			Item item = session.createNativeQuery("SELECT * FROM " + 
//												  "WHERE ")
			item = new Item(Integer.parseInt(itemTableId.get(0).toString()), itemId.get(0).toString(), 
					accessToken.get(0).toString(), institution_id);
			System.out.println("retrieving item: " + item);
			t.commit();
			session.close();
			return item;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
		return item;
	}

}
