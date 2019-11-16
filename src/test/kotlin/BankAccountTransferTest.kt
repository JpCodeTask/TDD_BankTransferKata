import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BankAccountTransferTest {

    companion object{
        lateinit var transactionManagerFactory: TransactionManager.Factory
        lateinit var fromAccount: BankAccount
        lateinit var inAccount: BankAccount
    }

    @BeforeEach
    fun setUp() {
        transactionManagerFactory = TransactionManagerFactoryImpl.getInstance()
        fromAccount = BankAccount(transactionManagerFactory,1000)
        inAccount = BankAccount(transactionManagerFactory,1000)
    }

    @Test
    fun `when transfer money between account then balances change`() {
        fromAccount.transfer(inAccount, 500)
        assertEquals(500, fromAccount.accountBalance)
        assertEquals(1500, inAccount.accountBalance)
    }

    @Test
    fun `when money amount to transfer is negative then throw IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java){
            fromAccount.transfer(inAccount, -100)
        }
    }

    @Test
    fun `when amount money to transfer is larger then balance then throw NotEnoughMoneyException`() {
        assertThrows(BankAccount.NotEnoughMoneyException::class.java){
            fromAccount.transfer(inAccount, 5000)
        }
    }

    @Test
    fun `when transfer money then transfer transactions are in history`(){
        fromAccount.transfer(inAccount, 234)

        assertEquals(true, fromAccount.getTransactionsHistory().last() is TransferFromTransaction)
        assertEquals(true, inAccount.getTransactionsHistory().last() is TransferInTransaction)
    }

    @Test
    fun `when query by operation type then return history of transactin of this type`() {
        fromAccount.payInto(100)
        fromAccount.payInto(100)
        fromAccount.withdraw(300)
        fromAccount.transfer(inAccount, 50)
        inAccount.transfer(fromAccount, 5)

        assertEquals(2, fromAccount.getTransactionsHistory(PayIntoTransaction::class.java).size)
        assertEquals(1, fromAccount.getTransactionsHistory(WithdrawTransaction::class.java).size)
        assertEquals(1, fromAccount.getTransactionsHistory(TransferFromTransaction::class.java).size)
        assertEquals(1, inAccount.getTransactionsHistory(TransferInTransaction::class.java).size)
    }
}