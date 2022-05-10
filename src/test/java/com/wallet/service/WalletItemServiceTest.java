package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.repository.WalletItemRepository;
import com.wallet.util.enums.TypeEnum;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class WalletItemServiceTest {

    @MockBean
    private WalletItemRepository repository;

    @Autowired
    private WalletItemService service;

    private static final Date DATE = new Date();
    private static final TypeEnum TYPE = TypeEnum.EN;
    private static final String DESCRIPTION = "Conta de Luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);

    @Test
    public void testSave() {
	BDDMockito.given(repository.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());

	WalletItem response = service.save(new WalletItem());

	assertNotNull(response);
	assertEquals(response.getDescription(), DESCRIPTION);
	assertEquals(response.getValue().compareTo(VALUE), 0);
    }

    @Test
    public void testFindBetweenDates() {
	List<WalletItem> list = Arrays.asList(getMockWalletItem());

	Page<WalletItem> page = new PageImpl<>(list);

	BDDMockito.given(repository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class), Mockito.any(PageRequest.class)))
		.willReturn(page);

	Page<WalletItem> response = service.findBetweenDates(1L, new Date(), new Date(), 0);

	assertNotNull(response);
	assertEquals(response.getContent().size(), 1);
	assertEquals(response.getContent().get(0).getDescription(), DESCRIPTION);

    }

    @Test
    public void testFindByType() {
	List<WalletItem> list = Arrays.asList(getMockWalletItem());

	BDDMockito.given(repository.findByWalletIdAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class))).willReturn(list);

	List<WalletItem> response = service.findByWalletAndType(1L, TypeEnum.EN);

	assertNotNull(response);
	assertEquals(response.get(0).getType(), TYPE);
    }

    @Test
    public void testSumByWallet() {
	BigDecimal value = BigDecimal.valueOf(45);

	BDDMockito.given(repository.sumByWalletId(Mockito.anyLong())).willReturn(value);

	BigDecimal response = service.sumByWalletId(1L);

	assertEquals(response.compareTo(value), 0);
    }

    private WalletItem getMockWalletItem() {
	Wallet w = new Wallet();
	w.setId(1L);

	return new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
    }
}