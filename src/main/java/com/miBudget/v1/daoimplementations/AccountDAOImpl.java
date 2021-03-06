package com.miBudget.v1.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;

import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.ItemAccountObject;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UserAccountObject;
import com.miBudget.v1.utilities.HibernateUtilities;

public class AccountDAOImpl {

	
	
	public AccountDAOImpl() {
    }
    
	// TODO: think of a better name and incorporate user.
	/**
	 * This method returns all the accountIds from a specific item for a specific user.
	 * @param item
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<String> getAccountIdsFromUser(Item item) {
    	ArrayList<String> accountIds = null;
    	Session session = null;
    	SessionFactory factory = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAccountIdsFromUser...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		accountIds = (ArrayList<String>) session
    				            .createNativeQuery("SELECT account_id FROM accounts " +
    								               "WHERE item_table_id = " + item.getItemTableId())
    											   .getResultList();
    		t.commit();
    		session.close();
    	} catch (Exception e) {
			System.out.println("Error executing getAccountIdsFromUser...");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		}
    	return accountIds;
    }
    
    /**
     * This method will return a List<String> object that
     * has all the accountIds for a specific user.
     * @param userId
     * @return
     */
    public List<String> getAccountIdsFromUser(int userId) {
    	List<String> userAccounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts ")
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				for (String accountId : (List<String>) userAccountsFromDB) {
					userAccounts.add(accountId);
					System.out.println("accountId: " + accountId);
				}
				System.out.println("user accounts list populated from AccountDAOImpl\n");
				return userAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return userAccounts;
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<String> getAccountIdsFromUser(User user) {
    	ArrayList<String> usersAccounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			usersAccounts = (ArrayList<String>) session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			System.out.println("Query executed!");
			session.getTransaction().commit();
			session.close();
			if (usersAccounts.size() == 0) {
				System.out.println(user.getFirstName() + " " + user.getLastName() + " currently doesn't have any accounts added.");
				return usersAccounts;
			} else {
				for (String acctId : usersAccounts) {
					System.out.println("accountId: " + acctId);
				}
				System.out.println("accountsIds list populated from AccountDAOImpl\n");
				return usersAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			session.close();
		} 
		return usersAccounts;
    }

    public int addAccountObjectToAccountsTableDatabase(com.miBudget.v1.entities.Account account) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute insert account query...");
			System.out.println(account);
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			session.save(account);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }
    
    public int addAccountObjectToUsersAccountsTable(com.miBudget.v1.entities.Account account, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute addAccountToUsersAccounts Table query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			session.saveOrUpdate(new UserAccountObject(
					user.getId(), 
					account.getAccountId(),
					account.getItemTableId(),
					account.getAvailableBalance(),
					account.getCurrentBalance(),
					account.getLimit(),
					account.getCurrencyCode(),
					account.getNameOfAccount(),
					account.getOfficialName(),
					account.getMask(),
					account.getType(),
					account.getSubType()
					));
			System.out.println("Inserted account into UsersAccounts.");
			t.commit();
			session.close();
			return 1; // good
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    		t.rollback();
    		session.close();
    	}
    	return 0; // bad
    }
    
    /**
     * Use this method, getAllAccounts, when you need to get all accounts' info's from one user
     * @param user
     * @param account_ids
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Account> getAllAccounts(ArrayList<String> accountIds) {
    	SessionFactory factory = HibernateUtilities.getSessionFactory();
    	Session session = null;
		Transaction t = null;
		ArrayList<Account> accountsResult = new ArrayList<>();
		StringBuilder str = new StringBuilder();
    	if (accountIds.size() != 0) {
			str.append("'" + accountIds.get(0) + "'");
    		for (int i = 1; i < accountIds.size(); i++)
    			str.append(", '" + accountIds.get(i) + "'");
    	} else {
    		System.out.println("Query not attempted. No accounts!");
    		return accountsResult;
    	}
    	System.out.println("strBuilder: " + str);
    	try {
    		System.out.println("\nAttempting to execute getAllAccounts from user query...");
    		session = factory.openSession();
			t = session.beginTransaction();
			accountsResult = (ArrayList<Account>) session
					.createNativeQuery("SELECT * FROM accounts " +
									   "WHERE account_id " +
									   "IN (" +  str + ")")
					.addEntity(Account.class).getResultList();
			session.getTransaction().commit();
			session.close();
			System.out.println("Query executed!");
		} catch (HibernateException e) {
    		System.out.println("Error during retrieval of next account or adding account to accounts list.");
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			t.rollback();
			session.close();
    	} 
		ArrayList<Account> accounts = new ArrayList<>();
		for (int i = 0; i < accountsResult.size(); i++) {
			//System.out.println("acc: " + accountsResult.get(i));
			accounts.add(accountsResult.get(i));
		}
		
		return accounts;
		
		/**
		 * Hibernate: 
			    SELECT
			        * 
			    FROM
			        accounts 
			    WHERE
			        account_id = 'D7zZvPBJn9uyk5WA3gr1CWgLJJRaDbfvwQ8rB'
			account received: [[Ljava.lang.Object;@4fba8eec]
			
		 * Solution: See above implementation
		 */
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Account> getAllAccountsForItem(int itemTableId) {
    	ArrayList<Account> accounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to getAllAccountsForItem using itemTableId query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<Account> userAccountsFromDB = (List<Account>) session
					   .createNativeQuery("SELECT * FROM accounts " +
							   		     "WHERE item_table_id = " + itemTableId)
					   .addEntity(Account.class)
					   .getResultList();
			System.out.println("getAllAccountsForItem query executed!");
			t.commit();
			session.close();
			
			for (Object o : userAccountsFromDB) {
				accounts.add((Account) o);
				System.out.println("Returning account: " + ((Account) o));
			}	
    	} catch (HibernateException e) {
    		System.out.println("hibernate error:");
    		System.out.println(e.getMessage());
    	}	
//    	} catch (Exception e) {
//    		System.out.println("Error connecting to DB");
//			System.out.println(e.getStackTrace());
//			t.rollback();
//			session.close();
//    	}
    	return accounts;	
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<UserAccountObject> getAllUserAccountObjectsFromUserAndItemTableId(User user, int itemTableId) {
    	ArrayList<UserAccountObject> accounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllUsersAccounts for " + user.getFirstName() + " " + user.getLastName());
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<UserAccountObject> userAccountsFromDB = (List<UserAccountObject>) session
					   .createNativeQuery("SELECT * FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() + " " +
							   			  "AND item_table_id = " + itemTableId)
					   .addEntity(UserAccountObject.class).getResultList();
			System.out.println("Query executed!");
			for (Object id : userAccountsFromDB) {
				accounts.add((UserAccountObject) id);
			}
			System.out.println("Returning " + accounts.size() + " accounts for " + itemTableId);
			session.close();	
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accounts;
    	
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<UserAccountObject> getAllOfUsersAccounts() {
    	ArrayList<UserAccountObject> accounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllOfUsersAccounts query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<UserAccountObject> userAccountsFromDB = session
					   .createNativeQuery("SELECT * FROM users_accounts")
					   .addEntity(UserAccountObject.class)
					   .getResultList();
			System.out.println("Query executed!");
			//t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accounts.add((UserAccountObject) id);
			}	
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accounts;
    }
    
    /**
     * This method returns all the account_ids stored in the users_accounts table.
     * @param user
     * @return
     */
    public List<String> getAllAccountsIds(User user) {
    	List<String> accountIds = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllAccountIds query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accountIds.add((String) id);
			}	
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accountIds;
    }
    
    public int addAccountIdToItemsAccountsTable(int itemTableId, String accountId) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		ItemAccountObject iaObj = new ItemAccountObject(itemTableId, accountId);
			System.out.println("\nAttempting to insert: " + iaObj + " into the ItemsAccounts table...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			session.save(iaObj);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }
    
    
    public boolean deleteAccountFromDatabase(Account account) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	
    	try {
    		System.out.println("\nAttempting to execute delete account from database query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.delete(account);
    		session.getTransaction().commit();
    		String name = account.getOfficialName() != null ? account.getOfficialName() : account.getNameOfAccount();
    		System.out.println(name + " was deleted!");
    		session.close();
    		return true;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
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
    
    /**
     * This method will delete all accounts from the accounts
     * table from a given Item.
     */
    public int deleteAccountsFromDatabase(ArrayList<Account> accounts) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete accounts query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		
    		StringBuilder allAccountIdsString = new StringBuilder();
    		allAccountIdsString.append("'");
    		accounts.forEach(acct -> {
    			allAccountIdsString.append(acct.getAccountId() + "', ");
    		});
    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last space
    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last comma
    		session.createQuery("DELETE FROM account " + 
    							"WHERE account_id IN " + allAccountIdsString + "");
    		for(int i = 0; i < accounts.size(); i++) {
    			String delAcct = accounts.get(i).getNameOfAccount();
    			t = session.beginTransaction();
    			session.delete(accounts.get(i));
    			t.commit();
    			System.out.println(delAcct + " was deleted!");
    		}
    		session.close();
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
    
    
    
    public boolean deleteFromItemsAccounts(Item item, String accountId) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to delete ItemsAccountsObject from items_accounts table");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
	    	t = session.beginTransaction();
			// Items_accounts table
			// delete from items_accounts where item_table_id = 963;
			session.createQuery("DELETE FROM ItemAccountObject " + 
			    				"WHERE item_table_id = " + item.getItemTableId() + 
			    				"AND account_id = \'" + accountId + "\'").executeUpdate();
			System.out.println("... account was deleted from items_accounts table...");
			t.commit();
			session.close();
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
    
    
    public boolean deleteAccountFromUsersAccounts(String accountId, Item item, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete account from users_accounts table...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UserAccountObject " +
					"WHERE account_id = \'" + accountId + "\' " +
					"AND item_table_id = " + item.getItemTableId()).executeUpdate();

    		System.out.println("... account was deleted from users_accounts table...");
    		t.commit();
    		session.close();
    		return true;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
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
}
