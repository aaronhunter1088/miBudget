package com.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.miBudget.core.MiBudgetState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.entities.Category;
import com.miBudget.entities.Item;
import com.miBudget.entities.User;
import com.miBudget.entities.UserAccountObject;
import com.miBudget.entities.UserItemsObject;
import org.springframework.beans.factory.annotation.Autowired;

public class MiBudgetDAOImpl {
	
	private static Logger LOGGER = LogManager.getLogger(MiBudgetDAOImpl.class);
	private SessionFactory factory;

	@Autowired
	public MiBudgetDAOImpl(SessionFactory factory) {
		this.factory = factory;
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
	public List<Category> getAllCategories(User user) {
		Session session = factory.openSession();
    	List<Category> categoriesFromDB = new ArrayList<>();
		try {
			LOGGER.info("Attempting to execute getAllCategories for " + user.getFirstName() + " " + user.getLastName());
			categoriesFromDB = (ArrayList<Category>) session
					.createNativeQuery("SELECT * FROM userscategories "
					   				 + "WHERE userId = " + user.getId())
					.addEntity(Category.class).getResultList();
			LOGGER.info("Query executed. categories list populated from MiBudgetDAOImpl.");
			LOGGER.info("Returning " + categoriesFromDB.size() + " categories for " + user.getFirstName() + " " + user.getLastName());
			session.close();
			if (categoriesFromDB.size() > 0) {
				for ( Object category : categoriesFromDB) {
					LOGGER.info(category);
				}
			} else {
				LOGGER.info(user.getFirstName() + " " + user.getLastName() + " hasn't saved any categories yet. Getting default list");
				//categoriesFromDB = user.getCategories();
			}
			return categoriesFromDB;
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
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
	public HashMap<String, ArrayList<UserAccountObject>> getAcctsAndInstitutionIdMap(User user) {
		HashMap<String, ArrayList<UserAccountObject>> mapToReturn = new HashMap<>();
		ArrayList<String> insIdsList = new ArrayList<>();
		ArrayList<UserAccountObject> acctsList = new ArrayList<>();
		
		try {
			insIdsList = getInstitutionIdsFromUser(user);
			if (insIdsList.size() != 0) {
				for (String id : insIdsList) {
					long itemTableId = MiBudgetState.getItemDAOImpl().getItemTableIdUsingInsId(id);
					acctsList = MiBudgetState.getAccountDAOImpl().getAllUserAccountObjectsFromUserAndItemTableId(user, itemTableId);
					mapToReturn.put(id, acctsList);
					//LOGGER.info("id");
					acctsList.forEach(acct -> {
						LOGGER.info(acct);
					});
					//LOGGER.info("");
				}
			}
			else {
				return mapToReturn; // empty
			}
			
		}
		catch (Exception e) {
    		LOGGER.error("An exception occurred!");
			LOGGER.error(e.getMessage());
		}
		return mapToReturn;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getInstitutionIdsFromUser(User user) {
		Session session = null;
    	Transaction t = null;
		ArrayList<String> institutionIds = new ArrayList<>();
		try {
			//LOGGER.info("Attempting to get all the institutionids for " + user.getFirstName() + " " + user.getLastName());
			session = factory.openSession();
			t = session.beginTransaction();
			institutionIds = (ArrayList<String>) session
					   .createNativeQuery("SELECT institutionid FROM usersinstitutionsids "
					   					+ "WHERE userid = " + user.getId())
					   .getResultList();
			//LOGGER.info("Query executed.");
			LOGGER.info(institutionIds.size() + " institutionids for " + user.getFirstName() + " " + user.getLastName());
			session.getTransaction().commit();
			session.close();
			if (institutionIds.size() != 0) {
				for ( String id : institutionIds) {
					//LOGGER.info("institutionid: " + id);
				}
			} else {
				LOGGER.info("Returning an empty list.");
				return new ArrayList<String>();
			}
			return institutionIds; // TODO: possibly return an empty list.
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		} 
		return institutionIds;
	}
    
    @SuppressWarnings("unchecked")
	public ArrayList<String> getAllInstitutionIdsFromUser(User user) {
    	Session session = null;
    	Transaction t = null;
		ArrayList<String> institutionIds = new ArrayList<>();
		try {
			LOGGER.info("Attempting to execute getAllInstitutionIds query...");
			session = factory.openSession();
			t = session.beginTransaction();
			institutionIds = (ArrayList<String>) session
					   .createNativeQuery("SELECT institution_id FROM user_institutions_ids "
					   					+ "WHERE user_id = " + user.getId()).getResultList();
			LOGGER.info("Query executed. institutionIds list populated from MiBudgetDAOImpl.");
			LOGGER.info("Returning " + institutionIds.size() + " institution ids for " + user.getFirstName() + " " + user.getLastName());
			session.getTransaction().commit();
			session.close();
			AtomicInteger ai = new AtomicInteger();
			for ( String id : institutionIds) {
				ai.incrementAndGet();
				LOGGER.info(" {} institutionid: {}", ai.get(), id);
			}
			return institutionIds;
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		} 
		return institutionIds;
    }
	
    // SQL Implementation
    public int addInstitutionIdToDatabase(String insId, User user) {
    	Session session = null;
    	Transaction t = null;
    	try {
			LOGGER.info("Attempting to execute insert institutionid query...");
			session = factory.openSession();
			t = session.beginTransaction();
			session.createNativeQuery("INSERT INTO user_institution_ids (institution_id, user_id) " +
									  "VALUES ('" + insId + "', " + user.getId() + ")").executeUpdate(); // recall Strings need quotes around them. numbers don't
			t.commit();
			LOGGER.info("Institution saved"); // "Institution save using session.save(item)"
			session.close();
			return 1; // good
		} catch (HibernateError e) {
			LOGGER.error("There was a specific Hibernate Error");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
        } catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
		return 0; // bad
	}
    
    // POJO Implementation
    public int addUserToDatabase(User user) {
    	Session session = null;
    	Transaction t = null;
    	try {
    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.save(user);
    		t.commit();
//    		for (Category c : user.getCategories()) {
//    			t = session.beginTransaction();
//    			session.save(c);
//    			session.getTransaction().commit();
//    		}
    		session.close();
			return 1; // good
		} catch (Exception e) {
			LOGGER.error("Error saving user to database");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
		return 0; // bad
    }
    
	public List<String> getAllCellphones() {
		List<String> cellphones = new ArrayList<>();
		Session session = factory.openSession();
		try {
			LOGGER.info("Attempting to execute query...");
			List<String> cellphonesFromDB = (List<String>) session
					   .createNativeQuery("SELECT cellphone FROM users")
					   .getResultList();
			LOGGER.info("Query executed!");
			session.close();
			Iterator<?> iterator = cellphonesFromDB.iterator();
			if (iterator.hasNext()) {
				while (iterator.hasNext()) {
					String cell = iterator.next().toString();
					cellphones.add(cell);
					LOGGER.info("cellphone: " + cell);
				}
			} else {
				LOGGER.info("Size of cellphones from DB is : " + cellphonesFromDB.size());
			}
			LOGGER.info("cellphones list populated from MiBudgetDAOImpl");
			return cellphones;
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB", e);
			session.close();
		}
		return cellphones;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute getAllUsers query...");
    		session = factory.openSession();
			t = session.beginTransaction();
			users = (List<User>) session.createNativeQuery("SELECT * FROM users")
										.addEntity(User.class).getResultList();
			session.getTransaction().commit();
			session.close();
			LOGGER.info("Query executed! " + users.size() + " users retrieved.");
			// Populate users accountId's if they have any
//			for (User user : usersNoAccounts) {
//				ArrayList<String> accountIds = (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAccountIdsFromUser(user);
//				user.setAccountIds(accountIds);
//				user.createCategories();
//				LOGGER.info("userFromDB: " + user);
//				users.add(user);
//			}
		}
		catch (HibernateException e) {
    		LOGGER.error("Error during retrieval of next account or adding account to accounts list.");
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getCause());
			t.rollback();
			session.close();
    	} 
    	return users;
	}

	// Logic for Items we create
	public int addItemToUsersItemsTable(long itemTableId, User user) {
		Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute addItemToUsersItemsTable query...");
			session = factory.openSession();
			long user_id = user.getId();
			t = session.beginTransaction();
			UserItemsObject uiObj = new UserItemsObject(itemTableId, user_id);
//			session.createNativeQuery("INSERT INTO users_items ('item_id', 'user_id') " +
//									  "VALUES (" + item_id + ", " + user_id + ")").executeUpdate();
//			session.createNativeQuery("UPDATE users " + 
//									  "SET Items = '" + item_id + "' " +
//									  "WHERE Id = " + user.getId()).executeUpdate();
//			update users set Items = "works" where Id = 2;
			
//			session.saveOrUpdate(user);
			LOGGER.info(uiObj);
			session.save(uiObj);
			t.commit();
			session.close();
			LOGGER.info(uiObj + " added to UsersItems table for: " + user.getFirstName() + ", Id: " + user.getId());
			return 1;
		} catch (NullPointerException e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
		return 0;
	}
	
	public List<Item> getAllItemIds() {
		List<Item> items = new ArrayList<>();
		Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute query...");
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
			
			LOGGER.info("3 Queries executed!");
			t.commit();
			session.close();
			int size = idsFromDB.size();
			for (int i = 0; i < size; i++) {
				Item item = new Item(idsFromDB.get(i).toString(), accessTokensFromDB.get(i).toString(), institutionIdsFromDB.get(i).toString());
				LOGGER.info("item: " + item);
				items.add(item);
			}
			
			LOGGER.info("all items from MiBudgetDAOImpl");
			return items;
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
		return null;
	}

	public List<String> getAllItemIdsFromUser(User user) {
		List<String> itemIds = new ArrayList<>();
		Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute query getAllItemsFromUser...");
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> item_idsFromDB = session
					   .createNativeQuery("SELECT useritemid FROM usersitems " +
							   			  "WHERE userid = " + user.getId())
					   .getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			//System.out.println("the results string: " + item_idsFromDB);
			if (item_idsFromDB.size() == 0) {
				LOGGER.info("no item_ids for this user yet...");
				return itemIds;
			}
			if (item_idsFromDB.size() > 0 || !item_idsFromDB.equals(null)) {
				
				Iterator<?> iterator = item_idsFromDB.iterator();
				while (iterator.hasNext()) {
					String itemID = iterator.next().toString();
					itemIds.add(itemID);
					LOGGER.info("itemid: " + itemID);
				}
//				String[] item_idsFromDBArr = item_idsFromDB.split(",");
//				for(String s : item_idsFromDBArr) {
//					String itemID = s;
//					itemIds.add(itemID);
//					System.out.println("item_id: " + itemID);
//				}
				LOGGER.info("itemids list populated from MiBudgetDAOImpl");
				return itemIds;
			} 
		} catch (NullPointerException e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e);
			LOGGER.error(e.getStackTrace());
			LOGGER.error(e.getMessage());
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
		LOGGER.info("Attempting to getItemFromDatabase using institutionId query...");
		Item item = null;
		Session session = null;
    	Transaction t = null;
		try {
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> itemTableId = session.createNativeQuery("SELECT _id " +
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
			LOGGER.info("retrieving item: " + item);
			t.commit();
			session.close();
			return item;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
		return item;
	}

	public ArrayList<com.miBudget.entities.Transaction> getTransactions(HttpServletRequest request) {
		// TODO: Implement if needed
		return new ArrayList<com.miBudget.entities.Transaction>();
	}

	public String clearTransactions(HttpSession session)
	{
		// clear transactions from user and session
		if (session.getId() != null)
		{
			User user = (User) session.getAttribute("user");
			//user.setTransactions(new ArrayList<>());
			session.setAttribute("usersTransactions", new ArrayList<>());
		}
		else return "{\"message\": \"session is null\"";
		return "{\"message\":\"user and session cleared\"";
	}
}
