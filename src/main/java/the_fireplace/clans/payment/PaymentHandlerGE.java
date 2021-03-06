package the_fireplace.clans.payment;

import the_fireplace.clans.clan.ClanCache;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.economy.Account;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class PaymentHandlerGE implements IPaymentHandler {

	@Override
	public boolean deductAmount(long amount, UUID account) {
		boolean ret = GrandEconomyApi.takeFromBalance(account, amount, false);
		if(ret && ClanCache.getClan(account) != null)
			try {
				Objects.requireNonNull(Account.get(account)).writeIfChanged();
			} catch(IOException e) {
				e.printStackTrace();
			}
		return ret;
	}

	@Override
	public long deductPartialAmount(long amount, UUID account) {
		long balance = GrandEconomyApi.getBalance(account);
		if(balance > amount) {
			deductAmount(amount, account);
			return 0;
		} else if(deductAmount(balance, account))
			return amount - balance;
		else
			return amount;
	}

	@Override
	public boolean addAmount(long amount, UUID account) {
		if(Account.get(account) == null)
			return false;
		GrandEconomyApi.addToBalance(account, amount, false);
		if(ClanCache.getClan(account) != null)
			try {
				Objects.requireNonNull(Account.get(account)).writeIfChanged();
			} catch(IOException e) {
				e.printStackTrace();
			}
		return true;
	}

	@Override
	public void ensureAccountExists(UUID account) {
		Account.get(account);
	}

	@Override
	public long getBalance(UUID account) {
		Account acct = Account.get(account);
		return acct != null ? acct.getBalance() : -1;
	}

	@Override
	public String getCurrencyName(long amount) {
		return GrandEconomyApi.getCurrencyName(amount);
	}
}
