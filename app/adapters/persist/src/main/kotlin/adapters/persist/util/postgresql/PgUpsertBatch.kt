package adapters.persist.util.postgresql
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal fun <T : Table> T.pgInsertOrUpdateBatch(
    vararg keyColumns: Column<*>,
    entities: List<T>,
    body: T.(InsertStatement<Number>, T) -> Unit,
) = PgInsertOrUpdateBatch<Number>(keyColumns = keyColumns, table = this).apply {
    entities.forEach { entity ->
        body(this, entity)
    }
    execute(TransactionManager.current())
}

internal class PgInsertOrUpdateBatch<Key : Any> constructor(
    private val keyColumns: Array<out Column<*>>,
    table: Table,
    isIgnore: Boolean = false,
) : InsertStatement<Key>(table = table, isIgnore = isIgnore) {

    override fun prepareSQL(transaction: Transaction, prepared: Boolean): String {
        val updateSetter = super.values.keys.joinToString { "${it.name} = EXCLUDED.${it.name}" }
        val onConflict = "ON CONFLICT (${keyColumns.joinToString { it.name } }) DO UPDATE SET $updateSetter"
        return "${super.prepareSQL(transaction, prepared)} $onConflict"
    }
}