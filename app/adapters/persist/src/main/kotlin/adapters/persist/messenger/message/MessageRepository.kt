package adapters.persist.messenger.message

import adapters.persist.messenger.entities.MessageSqlEntities
import adapters.persist.messenger.entities.MessageSqlEntity
import adapters.persist.messenger.entities.ReaderToMessageSqlEntities
import adapters.persist.messenger.entities.ReaderToMessageSqlEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

internal class MessageRepository {
    @MustBeCalledInTransactionContext
    fun getByIdOrNull(messageId: Long): MessageSqlEntity? {
        return MessageSqlEntities.select { MessageSqlEntities.id eq messageId }
            .limit(1)
            .map { MessageSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun hasEntityWithId(messageId: Long): Boolean {
        return MessageSqlEntities
            .select { MessageSqlEntities.id eq messageId }
            .limit(1)
            .count() > 0
    }

    @MustBeCalledInTransactionContext
    fun getMessagesPaging(roomId: Long, page: Long, pageSize: Int): Collection<MessageSqlEntity> {
        return MessageSqlEntities
            .select { MessageSqlEntities.roomId eq roomId }
            .orderBy(MessageSqlEntities.createdAt, SortOrder.DESC)
            .limit(n = pageSize, offset = page * pageSize)
            .map { MessageSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun getMessagesCount(roomId: Long): Long {
        return MessageSqlEntities
            .select { MessageSqlEntities.roomId eq roomId }
            .count()
    }

    @MustBeCalledInTransactionContext
    fun getLastMessageOfRoom(roomId: Long): MessageSqlEntity? {
        return MessageSqlEntities
            .select { MessageSqlEntities.roomId eq roomId }
            .orderBy(MessageSqlEntities.createdAt, SortOrder.DESC)
            .limit(1)
            .map { MessageSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: MessageSqlEntity): MessageSqlEntity {
        return MessageSqlEntities
            .pgInsertOrUpdate(MessageSqlEntities.id) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { MessageSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun getUnreadMessages(applicantId: Long, roomId: Long): Collection<MessageSqlEntity> {
        return (MessageSqlEntities leftJoin ReaderToMessageSqlEntities)
            .slice(MessageSqlEntities.columns)
            .select { (MessageSqlEntities.roomId eq roomId) and (ReaderToMessageSqlEntities.readerId neq applicantId) }
            .map { MessageSqlEntity.fromSqlResultRow(it) }
    }
}

internal class ReaderToMessageRepository {
    @MustBeCalledInTransactionContext
    fun getReadersByMessageId(messageId: Long): Collection<ReaderToMessageSqlEntity> {
        return ReaderToMessageSqlEntities
            .select { ReaderToMessageSqlEntities.messageId eq messageId }
            .map { ReaderToMessageSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: ReaderToMessageSqlEntity): ReaderToMessageSqlEntity {
        return ReaderToMessageSqlEntities
            .pgInsertOrUpdate(
                ReaderToMessageSqlEntities.readerId,
                ReaderToMessageSqlEntities.messageId
            ) {
                entity.toSqlStatement(it)
            }
            .resultedValues!!
            .first()
            .let { ReaderToMessageSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun upsert(entities: Collection<ReaderToMessageSqlEntity>): Collection<ReaderToMessageSqlEntity> {
        return entities.map { upsert(it) }
    }
}