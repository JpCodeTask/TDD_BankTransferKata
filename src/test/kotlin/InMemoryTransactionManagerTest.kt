import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryTransactionManagerTest {

    companion object{
        lateinit var transactionManager: InMemoryTransactionManager
        lateinit var transactionManagerFactory: TransactionManager.Factory
    }

    @BeforeEach
    fun setU() {
        transactionManagerFactory = TransactionManagerFactoryImpl.getInstance()
        transactionManager = InMemoryTransactionManager(BankAccount(transactionManagerFactory))
    }

    @Test
    fun `when create pay into transaction then history has pay into transaction`(){
        transactionManager.createPayIntoTransaction(500)
        assertEquals(true,
            transactionManager.getTransactionHistory().first() is PayIntoTransaction)
    }


    @Test
    fun `when create withdraw transaction then history has withdraw transaction`(){
        transactionManager.createWithdrawTransaction(400)
        assertEquals(true,
            transactionManager.getTransactionHistory().first() is WithdrawTransaction)
    }

    @Test
    fun `when create transfer in transaction then history has transfer in transaction`(){
        transactionManager.createTransferInTransaction(BankAccount(TransactionManagerFactoryImpl.getInstance()), 450)

        val lastTransaction = transactionManager.getTransactionHistory().last()

        assertEquals(true, lastTransaction is TransferInTransaction)
    }

    @Test
    fun `when create transfer from transaction then history has transfer from transaction`(){
        transactionManager.createTransferFromTransaction(BankAccount(transactionManagerFactory), 20)

        val lastTransaction = transactionManager.getTransactionHistory().last()

        assertEquals(true, lastTransaction is TransferFromTransaction)
    }
}