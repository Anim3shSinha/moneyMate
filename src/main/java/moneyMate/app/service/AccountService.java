package moneyMate.app.service;

import lombok.RequiredArgsConstructor;
import moneyMate.app.dto.AccountDto;
import moneyMate.app.dto.ConvertDto;
import moneyMate.app.dto.TransferDto;
import moneyMate.app.entity.Account;
import moneyMate.app.entity.Transaction;
import moneyMate.app.entity.User;
import moneyMate.app.repository.AccountRepository;
import moneyMate.app.service.helper.AccountHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountHelper.createAccount(accountDto, user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
                .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user);
    }

    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto, user);
    }

    public Account findAccount(String code, long recipientAccountNumber) {
        return accountRepository.findByCodeAndAccountNumber(code, recipientAccountNumber).orElseThrow();
    }
}