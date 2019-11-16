class BankAccount(transactionManagerFactory: TransactionManager.Factory, _accountBalance: Int = 0) {

    val number = "123"

    var accountBalance = _accountBalance
        private set

    private val transactionManager = transactionManagerFactory.createTransactionManager(this)

    init {
        require(accountBalance >= 0 ){"Balance of new account cannot be negative"}
    }

    fun payInto(moneyAmount: Int) {
        require(moneyAmount >= 0) { "Money amount must be positive" }

        transactionManager.createPayIntoTransaction(moneyAmount)
        accountBalance += moneyAmount
    }


    fun withdraw(moneyAmount: Int){
        require(moneyAmount >= 0) { "Money amount must be positive" }

        if (hasEnoughMoneyForOperation(moneyAmount))
            throw NotEnoughMoneyException()

        transactionManager.createWithdrawTransaction(moneyAmount)
        accountBalance -= moneyAmount
    }

    private fun hasEnoughMoneyForOperation(moneyAmount: Int): Boolean = moneyAmount > accountBalance


    fun transfer(inAccount: BankAccount, moneyAmount: Int) {
        require(moneyAmount >= 0) { "Money amount must be positive" }

        if (hasEnoughMoneyForOperation(moneyAmount))
            throw NotEnoughMoneyException()

        inAccount.transactionManager.createTransferInTransaction(this, moneyAmount)
        transactionManager.createTransferFromTransaction(this, moneyAmount)

        inAccount.accountBalance += moneyAmount
        accountBalance -= moneyAmount
    }

    fun getTransactionsHistory(transactionType: Class<out Transaction> = Transaction::class.java): List<Transaction>  =
        transactionManager.getTransactionHistory().filterIsInstance(transactionType)


    class NotEnoughMoneyException: Exception(){
        override val message: String?
            get() = "There is not enough money on the account"
    }

}
