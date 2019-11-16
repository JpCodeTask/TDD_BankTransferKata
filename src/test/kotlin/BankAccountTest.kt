import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BankAccountTest {

    companion object{
        lateinit var account: BankAccount
    }

    @BeforeEach
    fun setUp(){
        account = BankAccount(TransactionManagerFactoryImpl.getInstance())
    }

    @Test
    fun `when new account then default balance is 0`() {
        assertEquals(0, account.accountBalance)
    }

    @Test
    fun `when new account with negative balance create then throw IllegalArgumentException`(){
        assertThrows(IllegalArgumentException::class.java){ BankAccount(TransactionManagerFactoryImpl.getInstance(), -200) }
    }

    @Test
    fun `when deposit then accountBalance change`() {
        account.payInto(1000)
        assertEquals(1000, account.accountBalance)
    }

    @Test
    fun `when deposit money amount negative then throw IllegalArgumentException`() {
       assertThrows(IllegalArgumentException::class.java){ account.payInto( -100)}
    }

    @Test
    fun `when withdraw then accountBalance change`(){
        account.payInto(1000)
        account.withdraw(500)
        assertEquals(500, account.accountBalance)
    }

    @Test
    fun `when withdraw money amount negative then throw IllegalArgumentException`(){
        account.payInto(1000)
        assertThrows(IllegalArgumentException::class.java){ account.withdraw(-100)}
    }

    @Test
    fun `when withdraw more money then it on account throw NotEnoughMoneyException`(){
        assertThrows(BankAccount.NotEnoughMoneyException::class.java){account.withdraw(1000)}
    }

    @Test
    fun `when new account then transaction history is empty`(){
        assertEquals(true,
            account.getTransactionsHistory().isEmpty())
    }

    @Test
     fun `when pay into money then list of transactions is not empty`(){
         account.payInto(500)
         assertEquals(false,
             account.getTransactionsHistory().isEmpty())
     }

     @Test
     fun `when pay into money and withdraw then list of transactions is 2 long`(){
         account.payInto(500)
         account.withdraw(100)
         assertEquals(2, account.getTransactionsHistory().size)
     }

    @Test
    fun `when pay into money then list transaction contains PayIntoTransaction`(){
        val transaction = PayIntoTransaction(account.number, 350)
        account.payInto(350)
        assertEquals(transaction, account.getTransactionsHistory().first())
    }

    @Test
    fun `when withdraw then list transaction history contains WithdrawTransaction`(){
        val transaction = WithdrawTransaction(account.number, 400)
        account.payInto(500)
        account.withdraw(400)
        assertEquals(transaction, account.getTransactionsHistory().last())
    }

}