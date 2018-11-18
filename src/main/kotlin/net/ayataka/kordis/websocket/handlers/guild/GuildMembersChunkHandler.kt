package net.ayataka.kordis.websocket.handlers.guild

import com.google.gson.JsonObject
import net.ayataka.kordis.DiscordClientImpl
import net.ayataka.kordis.entity.server.ServerImpl
import net.ayataka.kordis.entity.server.member.MemberImpl
import net.ayataka.kordis.entity.user.UserImpl
import net.ayataka.kordis.websocket.handlers.GatewayHandler

class GuildMembersChunkHandler : GatewayHandler {
    override val eventType = "GUILD_MEMBERS_CHUNK"

    override fun handle(client: DiscordClientImpl, data: JsonObject) {
        val server = client.servers.find(data["guild_id"].asLong) as? ServerImpl ?: return

        data["members"].asJsonArray.map { it.asJsonObject }.forEach {
            val userObject = it["user"].asJsonObject
            val userId = userObject["id"].asLong

            server.members.updateOrPut(userId, it) {
                MemberImpl(
                        client, it, server,
                        client.users.getOrPut(userId) {
                            UserImpl(client, userObject)
                        }
                )
            }
        }
    }
}