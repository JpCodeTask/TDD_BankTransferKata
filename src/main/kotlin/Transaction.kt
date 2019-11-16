interface TransactionManager{
    fun createPayIntoTransaction(moneyAmount: Int)
    fun createWithdrawTransaction(moneyAmount: Int)
    fun getTransactionHistory(): List<Transaction>
    fun createTransferInTransaction(anotherAccount: BankAccount, moneyAmount: Int)
    fun createTransferFromTransaction(anotherAccount: BankAccount, moneyAmount: Int)

    interface Factory{
        fun createTransactionManager(bankAccount: BankAccount): TransactionManager
    }
}


class TransactionManagerFactoryImpl private constructor (): TransactionManager.Factory{

    companion object{
        private lateinit var instance: TransactionManager.Factory
        fun getInstance(): TransactionManager.Factory{
            instance = TransactionManagerFactoryImpl()
            return instance
        }
    }

    override fun createTransactionManager(bankAccount: BankAccount): TransactionManager = InMemoryTransactionManager(bankAccount)

}

class InMemoryTransactionManager(private val account: BankAccount): TransactionManager{

    private val transactionHistory = mutableListOf<Transaction>()

    override fun createPayIntoTransaction(moneyAmount: Int) {
        transactionHistory.add(PayIntoTransaction(account.number, moneyAmount))
    }

    override fun createWithdrawTransaction(moneyAmount: Int) {
        transactionHistory.add(WithdrawTransaction(account.number, moneyAmount))
    }

    override fun createTransferInTransaction(anotherAccount: BankAccount, moneyAmount: Int) {
        transactionHistory.add(TransferInTransaction(anotherAccount.number, account.number, moneyAmount))
    }

    override fun createTransferFromTransaction(anotherAccount: BankAccount, moneyAmount: Int) {
        transactionHistory.add(TransferFromTransaction(account.number, anotherAccount.number, moneyAmount))
    }

    override fun getTransactionHistory() = transactionHistory


}

abstract class Transaction()

data class PayIntoTransaction(val number: String, val moneyAmount: Int): Transaction()

data class WithdrawTransaction(val number: String, val moneyAmount: Int): Transaction()

data class TransferInTransaction(val fromAccountNumber: String, val number: String, val moneyAmount: Int): Transaction()

data class TransferFromTransaction(val fromAccountNumber: String, val number: String, val moneyAmount: Int): Transaction()
