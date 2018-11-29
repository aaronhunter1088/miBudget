package com.v1.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.v1.miBudget.entities.Account;
import com.v1.miBudget.entities.Item;
import com.v1.miBudget.entities.ItemAccountObject;
import com.v1.miBudget.entities.User;
import com.v1.miBudget.entities.UserAccountObject;
import com.v1.miBudget.utilities.HibernateUtilities;

public class AccountDAOImpl {
private static SessionFactory factory = HibernateUtilities.getSessionFactory();
	
	public AccountDAOImpl() {
    }
 
    public AccountDAOImpl(SessionFactory factory) {
    	AccountDAOImpl.factory = factory;
    }
    
    public List<String> getAccountIdsFromUser(Item item) {
    	List<String> accountIds = new ArrayList<>();
    	try {
    		System.out.println("\nAttempting to execute getAccountIdsFromUser...");
    		Session session = factory.openSession();
    		Transaction t;
    		t = session.beginTransaction();
    		List<?> accountIdsFromDB = session
    				.createNativeQuery("SELECT account_id FROM accounts " +
    								   "WHERE item_table_id = " + item.getItemTableId())
    									.getResultList();
    	} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
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
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + userId )
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				Iterator<?> iterator = userAccountsFromDB.iterator();
				while (iterator.hasNext()) { 
					String account_id = iterator.next().toString();
					userAccounts.add(account_id);
					System.out.println("account_id: " + account_id);
				}
				System.out.println("user accounts list populated from AccountDAOImpl\n");
				return userAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			
		} 
		return userAccounts;
    }
    
    public List<String> getAccountIdsFromUser(User user) {
    	List<String> userAccounts = new ArrayList<>();
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				Iterator<?> iterator = userAccountsFromDB.iterator();
				while (iterator.hasNext()) { 
					String account_id = iterator.next().toString();
					userAccounts.add(account_id);
					System.out.println("account_id: " + account_id);
				}
				System.out.println("user accounts list populated from AccountDAOImpl\n");
				return userAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			
		} 
		return userAccounts;
    }

    public int addAccountObjectToAccountsTableDatabase(com.v1.miBudget.entities.Account account) {
    	
    	try {
			System.out.println("\nAttempting to execute insert account query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			
//			com.v1.miBudget.entities.Account myAccount = new com.v1.miBudget.entities.Account(
//					account.getAccountId(),
//					item.getItemTableId(),
//					account.getAvailableBalance() != 0 ? account.getAvailableBalance() : 0.0,
//					account.getCurrentBalance() != 0 ? account.getCurrentBalance() : 0.0,
//					account.getLimit() != 0 ? account.getLimit() : 0.0,
//					account.getCurrencyCode() != null ? account.getCurrencyCode() : "USD",
//					account.getNameOfAccount() != null ? account.getNameOfAccount() : "",
//					account.getOfficialName() != null ? account.getOfficialName() : "",
//					account.getMask(),
//					account.getType(),
//					account.getSubType()
//			);
			System.out.println("Account: " + account);
			
			session.save(account);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			
		} 
    	return 0; // bad
    }
    
    public int addAccountObjectToUsers_AccountsTable(com.v1.miBudget.entities.Account account, User user) {
    	
    	try {
    		System.out.println("\nAttempting to execute addAccountToUsers_Table query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			
			
			
			// Using SQL code here because there is no object for users_accounts.
//			int insert = session.createNativeQuery("INSERT INTO users_accounts ('user_id', 'account_id') " +
//									  "VALUES (" + account.getAccountId() + "), (" + user.getId() + ")").executeUpdate();
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
			System.out.println("Inserted account into users_accounts");
			t.commit();
			session.close();
			return 1; // good
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    	}
    	return 0; // bad
    }
    
    /**
     * Use this method, getAllAccounts, when you need to get all accounts' info's from one user
     * @param user
     * @param account_ids
     * @return
     */
    public List<Account> getAllAccounts(User user, List<String> account_ids) {
    	List<Account> accounts = new ArrayList<>();
    	
    	Iterator<String> iter = account_ids.iterator();
    	while (iter.hasNext()) {
    		try {
        		System.out.println("\nAttempting to execute getAllAccounts query...");
    			Session session = factory.openSession();
    			Transaction t;
    			t = session.beginTransaction();
    			accounts = session
    					   .createNativeQuery("SELECT * FROM accounts " +
    							   			  "WHERE account_id = '" + iter.next() + "'").getResultList();
    			System.out.println("Query executed!");
    			t.commit();
    			session.close();
        	} catch (Exception e) {
        		System.out.println("Error connecting to DB");
    			System.out.println(e.getMessage());
        	}
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
    	try {
    		System.out.println("\nAttempting to execute getAllAccountIds query...");
			Session session = factory.openSession();
			Transaction t;
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
			
//			if (userAccountsFromDB.size() > 0) {
//				for (Object o : userAccountsFromDB) {
//					com.v1.miBudget.entities.Account newAccount = new com.v1.miBudget.entities.Account();
//					com.v1.miBudget.entities.Account acct = (com.v1.miBudget.entities.Account) o;
//					newAccount.setAccountId(acct.getAccountId());
//					newAccount.setItemTableId(acct.getItemTableId());
//					newAccount.setAvailableBalance(acct.getAvailableBalance());
//					newAccount.setCurrentBalance(acct.getCurrentBalance());
//					newAccount.setLimit(acct.getLimit());
//					newAccount.setCurrencyCode(acct.getCurrencyCode());
//					newAccount.setNameOfAccount(acct.getNameOfAccount());
//					newAccount.setOfficialName(acct.getOfficialName());
//					newAccount.setType(acct.getType());
//					newAccount.setSubType(acct.getSubType());
//					accounts.add(newAccount);
//				}
//				System.out.println("returning all accounts for " + user.getFirstName());
//				return accounts;
//				
//			} else {
//				System.out.println("No accounts for user " + user.getFirstName());
//				return accounts;
//			}
			
			
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
    	}
    	return accountIds;
    }
    
    public int addAccountIdToItems_AccountsTable(int itemTableId, String accountId) {
    	try {
			System.out.println("\nAttempting to execute insert accountId to Items_Accounts query...");
			Session session = factory.openSession();
			Transaction t;
			t = session.beginTransaction();
			
			ItemAccountObject obj = new ItemAccountObject(itemTableId, accountId);
			System.out.println("obj: " + obj);
			
			session.save(obj);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			
		} 
    	return 0; // bad
    }

    /**
     * TODO: Implement
     * This method will delete one account from the accounts 
     * table for a given Item.
     * @param account
     * @return
     */
    public int deleteAccountFromDatabase(Account account) {
    	try {
    		System.out.println("\nAttempting to execute delete account query...");
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
    
    /**
     * This method will delete all accounts from the accounts
     * table from a given Item.
     */
    public int deleteAccountsFromDatabase(ArrayList<Account> accounts) {
    	try {
    		System.out.println("\nAttempting to execute delete accounts query...");
    		Session session = factory.openSession();
    		Transaction t;
    		t = session.beginTransaction();
    		StringBuilder allAccountIdsString = new StringBuilder();
    		allAccountIdsString.append("'");
    		accounts.forEach(acct -> {
    			allAccountIdsString.append(acct.getAccountId() + "', ");
    		});
    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last space
    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last comma
    		session.createQuery("DELETE FROM account " + 
    							"WHERE account_id IN " + allAccountIdsString + "");
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
}
