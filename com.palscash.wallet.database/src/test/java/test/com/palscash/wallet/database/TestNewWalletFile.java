package test.com.palscash.wallet.database;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.io.FileUtils;

import com.palscash.wallet.database.common.exception.WalletException;
import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.domain.Transfer;
import com.palscash.wallet.database.service.AccountService;
import com.palscash.wallet.database.service.TransferService;
import com.palscash.wallet.database.service.WalletService;

import junit.framework.TestCase;

public class TestNewWalletFile extends TestCase {

	//public void test() throws WalletException, InterruptedException, IOException {
	public static void main(String[] args) throws WalletException, InterruptedException, IOException {

		String fileName = "wallet1";

		File file = new File(fileName + ".pca");

		FileUtils.deleteQuietly(file);

		{

			WalletService.create(new File(fileName), "test");
			WalletService.close();

			assertTrue(file.exists());
		}

		{
			boolean open = WalletService.open(new File(fileName), "test");
			assertTrue(open);

			AccountService accountService = WalletService.getCtx().getBean(AccountService.class);
			TransferService transferService = WalletService.getCtx().getBean(TransferService.class);

			assertEquals(0, accountService.getCount());

			Account acc = new Account();
			acc.setPrivateKey("priv");
			acc.setPublicKey("pub");
			acc.setUuid("uuid");
			acc.setBalance(BigDecimal.TEN.toPlainString());

			acc = accountService.save(acc);

			assertEquals(1, accountService.getCount());

			// Save new transfers
			for (int i = 0; i < 10; i++) {
				Transfer t1 = new Transfer();
				t1.setAccount(acc);
				transferService.save(t1);
				acc.getTransfers().add(t1);
			}

			accountService.save(acc);

			// Count transfers by account
			assertEquals(10, transferService.getCount(acc));

			assertEquals(10, transferService.getAllByAccount(acc, 0, 20).getTotalElements());

			WalletService.close();
		}
		/*
		 * 
		 * try { WalletService walletService = new WalletService(); boolean open
		 * = walletService.open(new File(fileName), "test2"); assertFalse(open);
		 * walletService.close(); fail("cant be here"); } catch (Exception e) {
		 * 
		 * }
		 */

		FileUtils.forceDelete(file);

		assertFalse(file.exists());

	}

}
