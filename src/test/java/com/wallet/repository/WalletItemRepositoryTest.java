package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WalletItemRepositoryTest {

    private static final Date DATE = new Date();
    private static final String TYPE = "EN";
    private static final String DESCRIPTION = "Conta de Luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);

    @Autowired
    WalletItemRepository repository;

    @Autowired
    WalletRepository walletRepository;

    @Test
    void testSave() {
	Wallet wallet = new Wallet();
	wallet.setName("Carteira 1");
	wallet.setValue(BigDecimal.valueOf(500));

	walletRepository.save(wallet);

	WalletItem walletItem = new WalletItem(1L, wallet, DATE, TYPE, DESCRIPTION, VALUE);
	WalletItem response = repository.save(walletItem);

	assertNotNull(response);

	assertEquals(response.getDescription(), DESCRIPTION);
	assertEquals(response.getType(), TYPE);
	assertEquals(response.getValue(), VALUE);
	assertEquals(response.getWallet().getId(), wallet.getId());
    }
}