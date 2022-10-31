package core.basesyntax.service.impl;

import static org.junit.Assert.assertEquals;

import core.basesyntax.db.Store;
import core.basesyntax.model.Operation;
import core.basesyntax.service.DataParserService;
import core.basesyntax.strategy.OperationStrategyImpl;
import core.basesyntax.strategy.operation.BalanceOperationImpl;
import core.basesyntax.strategy.operation.OperationHandler;
import core.basesyntax.strategy.operation.PurchaseOperationImpl;
import core.basesyntax.strategy.operation.ReturnOperationImpl;
import core.basesyntax.strategy.operation.SupplyOperationImpl;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataParserServiceImplTest {
    private static DataParserService dataParserService;
    private final String data = "b,banana,20" + System.lineSeparator()
            + "b,apple,100" + System.lineSeparator()
            + "s,banana,100" + System.lineSeparator()
            + "p,banana,13" + System.lineSeparator()
            + "r,apple,10" + System.lineSeparator()
            + "p,apple,20" + System.lineSeparator()
            + "p,banana,5" + System.lineSeparator()
            + "s,banana,50" + System.lineSeparator();

    @BeforeClass
    public static void beforeClass() {
        Map<String, OperationHandler> operationServiceMap = new HashMap<>();
        operationServiceMap.put(Operation.BALANCE.getOperation(),
                new BalanceOperationImpl());
        operationServiceMap.put(Operation.SUPPLY.getOperation(),
                new SupplyOperationImpl());
        operationServiceMap.put(Operation.PURCHASE.getOperation(),
                new PurchaseOperationImpl());
        operationServiceMap.put(Operation.RETURN.getOperation(),
                new ReturnOperationImpl());
        dataParserService = new DataParserServiceImpl(
                new OperationStrategyImpl(operationServiceMap));
    }

    @Test
    public void parseData_validData_ok() {
        Map<String, Integer> actualStorage = dataParserService.parseData(data);
        int expectedSize = 2;
        int actualSize = actualStorage.size();
        assertEquals("Expected size: " + expectedSize,expectedSize, actualSize);
        int expectedBananaQuantity = 152;
        int actualBananaQuantity = actualStorage.get("banana");
        assertEquals("Expected banana quantity: " + expectedBananaQuantity
                        + ", but was: " + actualBananaQuantity,
                expectedBananaQuantity, actualBananaQuantity);
        int expectedAppleQuantity = 90;
        int actualAppleQuantity = actualStorage.get("apple");
        assertEquals("Expected apple quantity" + expectedAppleQuantity
                        + ", but was: " + actualAppleQuantity,
                expectedAppleQuantity, actualAppleQuantity);
    }

    @After
    public void tearDown() {
        Store.FRUIT_STORAGE.clear();
    }
}