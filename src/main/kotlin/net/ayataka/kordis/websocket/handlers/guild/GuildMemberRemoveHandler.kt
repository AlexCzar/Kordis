package net.ayataka.kordis.websocket.handlers.guild

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.long
import net.ayataka.kordis.DiscordClientImpl
import net.ayataka.kordis.entity.server.ServerImpl
import net.ayataka.kordis.event.events.server.user.UserLeaveEvent
import net.ayataka.kordis.websocket.handlers.GatewayHandler

class GuildMemberRemoveHandler : GatewayHandler {
    override val eventType = "GUILD_MEMBER_REMOVE"
    override fun handle(client: DiscordClientImpl, data: JsonObject) {
        val server = client.servers.find(data["guild_id"].long) as? ServerImpl ?: return
        val userId = data["user"].jsonObject["id"].long
        val member = server.members.remove(userId)

        server.memberCount.decrementAndGet()

        if (member == null) {
            if (!server.initialized.get()) {
                server.removedMembers.add(userId)
            }
            return
        }

        client.eventManager.fire(UserLeaveEvent(member))
    }
}