package com.v1.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.User;
import com.v1.miBudget.entities.UsersItemsObject;
import com.v1.miBudget.utilities.HibernateUtilities;

public class MiBudgetDAOImpl {
	
	private SessionFactory factory = HibernateUtilities.getSessionFactory();
	
	private AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
	
	public MiBudgetDAOImpl() {
    }
    
    // get the users institutuion ids from users_institution_ids table
    public List<String> getAllInstitutionIdsFromUser(User user) {
    	// TODO Auto-generated method stub
		List<String> institutionIds = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAllInstitutionIds query...");
			Session session = this.factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> institutionIdsFromDB = session
					   .createNativeQuery("SELECT institution_id FROM users_institution_ids " + 
							   			  "WHERE user_id = " + user.getId()).getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			Iterator<?> iterator = institutionIdsFromDB.iterator();
			while (iterator.hasNext()) {
				String id = iterator.next().toString();
				institutionIds.add(id);
				System.out.println("institution_id: " + id);
			}
			System.out.println("institution_ids list populated from MiBudgetDAOImpl\n");
			return institutionIds;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return null;
			
		} finally {
		}
    }
	
    // SQL Implementation
    public int addInstitutionIdToDatabase(String ins_id, User user) {
    	try {
			System.out.println("\nAttempting to execute insert institution_id query...");
			Session session = this.factory.openSession();
			Transaction t;
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
			return 0; // bad
		} finally {
		}
	}
    
    // POJO Implementation
    public int addUserToDatabase(User user) {
    	try {
    		Session session = this.factory.openSession();
    		Transaction t;
    		t = session.beginTransaction();
    		session.save(user);
    		t.commit();
    		session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return 0; // bad
		}
    }
    
	public List<String> getAllCellphones() {
		// TODO Auto-generated method stub
		List<String> cellphones = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute query...");
			Session session = this.factory.openSession();
			Transaction t;
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
			
		} finally {
		}
		return cellphones;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAllUsers query...");
			Session session = this.factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> idsFromDB = session
					   				.createNativeQuery("SELECT id FROM users")
					   				.getResultList();
			List<?> firstnamesFromDB = session
											.createNativeQuery("SELECT first_name FROM users")
											.getResultList();
			List<?> lastnamesFromDB = session
										   .createNativeQuery("SELECT last_name FROM users")
										   .getResultList();
			List<?> cellphonesFromDB = session
									  .createNativeQuery("SELECT cellphone FROM users")
									  .getResultList();
			List<?> passwordsFromDB = session
					  					   .createNativeQuery("SELECT password FROM users")
					  					   .getResultList();
			List<?> emailsFromDB = session
										.createNativeQuery("SELECT email FROM users")
										.getResultList();
			System.out.println("6 Queries executed!");
			t.commit();
			int size = idsFromDB.size();
			for (int i = 0; i < size; i++) {
				List<String> accountIds = accountDAOImpl.getAccountIdsFromUser((Integer)idsFromDB.get(i));
				User user = new User((Integer)idsFromDB.get(i),
									 firstnamesFromDB.get(i).toString(),
									 lastnamesFromDB.get(i).toString(),
									 cellphonesFromDB.get(i).toString(),
									 passwordsFromDB.get(i).toString(),
									 emailsFromDB.get(i).toString(),
									 accountIds );
				System.out.println("user: " + user.toString());
				users.add(user);
			}
			
			System.out.println("all users from miBudget");
			session.close();
		} catch (HibernateException e) { 
			System.out.println("Error opening a session using the factory.");
			System.out.println(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i=0; i<ste.length; i++) { System.out.println(ste[i]); }
			System.out.println("Returning null.");
			return null;
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			StackTraceElement[] ste = e.getStackTrace();
			for (int i=0; i<ste.length; i++) { System.out.println(ste[i]); }
			System.out.println("Returning null.");
			return null;
		} finally {
		}
		return users;
	}

	// Logic for Items we create
	
	
	
	public int addItemToUsersItemsTable(int item_table_id, User user) {
		
		try {
			System.out.println("\nAttempting to execute addItemToUsersItemsTable query...");
			Session session = this.factory.openSession();
			int user_id = user.getId();
			Transaction t;
			t = session.beginTransaction();
			UsersItemsObject uiObj = new UsersItemsObject(item_table_id, user_id);
//			session.createNativeQuery("INSERT INTO users_items ('item_id', 'user_id') " +
//									  "VALUES (" + item_id + ", " + user_id + ")").executeUpdate();
//			session.createNativeQuery("UPDATE users " + 
//									  "SET Items = '" + item_id + "' " +
//									  "WHERE Id = " + user.getId()).executeUpdate();
//			update users set Items = "works" where Id = 2;
			
//			session.saveOrUpdate(user);
			System.out.println("item_table_id: " + item_table_id);
			session.save(uiObj);
			t.commit();
			session.close();
			System.out.println("item_table_id: " + item_table_id + " added to \nuser: " + user.getFirstName() + " \nId: " + user.getId());
			return 1;
		} catch (NullPointerException e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			return 0;
			
		} finally {
		}
	}
	
	public List<Item> getAllItemIds() {
		List<Item> items = new ArrayList<>();
		
		// Excute the queries
		try {
			System.out.println("\nAttempting to execute query...");
			Session session = this.factory.openSession();
			Transaction t;
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
			
		} finally {
		}
		return null;
	}

	public List<String> getAllItemIdsFromUser(User user) {
		List<String> itemIds = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute query getAllItemsFromUser...");
			Session session = this.factory.openSession();
			Transaction t;
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
			
		} finally {
		}
		return null;
	}

	public Item getItemFromDatabase(String institution_id) {
		Item item = null;
		try {
			Session session = factory.openSession();
			Transaction t;
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
			item = new Item(Integer.parseInt(itemTableId.get(0).toString()), itemId.get(0).toString(), 
					accessToken.get(0).toString(), institution_id);
			System.out.println("retrieving item: " + item.toString());
			session.getTransaction().commit();
			session.close();
			return item;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return item;
	}

}
